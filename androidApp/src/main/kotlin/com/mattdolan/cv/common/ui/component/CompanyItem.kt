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
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.androidApp.databinding.CompanyItemBinding
import com.mattdolan.cv.domain.Experience
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class CompanyItem(val context: Context, val experience: Experience, val topLine: Boolean, val bottomLine: Boolean) :
    BindableItem<CompanyItemBinding>() {

    private val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    override fun initializeViewBinding(view: View) = CompanyItemBinding.bind(view)

    override fun getLayout() = R.layout.company_item

    override fun bind(viewBinding: CompanyItemBinding, position: Int) {
        viewBinding.company.text = experience.company
        viewBinding.industry.text = context.getString(R.string.industry, experience.industry, experience.location)
        viewBinding.range.text = experience.period

        viewBinding.topLine.visibility = if (topLine) View.VISIBLE else View.GONE
        viewBinding.bottomLine.visibility = if (bottomLine) View.VISIBLE else View.GONE

        GlobalScope.launch {
            imageLoader.execute(
                ImageRequest.Builder(context).data(experience.logoUrl).placeholder(R.drawable.ic_company_placeholder).target(viewBinding.icon)
                    .build()
            )
        }
    }
}
