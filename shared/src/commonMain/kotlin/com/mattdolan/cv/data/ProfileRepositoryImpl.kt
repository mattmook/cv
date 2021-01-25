/*
 * Copyright 2021 Matthew Dolan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mattdolan.cv.data

import com.mattdolan.cv.data.database.CvDatabaseDataSource
import com.mattdolan.cv.data.network.CvNetworkDataSource
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.collections.set

internal class ProfileRepositoryImpl(
    private val cacheController: CacheController,
    private val cvNetworkDataSource: CvNetworkDataSource,
    private val cvDatabaseDataSource: CvDatabaseDataSource
) : ProfileRepository {

    private val mutex = Mutex()
    private val inflightRequests = mutableMapOf<suspend () -> Any, Deferred<Any?>>()

    private suspend fun <T : Any> cached(getDatabase: () -> T?, setDatabase: (T) -> Unit, getNetwork: suspend () -> T): T = coroutineScope {
        val job = mutex.withLock {
            cacheController.verifyCache {
                cvDatabaseDataSource.clearDatabase()
            }

            inflightRequests[getNetwork] ?: async {
                getDatabase() ?: getNetwork().also {
                    setDatabase(it)
                }
            }.apply {
                inflightRequests[getNetwork] = this
            }
        }

        try {
            @Suppress("UNCHECKED_CAST")
            job.await() as T
        } finally {
            @Suppress("DeferredResultUnused")
            mutex.withLock {
                inflightRequests.remove(getNetwork)
            }
        }
    }

    override suspend fun personalDetails(): PersonalDetails = cached(
        cvDatabaseDataSource::getPersonalDetails,
        cvDatabaseDataSource::setPersonalDetails,
        cvNetworkDataSource::getPersonalDetails
    )

    override suspend fun experiences(): List<Experience> = cached(
        cvDatabaseDataSource::getExperiences,
        cvDatabaseDataSource::setExperiences,
        cvNetworkDataSource::getExperiences
    )

    override suspend fun skills(): List<Skill> = cached(
        cvDatabaseDataSource::getSkills,
        cvDatabaseDataSource::setSkills,
        cvNetworkDataSource::getSkills
    )

    override suspend fun roleDetails(role: Role): RoleDetails = cached(
        { cvDatabaseDataSource.getRoleDetails(role) },
        { cvDatabaseDataSource.setRoleDetails(role, it) },
        { cvNetworkDataSource.getRoleDetails(role) }
    )
}
