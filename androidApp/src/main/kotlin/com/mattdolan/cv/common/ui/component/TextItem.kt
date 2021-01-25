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
import com.mattdolan.cv.androidApp.databinding.TextItemBinding
import com.xwray.groupie.viewbinding.BindableItem

data class TextItem(
    val text: CharSequence,
) : BindableItem<TextItemBinding>() {

    override fun initializeViewBinding(view: View) = TextItemBinding.bind(view)

    override fun getLayout() = R.layout.text_item

    override fun bind(viewBinding: TextItemBinding, position: Int) {
        viewBinding.text.text = text
    }
}
