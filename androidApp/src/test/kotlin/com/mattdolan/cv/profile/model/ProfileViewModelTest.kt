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

package com.mattdolan.cv.profile.model

import androidx.lifecycle.SavedStateHandle
import com.mattdolan.cv.common.SideEffect
import com.mattdolan.cv.data.MockProfileRepository
import com.mattdolan.cv.data.nextExperience
import com.mattdolan.cv.data.nextList
import com.mattdolan.cv.data.nextPersonalDetails
import com.mattdolan.cv.data.nextRole
import com.mattdolan.cv.data.nextSkill
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.orbitmvi.orbit.test
import kotlin.random.Random

class ProfileViewModelTest {

    private val mockProfileRepository = MockProfileRepository()

    private val profileViewModel = ProfileViewModel(mockProfileRepository, SavedStateHandle())

    @Test
    fun `onCreate loads details when initialState not Ready`() {
        runBlocking {
            // Given all details load

            // When onCreate called
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.runOnCreate()

            // Then we output loading then ready
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    {
                        ProfileState.Ready(
                            personalDetails = mockProfileRepository._personalDetails,
                            skills = mockProfileRepository._skills,
                            experiences = mockProfileRepository._experiences
                        )
                    }
                )
            }
        }
    }

    @Test
    fun `onCreate outputs Loading then Error when personalDetails fails`() {
        runBlocking {
            // Given personal details fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.PersonalDetails)

            // When onCreate called
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.runOnCreate()

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `onCreate outputs Loading then Error when experiences fails`() {
        runBlocking {
            // Given experiences fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.Experience)

            // When onCreate called
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.runOnCreate()

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `onCreate outputs Loading then Error when skills fails`() {
        runBlocking {
            // Given skills fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.Skill)

            // When onCreate called
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.runOnCreate()

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `onCreate does nothing when initialState Ready`() {
        runBlocking {
            // Given a ready initial state
            val initialState = ProfileState.Ready(
                personalDetails = Random.nextPersonalDetails(),
                skills = Random.nextList { Random.nextSkill() },
                experiences = Random.nextList { Random.nextExperience() }
            )

            // When onCreate called
            val testContainer = profileViewModel.test(initialState = initialState)
            testContainer.runOnCreate()

            // Then no state changes occur
            testContainer.assert(initialState)
        }
    }

    @Test
    fun `loadProfile outputs Loading then Ready`() {
        runBlocking {
            // Given all details load

            // When we load profile
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.testIntent { loadProfile() }

            // Then we output loading then ready
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    {
                        ProfileState.Ready(
                            personalDetails = mockProfileRepository._personalDetails,
                            skills = mockProfileRepository._skills,
                            experiences = mockProfileRepository._experiences
                        )
                    }
                )
            }
        }
    }

    @Test
    fun `loadProfile outputs Loading then Error when personalDetails fails`() {
        runBlocking {
            // Given personal details fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.PersonalDetails)

            // When we load profile
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.testIntent { loadProfile() }

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `loadProfile outputs Loading then Error when experiences fails`() {
        runBlocking {
            // Given experiences fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.Experience)

            // When we load profile
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.testIntent { loadProfile() }

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `loadProfile outputs Loading then Error when skills fails`() {
        runBlocking {
            // Given skills fails to load
            mockProfileRepository.mode =
                MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.Skill)

            // When we load profile
            val testContainer = profileViewModel.test(initialState = ProfileState.Error)
            testContainer.testIntent { loadProfile() }

            // Then we output loading then error
            testContainer.assert(ProfileState.Error) {
                states(
                    { ProfileState.Loading },
                    { ProfileState.Error }
                )
            }
        }
    }

    @Test
    fun `selectRole sends sideEffect when role exists`() {
        runBlocking {
            // Given a Ready initial state
            val initialState = ProfileState.Ready(
                personalDetails = Random.nextPersonalDetails(),
                skills = Random.nextList { Random.nextSkill() },
                experiences = Random.nextList { Random.nextExperience() }
            )

            // When we select an existing role
            val experience = initialState.experiences.random()
            val role = experience.roles.random()
            val testContainer = profileViewModel.test(initialState = initialState)
            testContainer.testIntent { selectRole(role) }

            // Then a side effect posted
            testContainer.assert(initialState) {
                postedSideEffects(SideEffect.NavigateToRoleDetails(experience, role))
            }
        }
    }

    @Test
    fun `selectRole does nothing when role does not exist`() {
        runBlocking {
            // Given a Ready initial state
            val initialState = ProfileState.Ready(
                personalDetails = Random.nextPersonalDetails(),
                skills = Random.nextList { Random.nextSkill() },
                experiences = Random.nextList { Random.nextExperience() }
            )

            // When we select a random role that does not exist
            val role = Random.nextRole()
            val testContainer = profileViewModel.test(initialState = initialState)
            testContainer.testIntent { selectRole(role) }

            // Then no side effect posted
            testContainer.assert(initialState)
        }
    }

    @Test
    fun `selectRole does nothing when initialState not Ready`() {
        runBlocking {
            // Given a Loading initial state
            val initialState = ProfileState.Loading

            // When we select a random role that does not exist
            val role = Random.nextRole()
            val testContainer = profileViewModel.test(initialState = initialState)
            testContainer.testIntent { selectRole(role) }

            // Then no side effect posted
            testContainer.assert(initialState)
        }
    }
}
