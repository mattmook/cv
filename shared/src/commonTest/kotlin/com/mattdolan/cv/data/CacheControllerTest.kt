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
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.days
import kotlin.time.minutes

@Suppress("EXPERIMENTAL_API_USAGE_ERROR")
class CacheControllerTest {

    private val mockSettings = MockSettings()
    private val cacheController = CacheController.OneDay(mockSettings)

    @Test
    fun storageResetWhenSettingsEmpty() {
        // When we verify the cache
        var resetStorageCalled = false
        cacheController.verifyCache { resetStorageCalled = true }

        // Then storage is reset
        resetStorageCalled.shouldBeTrue()

        // And settings is updated to now
        val now = Clock.System.now()
        (mockSettings.data["cache_timestamp"] in (now - Duration.minutes(1)).toEpochMilliseconds()..now.toEpochMilliseconds()).shouldBeTrue()
    }

    @Test

    fun storageResetsWhenCacheOlderThanOneDay() {
        // Given settings with a value older than one day
        mockSettings.data["cache_timestamp"] = Clock.System.now().minus(Duration.days(1) + Duration.minutes(1)).toEpochMilliseconds()

        // When we verify the cache
        var resetStorageCalled = false
        cacheController.verifyCache { resetStorageCalled = true }

        // Then storage is reset
        resetStorageCalled.shouldBeTrue()

        // And settings is updated to now
        val now = Clock.System.now()
        (mockSettings.data["cache_timestamp"] in (now - Duration.minutes(1)).toEpochMilliseconds()..now.toEpochMilliseconds()).shouldBeTrue()
    }

    @Test
    fun storageRetainedWhenCacheYoungerThanOneDay() {
        // Given settings with a value younger than one day
        val oneDayAgo = Clock.System.now().minus(Duration.days(1) - Duration.minutes(1)).toEpochMilliseconds()
        mockSettings.data["cache_timestamp"] = oneDayAgo

        // When we verify the cache
        var resetStorageCalled = false
        cacheController.verifyCache { resetStorageCalled = true }

        // Then storage is not reset
        resetStorageCalled.shouldBeFalse()

        // And settings is not updated
        mockSettings.data["cache_timestamp"].shouldBe(oneDayAgo)
    }

    class MockSettings : Settings {
        val data = mutableMapOf<String, Any>()

        override val keys: Set<String>
            get() = data.keys
        override val size: Int
            get() = data.size

        override fun clear() = data.clear()

        override fun getBoolean(key: String, defaultValue: Boolean) = data.getOrElse(key) { defaultValue } as Boolean
        override fun getBooleanOrNull(key: String) = data[key] as Boolean?

        override fun getDouble(key: String, defaultValue: Double) = data.getOrElse(key) { defaultValue } as Double
        override fun getDoubleOrNull(key: String) = data[key] as Double?

        override fun getFloat(key: String, defaultValue: Float) = data.getOrElse(key) { defaultValue } as Float
        override fun getFloatOrNull(key: String) = data[key] as Float?

        override fun getInt(key: String, defaultValue: Int) = data.getOrElse(key) { defaultValue } as Int
        override fun getIntOrNull(key: String) = data[key] as Int?

        override fun getLong(key: String, defaultValue: Long) = data.getOrElse(key) { defaultValue } as Long
        override fun getLongOrNull(key: String) = data[key] as Long?

        override fun getString(key: String, defaultValue: String) = data.getOrElse(key) { defaultValue } as String
        override fun getStringOrNull(key: String) = data[key] as String?

        override fun hasKey(key: String) = data.containsKey(key)

        override fun putBoolean(key: String, value: Boolean) {
            data[key] = value
        }

        override fun putDouble(key: String, value: Double) {
            data[key] = value
        }

        override fun putFloat(key: String, value: Float) {
            data[key] = value
        }

        override fun putInt(key: String, value: Int) {
            data[key] = value
        }

        override fun putLong(key: String, value: Long) {
            data[key] = value
        }

        override fun putString(key: String, value: String) {
            data[key] = value
        }

        override fun remove(key: String) {
            data.remove(key)
        }
    }
}
