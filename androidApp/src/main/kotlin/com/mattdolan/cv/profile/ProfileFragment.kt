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

package com.mattdolan.cv.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.chip.Chip
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.androidApp.databinding.ProfileFragmentBinding
import com.mattdolan.cv.common.SideEffect
import com.mattdolan.cv.common.ui.MotionLayoutSavedStateViewModel
import com.mattdolan.cv.common.ui.ShadowScrollBehavior
import com.mattdolan.cv.common.ui.animate
import com.mattdolan.cv.common.ui.component.TwoLineWithIconAndMetaTextItem
import com.mattdolan.cv.common.ui.component.TwoLineWithMetaTextItem
import com.mattdolan.cv.common.ui.component.input.IconValue
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Skill
import com.mattdolan.cv.experience.RoleDetailArguments
import com.mattdolan.cv.profile.model.ProfileState
import com.mattdolan.cv.profile.model.ProfileViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val profileViewModel by viewModels<ProfileViewModel>()

    private val motionLayoutSavedStateViewModel by viewModels<MotionLayoutSavedStateViewModel>()

    private val profileSection = Section()
    private val experiencesSection = Section()

    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ProfileFragmentBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        motionLayoutSavedStateViewModel.bind(binding.mainView)

        binding.errorButton.setOnClickListener {
            profileViewModel.loadProfile()
        }

        binding.loading.drawable.animate(lifecycleScope = lifecycleScope, repeat = true, repeatDelayMillis = 300)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(profileSection)
                add(experiencesSection)
            }
        }

        ShadowScrollBehavior(requireContext()).onDependentViewChanged(
            scrollingView = binding.recyclerView,
            binding.headerBackground, binding.image, binding.name, binding.tagline, binding.location, binding.chipGroup, binding.pad
        )

        lifecycleScope.launch {
            profileViewModel.container.stateFlow.collect(::render)
        }

        lifecycleScope.launch {
            profileViewModel.container.sideEffectFlow.collect(::sideEffect)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fix memory leak with RecyclerView
        binding.recyclerView.adapter = null
    }

    private fun render(state: ProfileState) {
        when (state) {
            ProfileState.Loading -> {
                if (binding.mainView.currentState != R.id.loading) {
                    binding.mainView.setTransition(R.id.loadingToError)
                    binding.mainView.transitionToStart()
                }
            }
            ProfileState.Error -> {
                if (binding.mainView.currentState != R.id.error) {
                    binding.mainView.setTransition(R.id.loadingToError)
                    binding.mainView.transitionToEnd()
                }
            }
            is ProfileState.Ready -> {
                if (binding.mainView.currentState != R.id.ready && binding.mainView.currentState != R.id.readyCollapsed) {
                    binding.mainView.setTransition(R.id.loadingToReady)
                    binding.mainView.transitionToEnd()
                }

                renderPersonalDetails(state.personalDetails, state.skills)
                renderExperiences(state.experiences)
            }
        }
    }

    private fun renderPersonalDetails(personalDetails: PersonalDetails, skills: List<Skill>) {
        val year = Year.now().value
        val chips = skills.map { skill ->
            skill.since?.let { it -> "${skill.skill} (${year - it} yrs)" } ?: skill.skill
        }

        lifecycleScope.launch {
            imageLoader.execute(
                ImageRequest.Builder(requireContext())
                    .data(personalDetails.avatarUrl)
                    .transformations(CircleCropTransformation())
                    .target(binding.image)
                    .build()
            )
        }

        binding.name.text = personalDetails.name
        binding.tagline.text = personalDetails.tagline
        binding.location.text = personalDetails.location

        binding.chipGroup.apply {
            removeAllViews()

            chips.forEach {
                addView(Chip(context).apply {
                    text = it
                })
            }
        }
    }

    private fun renderExperiences(experiences: List<Experience>) {
        experiencesSection.update(experiences.flatMapIndexed { companyIndex, experience ->
            listOf(
                TwoLineWithIconAndMetaTextItem(
                    coroutineScope = lifecycleScope,
                    context = requireContext(),
                    imageLoader = imageLoader,
                    supportingVisual = IconValue(experience.logoUrl, R.drawable.ic_company_placeholder),
                    primaryText = experience.company,
                    secondaryText = requireContext().getString(R.string.industry, experience.industry, experience.location),
                    metadata = experience.period,
                    topLine = companyIndex != 0,
                    bottomLine = true
                )
            ) + experience.roles.mapIndexed { roleIndex, role ->
                TwoLineWithMetaTextItem(
                    primaryText = role.title,
                    secondaryText = role.team,
                    metadata = role.period,
                    topLine = true,
                    bottomLine = companyIndex != experiences.size - 1 || roleIndex != experience.roles.size - 1
                ) {
                    profileViewModel.selectRole(role)
                }
            }
        })
    }

    private fun sideEffect(sideEffect: SideEffect) {
        when (sideEffect) {
            is SideEffect.NavigateToRoleDetails -> {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToRoleDetailFragment(
                        RoleDetailArguments(sideEffect.experience, sideEffect.role)
                    )
                )
            }
        }
    }
}
