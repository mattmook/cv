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

package com.mattdolan.cv.test

import platform.Foundation.NSDate
import platform.Foundation.NSRunLoop
import platform.Foundation.addTimeInterval
import platform.Foundation.runUntilDate

class Expectation<T> {
    private var waiting = true
    private var result: T? = null

    fun fulfill(result: T?) {
        waiting = false
        this.result = result
    }

    fun wait(): T? {
        while (waiting) {
            advanceRunLoop()
        }

        return result
    }
}

private fun advanceRunLoop() {
    val date = NSDate().addTimeInterval(1.0) as NSDate
    NSRunLoop.mainRunLoop.runUntilDate(date)
}
