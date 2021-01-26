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

import android.os.Build
import android.os.Looper
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mattdolan.cv.MainActivity
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.test.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

// This test does not run through the IDE
// See https://github.com/google/dagger/issues/1956
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(sdk = [Build.VERSION_CODES.P], application = HiltTestApplication::class)
class ProfileFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    //@get:Rule
    //val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /*@Test
    fun testThis() {
        //activityRule.
        val activityScenario = launch(MainActivity::class.java)

        onView(withId(R.id.error_button)).perform(click())


        //val scenario = launchFragmentInContainer<ProfileFragment>()
        //activityRule.scenario

        activityScenario.close()
    }*/

    fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadUntilIdle()
                uiController?.loopMainThreadForAtLeast(delay)
                uiController?.loopMainThreadUntilIdle()
            }

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }
        }
    }

    @Test
    fun testSomethingElse() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setGraph(R.navigation.nav_graph)
        }

        val scenario = launchFragmentInHiltContainer<ProfileFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }


        shadowOf(Looper.getMainLooper()).idle()

        onView(isRoot()).perform(waitFor(10000))

        //onView(withId(R.id.company)).perform(click())

        onView(withId(R.id.error_button)).perform(click())


    }
}