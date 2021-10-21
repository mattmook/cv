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

package com.mattdolan.cv

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import coil.compose.LocalImageLoader
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.di.ImageLoaderModule
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.Skill
import com.mattdolan.cv.profile.ProfileReadyScreen
import com.mattdolan.cv.profile.ProfileScreen
import com.mattdolan.cv.profile.model.ProfileState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(R.layout.main_activity)
        //if (true) return

        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides ImageLoaderModule.provideImageLoader(LocalContext.current)
            ) {

                //ErrorScreen(stringResource(R.string.sorry), stringResource(R.string.retry), {})
                ProfileScreen(viewModel = viewModel())
            }
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
