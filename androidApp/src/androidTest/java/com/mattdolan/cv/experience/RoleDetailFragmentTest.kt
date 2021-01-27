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

package com.mattdolan.cv.experience

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.data.MockProfileRepository
import com.mattdolan.cv.di.RepositoryModule
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.test.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class RoleDetailFragmentTest : TestCase() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockProfileRepository = MockProfileRepository()

    @BindValue
    @JvmField
    val profileRepository: ProfileRepository = mockProfileRepository

    @Test
    fun showsErrorWhenRepositoryFails() {
        // Given the repository throws an exception when loading
        mockProfileRepository.mode = MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.All)

        // When we launch the fragment
        val experience = mockProfileRepository._experiences.random()
        val role = experience.roles.random()
        val args = RoleDetailFragmentArgs(RoleDetailArguments(experience, role))
        launchFragmentInHiltContainer<RoleDetailFragment>(args.toBundle(), R.style.AppTheme)

        // Then the error screen is visible
        RoleDetailScreen {
            errorImage {
                isVisible()
            }
        }
    }

    @Test
    fun showsRoleDetailsWhenRetryClicked() {
        // Given the repository throws an exception when loading
        mockProfileRepository.mode = MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.All)

        // And we launch the fragment in error state
        val experience = mockProfileRepository._experiences.random()
        val role = experience.roles.random()
        val args = RoleDetailFragmentArgs(RoleDetailArguments(experience, role))
        launchFragmentInHiltContainer<RoleDetailFragment>(args.toBundle(), R.style.AppTheme)
        RoleDetailScreen {
            retryButton {
                isVisible()
            }
        }

        // When the repository returns data successfully
        mockProfileRepository.mode = MockProfileRepository.Mode.ReturnData

        // And we click the retry button
        RoleDetailScreen {
            retryButton {
                click()
            }
        }

        // Then the ready screen is visible
        RoleDetailScreen {
            description {
                isVisible()
            }
        }
    }

    @Test
    fun showsRoleDetailsWhenRepositorySucceeds() {
        // When the repository returns data successfully
        mockProfileRepository.mode = MockProfileRepository.Mode.ReturnData

        // When we launch the fragment
        val experience = mockProfileRepository._experiences.random()
        val role = experience.roles.random()
        val args = RoleDetailFragmentArgs(RoleDetailArguments(experience, role))
        launchFragmentInHiltContainer<RoleDetailFragment>(args.toBundle(), R.style.AppTheme)

        // Then the ready screen is visible and populated
        RoleDetailScreen {
            description {
                isVisible()
                hasSize(mockProfileRepository._roleDetails.description.size)
                mockProfileRepository._roleDetails.description.forEachIndexed { index, text ->
                    childAt<RoleDetailScreen.DescriptionItem>(index) {
                        primaryText.hasText(text)
                    }
                }
            }
        }
    }

    @Test
    fun navigatesBack() {
        // Given we launch the fragment
        val experience = mockProfileRepository._experiences.random()
        val role = experience.roles.random()
        val args = RoleDetailFragmentArgs(RoleDetailArguments(experience, role))

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.roleDetailFragment, args.toBundle())
        }

        launchFragmentInHiltContainer<RoleDetailFragment>(args.toBundle(), R.style.AppTheme) {
            Navigation.setViewNavController(requireView(), navController)
        }

        // And the nav graph is on the role detail fragment
        navController.currentDestination?.id.shouldBe(R.id.roleDetailFragment)

        // When we navigate back
        RoleDetailScreen {
            backButton {
                click()
            }
        }

        // Then we navigate back to the profile fragmeent
        navController.currentDestination?.id.shouldBe(R.id.profileFragment)
    }
}
