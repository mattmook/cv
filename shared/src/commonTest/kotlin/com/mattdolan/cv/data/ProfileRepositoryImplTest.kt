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

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.mattdolan.cv.data.database.CvDatabaseDataSource
import com.mattdolan.cv.data.network.CvNetworkDataSource
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill
import com.mattdolan.cv.test.runBlocking
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.test.Test

class ProfileRepositoryImplTest {

    private val mockCacheController = MockCacheController()
    private val mockNetworkDataSource = MockNetworkDataSource()
    private val mockDatabaseDataSource = MockDatabaseDataSource()

    private val profileRepository = ProfileRepositoryImpl(mockCacheController, mockNetworkDataSource, mockDatabaseDataSource)

    @Test
    fun personalDetailsCallsCacheController() {
        runBlocking {
            // Given database has personal details
            val personalDetails = Random.nextPersonalDetails()
            mockDatabaseDataSource._personalDetails.set(personalDetails)

            // And cacheController will reset storage
            mockCacheController._resetStorage.value = true

            // When we request personal details
            profileRepository.personalDetails()

            // Then the cache controller is called
            mockCacheController.actions.contains("verifyCache")

            // And the database reset
            mockDatabaseDataSource.actions.shouldContain("clearDatabase")
        }
    }

    @Test
    fun experiencesCallsCacheController() {
        runBlocking {
            // Given database has experiences
            val experiences = Random.nextList { Random.nextExperience() }
            mockDatabaseDataSource._experiences.set(experiences)

            // And cacheController will reset storage
            mockCacheController._resetStorage.value = true

            // When we request experiences
            profileRepository.experiences()

            // Then the cache controller is called
            mockCacheController.actions.contains("verifyCache")

            // And the database reset
            mockDatabaseDataSource.actions.shouldContain("clearDatabase")
        }
    }

    @Test
    fun skillsCallsCacheController() {
        runBlocking {
            // Given database has skills
            val skills = Random.nextList { Random.nextSkill() }
            mockDatabaseDataSource._skills.set(skills)

            // And cacheController will reset storage
            mockCacheController._resetStorage.value = true

            // When we request skills
            profileRepository.skills()

            // Then the cache controller is called
            mockCacheController.actions.contains("verifyCache")

            // And the database reset
            mockDatabaseDataSource.actions.shouldContain("clearDatabase")
        }
    }

    @Test
    fun roleDetailsCallsCacheController() {
        runBlocking {
            // Given database has role details
            val roleDetails = Random.nextRoleDetails()
            mockDatabaseDataSource._roleDetails.set(roleDetails)

            // And cacheController will reset storage
            mockCacheController._resetStorage.value = true

            // When we request role details
            profileRepository.roleDetails(Random.nextRole())

            // Then the cache controller is called
            mockCacheController.actions.contains("verifyCache")

            // And the database reset
            mockDatabaseDataSource.actions.shouldContain("clearDatabase")
        }
    }

    @Test
    fun personalDetailsReturnedFromDatabase() {
        runBlocking {
            // Given database has personal details
            val personalDetails = Random.nextPersonalDetails()
            mockDatabaseDataSource._personalDetails.set(personalDetails)

            // When we request from the repository
            val result = profileRepository.personalDetails()

            // Then personal details are returned
            result.shouldBe(personalDetails)

            // And the network is not queried
            mockNetworkDataSource.actions.shouldBeEmpty()
        }
    }

    @Test
    fun experiencesReturnedFromDatabase() {
        runBlocking {
            // Given database has experiences
            val experiences = Random.nextList { Random.nextExperience() }
            mockDatabaseDataSource._experiences.set(experiences)

            // When we request from the repository
            val result = profileRepository.experiences()

            // Then experiences are returned
            result.shouldContainExactly(experiences)

            // And the network is not queried
            mockNetworkDataSource.actions.shouldBeEmpty()
        }
    }

    @Test
    fun skillsReturnedFromDatabase() {
        runBlocking {
            // Given database has skills
            val skills = Random.nextList { Random.nextSkill() }
            mockDatabaseDataSource._skills.set(skills)

            // When we request from the repository
            val result = profileRepository.skills()

            // Then skills are returned
            result.shouldContainExactly(skills)

            // And the network is not queried
            mockNetworkDataSource.actions.shouldBeEmpty()
        }
    }

    @Test
    fun roleDetailsReturnedFromDatabase() {
        runBlocking {
            // Given database has role details
            val roleDetails = Random.nextRoleDetails()
            mockDatabaseDataSource._roleDetails.set(roleDetails)

            // When we request from the repository
            val result = profileRepository.roleDetails(Random.nextRole())

            // Then role details are returned
            result.shouldBe(roleDetails)

            // And the network is not queried
            mockNetworkDataSource.actions.shouldBeEmpty()
        }
    }

    @Test
    fun personalDetailsReturnedFromNetworkWhenDatabaseEmpty() {
        runBlocking {
            // Given database is empty and network has personal details
            val personalDetails = Random.nextPersonalDetails()
            mockNetworkDataSource._personalDetails.set(personalDetails)

            // When we request from the repository
            val result = profileRepository.personalDetails()

            // Then personal details are returned
            result.shouldBe(personalDetails)

            // And saved to the database
            mockDatabaseDataSource._personalDetails.get().shouldBe(personalDetails)

            // And the network is queried
            mockNetworkDataSource.actions.shouldContainExactly(listOf("getPersonalDetails"))
        }
    }

    @Test
    fun experiencesReturnedFromNetworkWhenDatabaseEmpty() {
        runBlocking {
            // Given database is empty and network has experiences
            val experiences = Random.nextList { Random.nextExperience() }
            mockNetworkDataSource._experiences.set(experiences)

            // When we request from the repository
            val result = profileRepository.experiences()

            // Then experiences are returned
            result.shouldBe(experiences)

            // And saved to the database
            mockDatabaseDataSource._experiences.get().shouldBe(experiences)

            // And the network is queried
            mockNetworkDataSource.actions.shouldContainExactly(listOf("getExperiences"))
        }
    }

    @Test
    fun skillsReturnedFromNetworkWhenDatabaseEmpty() {
        runBlocking {
            // Given database is empty and network has skills
            val skills = Random.nextList { Random.nextSkill() }
            mockNetworkDataSource._skills.set(skills)

            // When we request from the repository
            val result = profileRepository.skills()

            // Then skills are returned
            result.shouldBe(skills)

            // And saved to the database
            mockDatabaseDataSource._skills.get().shouldBe(skills)

            // And the network is queried
            mockNetworkDataSource.actions.shouldContainExactly(listOf("getSkills"))
        }
    }

    @Test
    fun roleDetailsReturnedFromNetworkWhenDatabaseEmpty() {
        runBlocking {
            // Given database is empty and network has role details
            val roleDetails = Random.nextRoleDetails()
            mockNetworkDataSource._roleDetails.set(roleDetails)

            // When we request from the repository
            val result = profileRepository.roleDetails(Random.nextRole())

            // Then role details are returned
            result.shouldBe(roleDetails)

            // And saved to the database
            mockDatabaseDataSource._roleDetails.get().shouldBe(roleDetails)

            // And the network is queried
            mockNetworkDataSource.actions.shouldContainExactly(listOf("getRoleDetails"))
        }
    }

    @Suppress("PropertyName")
    private class MockCacheController : CacheController {
        val actions = sharedMutableListOf<String>()
        var _resetStorage = AtomicBoolean(false)

        override fun verifyCache(resetStorage: () -> Unit) {
            actions.add("verifyCache")
            if (_resetStorage.value) resetStorage()
        }
    }

    @Suppress("PropertyName")
    private class MockNetworkDataSource : CvNetworkDataSource {
        val actions = sharedMutableListOf<String>()
        var _experiences = AtomicReference<List<Experience>?>(null)
        var _skills = AtomicReference<List<Skill>?>(null)
        var _personalDetails = AtomicReference<PersonalDetails?>(null)
        var _roleDetails = AtomicReference<RoleDetails?>(null)

        override suspend fun getExperiences(): List<Experience> {
            actions.add("getExperiences")
            return _experiences.get() ?: throw IllegalStateException("Not populated")
        }

        override suspend fun getSkills(): List<Skill> {
            actions.add("getSkills")
            return _skills.get() ?: throw IllegalStateException("Not populated")
        }

        override suspend fun getPersonalDetails(): PersonalDetails {
            actions.add("getPersonalDetails")
            return _personalDetails.get() ?: throw IllegalStateException("Not populated")
        }

        override suspend fun getRoleDetails(role: Role): RoleDetails {
            actions.add("getRoleDetails")
            return _roleDetails.get() ?: throw IllegalStateException("Not populated")
        }
    }

    @Suppress("PropertyName")
    private class MockDatabaseDataSource : CvDatabaseDataSource {
        val actions = sharedMutableListOf<String>()
        var _experiences = AtomicReference<List<Experience>?>(null)
        var _skills = AtomicReference<List<Skill>?>(null)
        var _personalDetails = AtomicReference<PersonalDetails?>(null)
        var _roleDetails = AtomicReference<RoleDetails?>(null)

        override fun clearDatabase() {
            actions.add("clearDatabase")
        }

        override fun getExperiences(): List<Experience>? {
            actions.add("getExperiences")
            return _experiences.get()
        }

        override fun setExperiences(value: List<Experience>) {
            actions.add("setExperiences")
            _experiences.set(value)
        }

        override fun getPersonalDetails(): PersonalDetails? {
            actions.add("getPersonalDetails")
            return _personalDetails.get()
        }

        override fun setPersonalDetails(value: PersonalDetails) {
            actions.add("setPersonalDetails")
            _personalDetails.set(value)
        }

        override fun getSkills(): List<Skill>? {
            actions.add("getSkills")
            return _skills.get()
        }

        override fun setSkills(value: List<Skill>) {
            actions.add("setSkills")
            _skills.set(value)
        }

        override fun getRoleDetails(role: Role): RoleDetails? {
            actions.add("getRoleDetails")
            return _roleDetails.get()
        }

        override fun setRoleDetails(role: Role, roleDetails: RoleDetails) {
            actions.add("setRoleDetails")
            this._roleDetails.set(roleDetails)
        }
    }
}
