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

package com.mattdolan.cv.common.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class DebounceKtTest {

    private val debounceTime = 500L
    private val testFun = mock<() -> Unit> {
        onGeneric { invoke() } doReturn Unit
    }

    @Test
    fun `triggers only once when called within debounce time`() = runBlockingTest {
        // Given a debounce function
        val debouncedTestFun = debounce(debounceTime, this, testFun)

        // When we call it quickly
        debouncedTestFun()
        advanceTimeBy(200L)
        debouncedTestFun()

        // Then it triggers only once
        verify(testFun, times(1)).invoke()
    }

    @Test
    fun `triggers every time when called outside debounce time`() = runBlockingTest {
        // Given a debounce function
        val debouncedTestFun = debounce(debounceTime, this, testFun)

        // When we call it slowly
        debouncedTestFun()
        advanceTimeBy(1000L)
        debouncedTestFun()

        // Then it triggers twice
        verify(testFun, times(2)).invoke()
    }
}
