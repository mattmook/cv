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
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import com.google.android.material.appbar.AppBarLayout
import com.mattdolan.cv.androidApp.R

/**
 * Ensures Toolbar is not elevated when a recyclerview or nestedscrollview is scrolled to the top
 */
@Suppress("unused")
class AppBarShadowScrollBehavior(context: Context, attrs: AttributeSet) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    private val shadowScrollBehavior = ShadowScrollBehavior(context)

    @SuppressLint("PrivateResource")
    private val maxElevation = context.resources.getDimensionPixelSize(R.dimen.design_appbar_elevation).toFloat()

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (dependency is AppBarLayout && child is ScrollingView) {
            shadowScrollBehavior.onDependentViewChanged(child, dependency)
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }
}
