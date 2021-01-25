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

import com.mattdolan.cv.data.CacheController
import com.mattdolan.cv.data.ProfileRepositoryImpl
import com.mattdolan.cv.data.database.AppDatabase
import com.mattdolan.cv.data.database.CvDatabaseDataSourceImpl
import com.mattdolan.cv.data.database.DriverFactory
import com.mattdolan.cv.data.network.CvNetworkDataSourceImpl
import com.mattdolan.cv.domain.ProfileRepository
import com.russhwolf.settings.AppleSettings

actual class Sdk {

    actual fun profileRepository(): ProfileRepository {
        val settings = AppleSettings.Factory().create()
        val cvNetworkDataSource = CvNetworkDataSourceImpl()
        val appDatabase = AppDatabase(DriverFactory().createDriver())
        val cvDatabaseDataSource = CvDatabaseDataSourceImpl(appDatabase)

        return ProfileRepositoryImpl(CacheController.OneDay(settings), cvNetworkDataSource, cvDatabaseDataSource)
    }
}
