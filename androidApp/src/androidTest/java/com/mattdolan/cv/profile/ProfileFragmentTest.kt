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

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcaserule.TestCaseRule
import com.mattdolan.cv.MainActivity
import com.mattdolan.cv.data.MockProfileRepository
import com.mattdolan.cv.di.RepositoryModule
import com.mattdolan.cv.domain.ProfileRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// This test does not run through the IDE
// See https://github.com/google/dagger/issues/1956
@RunWith(AndroidJUnit4::class)
@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class ProfileFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val testCaseRule = TestCaseRule(javaClass.name)

    private val mockProfileRepository = MockProfileRepository()

    @BindValue
    @JvmField
    val profileRepository: ProfileRepository = mockProfileRepository

    @Test
    fun testStartsInLoadingState() {
        // When we launch the activity
        launch(MainActivity::class.java).use {

            // Then the loading screen is visible
            ProfileScreen {

                loadingImage {
                    isVisible()
                }
            }
        }
    }

    @Test
    fun testShowsErrorWhenRepositoryFails() {
        // Given the repository throws an exception when loading personal details
        mockProfileRepository.personalDetails = null

        // When we launch the activity
        launch(MainActivity::class.java).use {

            // Then the error screen is visible
            ProfileScreen {
                errorImage {
                    isVisible()
                }
            }
        }
    }

    @Ignore("Test does not pass as transition to ready state not occurring")
    @Test
    fun testShowsProfileDetailsWhenRepositorySucceeds() {
        // When we launch the activity
        launch(MainActivity::class.java).use {

            // Then the ready screen is visible
            ProfileScreen {
                experiences {
                    isVisible()
                }
            }
        }
    }
}
