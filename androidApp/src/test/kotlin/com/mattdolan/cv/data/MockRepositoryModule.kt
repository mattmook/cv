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

package com.mattdolan.cv.data;

import com.mattdolan.cv.di.RepositoryModule
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
object MockRepositoryModule {

    @Provides
    fun provideProfileRepository() = object : ProfileRepository {
        override suspend fun personalDetails(): PersonalDetails {
            println("called personalDetails")
            TODO("Not yet implemented")
        }

        override suspend fun experiences(): List<Experience> {
            println("called experiences")
            TODO("Not yet implemented")
        }

        override suspend fun skills(): List<Skill> {
            println("called skills")
            TODO("Not yet implemented")
        }

        override suspend fun roleDetails(role: Role): RoleDetails {
            println("called roleDetails")
            TODO("Not yet implemented")
        }
    }
}
