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

package com.mattdolan.cv.data.database

import com.mattdolan.cv.data.nextExperience
import com.mattdolan.cv.data.nextList
import com.mattdolan.cv.data.nextPersonalDetails
import com.mattdolan.cv.data.nextSkill
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.test.Test

/**
 * Tests to verify the contract with the database and that data maps as expected
 */
class CvDatabaseDataSourceImplTest {

    private val cvDatabaseDataSource = CvDatabaseDataSourceImpl(AppDatabase(testDriver()))

    @Test
    fun databaseInitiallyEmpty() {
        // Then the database is empty
        cvDatabaseDataSource.getExperiences().shouldBeNull()
        cvDatabaseDataSource.getPersonalDetails().shouldBeNull()
        cvDatabaseDataSource.getSkills().shouldBeNull()
    }

    @Test
    fun savesPersonalDetails() {
        // Given a PersonalDetails object set in the database
        val personalDetails = Random.nextPersonalDetails()
        cvDatabaseDataSource.setPersonalDetails(personalDetails)

        // When we get personal details from the database
        val result = cvDatabaseDataSource.getPersonalDetails()

        // Then the result matches the original data stored
        result.shouldBe(personalDetails)
    }

    @Test
    fun savesSkills() {
        // Given a list of Skill objects set in the database
        val skills = Random.nextList { Random.nextSkill() }
        cvDatabaseDataSource.setSkills(skills)

        // When we get skills from the database
        val result = cvDatabaseDataSource.getSkills()

        // Then the result matches the original data stored
        result.shouldContainExactly(skills)
    }

    @Test
    fun savesExperiences() {
        // Given a list of Experience objects set in the database
        val experiences = Random.nextList { Random.nextExperience() }
        cvDatabaseDataSource.setExperiences(experiences)

        // When we get experiences from the database
        val results = cvDatabaseDataSource.getExperiences()

        // Then the result matches the original data stored
        results.shouldContainExactly(experiences)
    }
}
