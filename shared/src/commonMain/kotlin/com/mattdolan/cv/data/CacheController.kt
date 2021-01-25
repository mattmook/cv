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

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil

internal interface CacheController {
    fun verifyCache(resetStorage: () -> Unit)

    class OneDay(private val settings: Settings) : CacheController {

        override fun verifyCache(resetStorage: () -> Unit) {
            val now = Clock.System.now()
            val cache = settings.getLongOrNull("cache_timestamp")?.let(Instant::fromEpochMilliseconds)

            // Cache for 1 day
            if (cache == null || cache.daysUntil(now, TimeZone.UTC) >= 1) {
                resetStorage()
                settings["cache_timestamp"] = now.toEpochMilliseconds()
            }
        }
    }
}
