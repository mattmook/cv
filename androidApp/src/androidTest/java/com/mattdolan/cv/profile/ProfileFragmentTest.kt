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

package com.mattdolan.cv.profile

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.data.MockProfileRepository
import com.mattdolan.cv.di.RepositoryModule
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.experience.RoleDetailFragmentArgs
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
class ProfileFragmentTest : TestCase() {

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
        launchFragmentInHiltContainer<ProfileFragment>(null, R.style.AppTheme)

        // Then the error screen is visible
        ProfileScreen {
            errorImage {
                isVisible()
            }
        }
    }

    @Test
    fun showsProfileDetailsWhenRetryClicked() {
        // Given the repository throws an exception when loading
        mockProfileRepository.mode = MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.All)

        // And we launch the fragment in error state
        launchFragmentInHiltContainer<ProfileFragment>(null, R.style.AppTheme)
        ProfileScreen {
            retryButton {
                isVisible()
            }
        }

        // When the repository returns data successfully
        mockProfileRepository.mode = MockProfileRepository.Mode.ReturnData

        // And we click the retry button
        ProfileScreen {
            retryButton {
                click()
            }
        }

        // Then the ready screen is visible
        ProfileScreen {
            experiences {
                isVisible()
            }
        }
    }

    @Test
    fun showsProfileDetailsWhenRepositorySucceeds() {
        // When the repository returns data successfully
        mockProfileRepository.mode = MockProfileRepository.Mode.ReturnData

        // When we launch the fragment
        launchFragmentInHiltContainer<ProfileFragment>(null, R.style.AppTheme)

        // Then the ready screen is visible
        ProfileScreen {
            experiences {
                isVisible()
            }
        }
    }

    @Test
    fun navigatesToRoleDetails() {
        // Given the repository returns data successfully
        val experience = mockProfileRepository._experiences.random()
        val role = experience.roles.random()
        mockProfileRepository.mode = MockProfileRepository.Mode.ReturnData

        // And we launch the fragment
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        runOnUiThread {
            navController.setGraph(R.navigation.nav_graph)
        }
        launchFragmentInHiltContainer<ProfileFragment>(null, R.style.AppTheme) {
            Navigation.setViewNavController(requireView(), navController)
        }

        // When we click on a role
        ProfileScreen {
            experiences {
                /*scrollTo {
                    withDescendant { withText(role.title) }
                }*/

                childWith<ProfileScreen.RoleItem> {
                    withDescendant { withText(role.title) }
                }.perform {
                    click()
                }
            }
        }

        // Then we navigate to the details screen
        navController.currentDestination?.id.shouldBe(R.id.roleDetailFragment)

        // And the arguments are as expected
        val args = RoleDetailFragmentArgs.fromBundle(navController.backStack.last().arguments!!).role
        args.role.shouldBe(role)
        args.experience.shouldBe(experience)
    }
}
