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

package com.mattdolan.cv.data.network

import com.mattdolan.cv.test.IgnoreIos
import com.mattdolan.cv.test.runBlocking
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.http.Url
import kotlin.test.Test

/**
 * Tests to verify the contract with the backend and that data maps as expected
 */
class CvNetworkDataSourceImplTest {

    private val networkDataSource = CvNetworkDataSourceImpl()

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun hasAtLeastOneExperience() {
        runBlocking {
            // Given we retrieve experiences
            val experiences = networkDataSource.getExperiences()

            // Then it should contain at least one experience
            experiences.shouldNotBeEmpty()
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun allExperiencesHaveData() {
        runBlocking {
            // Given we retrieve experiences
            val experiences = networkDataSource.getExperiences()

            // Then all experiences have data
            experiences.forEach {
                it.company.shouldNotBeBlank()
                it.logoUrl.shouldNotBeBlank()
                it.industry.shouldNotBeBlank()
                it.location.shouldNotBeBlank()
                it.period.shouldNotBeBlank()
                it.roles.shouldNotBeEmpty()
            }
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun allExperiencesLogosExist() {
        runBlocking {
            // Given we retrieve experiences
            val experiences = networkDataSource.getExperiences()

            // Then all experiences logos exist
            experiences.forEach {
                shouldNotThrowAny {
                    HttpClient().head<Unit>(Url(it.logoUrl))
                }
            }
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun allRolesHaveData() {
        runBlocking {
            // Given we retrieve experiences
            val experiences = networkDataSource.getExperiences()

            // Then all roles have data
            experiences.flatMap { it.roles }.forEach {
                it.title.shouldNotBeBlank()
                it.team?.shouldNotBeBlank()
                it.period.shouldNotBeBlank()
                it.detailUrl.shouldNotBeBlank()
            }
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun allRoleDetailsHaveData() {
        runBlocking {
            // Given we retrieve experiences
            val experiences = networkDataSource.getExperiences()

            // When we retrieve all role details
            val allRoleDetails = experiences.flatMap { it.roles }.map {
                networkDataSource.getRoleDetails(it)
            }

            // Then all roleDetails have data
            allRoleDetails.forEach {
                it.description.shouldNotBeEmpty()
                it.description.forEach { bullet -> bullet.shouldNotBeBlank() }
            }
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun hasAtLeastOneSkill() {
        runBlocking {
            // Given we retrieve skills
            val skills = networkDataSource.getSkills()

            // Then it should contain at least one skill
            skills.shouldNotBeEmpty()
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun allSkillsHaveData() {
        runBlocking {
            // Given we retrieve skills
            val skills = networkDataSource.getSkills()

            // Then all skills have data
            skills.forEach {
                it.skill.shouldNotBeBlank()
                it.since?.shouldBeBetween(1970, 2100)
            }
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun hasPersonalDetails() {
        runBlocking {
            // Given we retrieve personal details
            val personalDetails = networkDataSource.getPersonalDetails()

            // Then personal details has data
            personalDetails.name.shouldNotBeBlank()
            personalDetails.tagline.shouldNotBeBlank()
            personalDetails.location.shouldNotBeBlank()
            personalDetails.avatarUrl.shouldNotBeBlank()
        }
    }

    // iOS throwing "The certificate for this server is invalid"
    // See https://youtrack.jetbrains.com/issue/KT-38317
    @IgnoreIos
    @Test
    fun personalDetailsAvatarExists() {
        runBlocking {
            // Given we retrieve personal details
            val personalDetails = networkDataSource.getPersonalDetails()

            // Then the avatar exists
            shouldNotThrowAny {
                HttpClient().head<Unit>(Url(personalDetails.avatarUrl))
            }
        }
    }
}
