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

import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import java.lang.ref.WeakReference

/**
 * A ViewModel to save the state of a MotionLayout - this is necessary as using NavController means Fragments get recreated on navigation
 * See https://stackoverflow.com/a/60877714/14508577
 */
class MotionLayoutSavedStateViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var motionLayout: WeakReference<MotionLayout>? = null

    private val idlingResource = CountingIdlingResource("motionLayout")

    private val listener = object : MotionLayout.TransitionListener {
        override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
            savedStateHandle.set("motion", motionLayout.transitionState)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
            savedStateHandle.set("motion", motionLayout.transitionState)
            idlingResource.decrement()
        }

        override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            savedStateHandle.set("motion", motionLayout.transitionState)
            idlingResource.increment()
        }

        override fun onTransitionTrigger(motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) = Unit
    }

    fun bind(motionLayout: MotionLayout) {
        this.motionLayout = WeakReference(motionLayout)

        savedStateHandle.get<Bundle>("motion")?.let {
            // Cannot just restore transitionState as the transition will automatically play
            //     motionLayout.transitionState = it
            // See https://stackoverflow.com/questions/65048418/how-to-restore-transition-state-of-motionlayout-without-auto-playing-the-transit
            motionLayout.setTransition(it.getInt("motion.StartState"), it.getInt("motion.EndState"))
            motionLayout.progress = it.getFloat("motion.progress")
        }

        motionLayout.addTransitionListener(listener)

        IdlingRegistry.getInstance().register(idlingResource)
    }

    override fun onCleared() {
        super.onCleared()

        IdlingRegistry.getInstance().unregister(idlingResource)
        motionLayout?.get()?.removeTransitionListener(listener)
        motionLayout?.clear()
        motionLayout = null
    }
}
