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

import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill
import kotlinx.coroutines.delay
import kotlin.random.Random

class MockProfileRepository : ProfileRepository {

    var mode: Mode = Mode.ReturnData

    val _personalDetails: PersonalDetails = Random.nextPersonalDetails()
    override suspend fun personalDetails(): PersonalDetails = handleMode(DataType.PersonalDetails) { _personalDetails }

    val _experiences: List<Experience> = Random.nextList { Random.nextExperience() }
    override suspend fun experiences(): List<Experience> = handleMode(DataType.Experience) { _experiences }

    val _skills: List<Skill> = Random.nextList { Random.nextSkill() }
    override suspend fun skills(): List<Skill> = handleMode(DataType.Skill) { _skills }

    val _roleDetails: RoleDetails = Random.nextRoleDetails()
    override suspend fun roleDetails(role: Role): RoleDetails = handleMode(DataType.RoleDetails) { _roleDetails }

    private suspend inline fun <T> handleMode(dataType: DataType, block: () -> T): T {
        return when (val cur = mode) {
            Mode.ReturnData -> block()
            Mode.DelayReturn -> {
                delay(5000)
                block()
            }
            is Mode.ThrowException -> {
                if (cur.dataType == DataType.All || cur.dataType == dataType) {
                    throw IllegalStateException("Not populated")
                } else {
                    block()
                }
            }
        }
    }

    sealed class Mode {
        object ReturnData : Mode()
        object DelayReturn : Mode()
        data class ThrowException(val dataType: DataType) : Mode()
    }

    enum class DataType {
        PersonalDetails, Experience, Skill, RoleDetails, All
    }
}
