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

import android.content.Context
import android.view.View
import coil.ImageLoader
import coil.request.ImageRequest
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.androidApp.databinding.TwoLineWithIconAndMetaTextItemBinding
import com.mattdolan.cv.common.ui.component.input.IconValue
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class TwoLineWithIconAndMetaTextItem(
    val context: Context,
    val imageLoader: ImageLoader,
    val supportingVisual: IconValue,
    val primaryText: String,
    val secondaryText: String,
    val metadata: String,
    val topLine: Boolean,
    val bottomLine: Boolean
) : BindableItem<TwoLineWithIconAndMetaTextItemBinding>() {

    override fun initializeViewBinding(view: View) = TwoLineWithIconAndMetaTextItemBinding.bind(view)

    override fun getLayout() = R.layout.two_line_with_icon_and_meta_text_item

    override fun bind(viewBinding: TwoLineWithIconAndMetaTextItemBinding, position: Int) {
        viewBinding.primaryText.text = primaryText
        viewBinding.secondaryText.text = secondaryText
        viewBinding.metadata.text = metadata

        viewBinding.topLine.visibility = if (topLine) View.VISIBLE else View.GONE
        viewBinding.bottomLine.visibility = if (bottomLine) View.VISIBLE else View.GONE

        GlobalScope.launch {
            imageLoader.execute(
                ImageRequest.Builder(context).data(supportingVisual.imageUrl).placeholder(supportingVisual.placeholder).target(viewBinding.icon)
                    .build()
            )
        }
    }
}
