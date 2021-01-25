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
import com.mattdolan.cv.androidApp.databinding.RoleItemBinding
import com.mattdolan.cv.domain.Role
import com.xwray.groupie.viewbinding.BindableItem

data class RoleItem(
    val role: Role,
    val topLine: Boolean,
    val bottomLine: Boolean,
    val clickListener: () -> Unit = emptyListener
) : BindableItem<RoleItemBinding>() {

    override fun initializeViewBinding(view: View) = RoleItemBinding.bind(view)

    override fun getLayout() = R.layout.role_item

    override fun bind(viewBinding: RoleItemBinding, position: Int) {
        viewBinding.role.text = role.title
        viewBinding.team.apply {
            text = role.team
            visibility = if (role.team.isNullOrBlank()) View.GONE else View.VISIBLE
        }
        viewBinding.range.text = role.period

        viewBinding.topLine.visibility = if (topLine) View.VISIBLE else View.GONE
        viewBinding.bottomLine.visibility = if (bottomLine) View.VISIBLE else View.GONE

        viewBinding.item.setOnClickListener {
            clickListener()
        }
        if (clickListener == emptyListener) {
            viewBinding.item.isClickable = false
        }
    }

    companion object {
        private val emptyListener: () -> Unit = {}
    }
}
