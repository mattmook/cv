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

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.mattdolan.cv.androidApp.R

/**
 * Ensures Toolbar is not elevated when a recyclerview or nestedscrollview is scrolled to the top
 */
class ShadowScrollBehavior(context: Context) {

    @SuppressLint("PrivateResource")
    private val maxElevation = context.resources.getDimensionPixelSize(R.dimen.design_appbar_elevation).toFloat()

    fun onDependentViewChanged(scrollingView: ScrollingView, vararg dependency: View) {
        when (scrollingView) {
            is NestedScrollView -> {
                setElevation(scrollingView, dependency)
                addScrollListener(scrollingView, dependency)
            }
            is RecyclerView -> {
                setElevation(scrollingView, dependency)
                addScrollListener(scrollingView, dependency)
            }
        }
    }

    private fun addScrollListener(child: NestedScrollView, dependency: Array<out View>) {
        child.setOnScrollChangeListener { _: NestedScrollView?, _: Int, _: Int, _: Int, _: Int ->
            setElevation(child, dependency)
        }
    }

    private fun addScrollListener(child: RecyclerView, dependency: Array<out View>) {
        child.clearOnScrollListeners()
        child.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    setElevation(recyclerView, dependency)
                }
            }
        )
    }

    private fun setElevation(scrollingView: View, dependency: Array<out View>) {
        val elevation = if (scrollingView.canScrollVertically(SCROLL_DIRECTION_UP)) maxElevation else 0f
        dependency.forEach {
            ViewCompat.setElevation(it, elevation)
        }
    }

    companion object {
        private const val SCROLL_DIRECTION_UP = -1
    }
}
