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

package com.mattdolan.cv.experience

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.androidApp.databinding.RoleDetailFragmentBinding
import com.mattdolan.cv.common.ui.ShadowScrollBehavior
import com.mattdolan.cv.common.ui.SpaceItemDecoration
import com.mattdolan.cv.common.ui.animate
import com.mattdolan.cv.common.ui.component.SingleLineTextItem
import com.mattdolan.cv.common.ui.debounce
import com.mattdolan.cv.common.viewBinding
import com.mattdolan.cv.common.viewmodel.viewModelsWithArgs
import com.mattdolan.cv.experience.model.RoleDetailState
import com.mattdolan.cv.experience.model.RoleDetailViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RoleDetailFragment : Fragment(R.layout.role_detail_fragment) {

    private val viewModel by viewModelsWithArgs<RoleDetailViewModel, RoleDetailFragmentArgs, RoleDetailArguments> {
        it.role
    }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val binding by viewBinding<RoleDetailFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

        binding.loading.drawable.animate(lifecycleScope = lifecycleScope, repeat = true, repeatDelayMillis = 300)

        binding.errorButton.setOnClickListener {
            viewModel.loadDetails()
        }

        binding.content.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.separator_thickness))
        }

        ShadowScrollBehavior(requireContext()).onDependentViewChanged(scrollingView = binding.content, binding.toolbar)

        lifecycleScope.launchWhenCreated {
            viewModel.container.stateFlow.collect(::render)
        }
    }

    private fun configureToolbar() {
        val listener = debounce(scope = lifecycleScope) { findNavController().popBackStack() }
        binding.back.setOnClickListener {
            listener()
        }
    }

    private fun render(state: RoleDetailState) {
        state.header?.let { header ->
            binding.role.text = header.title

            val team = header.team.takeIf { !it.isNullOrBlank() }
            binding.team.isVisible = team != null
            team?.let {
                binding.team.text = header.team
            }

            binding.range.text = header.period
        }

        if (state.details is RoleDetailState.Details.Ready) {
            val bulletSpacing = requireContext().resources.getDimensionPixelSize(R.dimen.bullet_spacing)

            state.details.description.map {
                SingleLineTextItem(SpannableStringBuilder().append(it, BulletSpan(bulletSpacing), 0))
            }.let(groupAdapter::update)
        }

        binding.content.isVisible = state.details is RoleDetailState.Details.Ready
        binding.loading.isVisible = state.details is RoleDetailState.Details.Loading
        binding.errorGroup.isVisible = state.details is RoleDetailState.Details.Error
    }
}
