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

package com.mattdolan.cv.profile.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattdolan.cv.common.SideEffect
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.Skill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ProfileState, SideEffect> {

    override val container: Container<ProfileState, SideEffect> = container(ProfileState.Loading, savedStateHandle) {
        if (it !is ProfileState.Ready) loadProfile()
    }

    fun loadProfile() = intent {
        try {
            reduce { ProfileState.Loading }

            val (personalDetails, skills, experiences) = coroutineScope {
                listOf(
                    async { profileRepository.personalDetails() },
                    async { profileRepository.skills() },
                    async { profileRepository.experiences() },
                ).awaitAll()
            }

            @Suppress("UNCHECKED_CAST")
            reduce { ProfileState.Ready(personalDetails as PersonalDetails, skills as List<Skill>, experiences as List<Experience>) }
        } catch (expected: Exception) {
            reduce { ProfileState.Error }
        }
    }

    fun selectRole(role: Role) = intent {
        (state as? ProfileState.Ready)?.experiences?.firstOrNull { it.roles.contains(role) }?.let { experience ->
            postSideEffect(SideEffect.NavigateToRoleDetails(experience, role))
        }
    }
}
