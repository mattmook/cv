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

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    context: Context,
    @DimenRes
    private val space: Int
) : RecyclerView.ItemDecoration() {

    private val spacePx = context.resources.getDimensionPixelSize(space)
    private val halfSpacePx = spacePx / 2

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val size = parent.adapter?.itemCount

        val isFirstItem = position == 0
        val isLastItem = position + 1 == size

        if (position == -1) return

        outRect.left = spacePx
        outRect.right = spacePx
        outRect.top = if (isFirstItem) spacePx else halfSpacePx
        outRect.bottom = if (isLastItem) spacePx else halfSpacePx
    }
}
