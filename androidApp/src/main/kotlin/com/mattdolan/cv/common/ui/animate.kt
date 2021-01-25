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

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.mattdolan.cv.common.isRunningEspresso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Drawable.animate(lifecycleScope: CoroutineScope, repeat: Boolean = false, repeatDelayMillis: Long) {
    if (!isRunningEspresso) {
        val animation = (this as Animatable)

        AnimatedVectorDrawableCompat.registerAnimationCallback(
            this,
            object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    if (repeat) {
                        lifecycleScope.launch {
                            delay(repeatDelayMillis)
                            animation.start()
                        }
                    }
                }
            }
        )

        animation.start()
    }
}
