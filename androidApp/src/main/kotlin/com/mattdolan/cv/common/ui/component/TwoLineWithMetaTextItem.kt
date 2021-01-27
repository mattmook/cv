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

package com.mattdolan.cv.common.ui.component

import android.view.View
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.androidApp.databinding.TwoLineWithMetaTextItemBinding
import com.xwray.groupie.viewbinding.BindableItem

data class TwoLineWithMetaTextItem(
    val primaryText: String,
    val secondaryText: String?,
    val metadata: String,
    val topLine: Boolean,
    val bottomLine: Boolean,
    val clickListener: () -> Unit = emptyListener
) : BindableItem<TwoLineWithMetaTextItemBinding>() {

    override fun initializeViewBinding(view: View) = TwoLineWithMetaTextItemBinding.bind(view)

    override fun getLayout() = R.layout.two_line_with_meta_text_item

    override fun bind(viewBinding: TwoLineWithMetaTextItemBinding, position: Int) {
        viewBinding.primaryText.text = primaryText
        viewBinding.secondaryText.apply {
            text = secondaryText
            visibility = if (secondaryText.isNullOrBlank()) View.GONE else View.VISIBLE
        }
        viewBinding.metadata.text = metadata
        viewBinding.topLine.visibility = if (topLine) View.VISIBLE else View.GONE
        viewBinding.bottomLine.visibility = if (bottomLine) View.VISIBLE else View.GONE

        viewBinding.container.setOnClickListener {
            clickListener()
        }
        if (clickListener == emptyListener) {
            viewBinding.container.isClickable = false
        }
    }

    companion object {
        private val emptyListener: () -> Unit = {}
    }
}
