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

package com.mattdolan.cv.experience.model

import androidx.lifecycle.SavedStateHandle
import com.mattdolan.cv.common.SideEffect
import com.mattdolan.cv.common.viewmodel.ViewModelSetup
import com.mattdolan.cv.domain.ProfileRepository
import com.mattdolan.cv.experience.RoleDetailArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RoleDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelSetup<RoleDetailArguments>(), ContainerHost<RoleDetailState, SideEffect> {

    private lateinit var arguments: RoleDetailArguments

    override fun setup(data: RoleDetailArguments) {
        arguments = data
    }

    override val container = container<RoleDetailState, SideEffect>(RoleDetailState(), savedStateHandle) {
        it.header ?: loadHeader()
        if (it.details !is RoleDetailState.Details.Ready) loadDetails()
    }

    private fun loadHeader() = intent {
        reduce {
            state.copy(
                header = RoleDetailState.Header(
                    logoUrl = arguments.experience.logoUrl,
                    title = arguments.role.title,
                    team = arguments.role.team,
                    period = arguments.role.period,
                )
            )
        }
    }

    fun loadDetails() = intent {
        try {
            reduce { state.copy(details = RoleDetailState.Details.Loading) }

            val roleDetails = profileRepository.roleDetails(arguments.role)

            reduce { state.copy(details = RoleDetailState.Details.Ready(roleDetails.description)) }
        } catch (expected: Exception) {
            reduce { state.copy(details = RoleDetailState.Details.Error) }
        }
    }
}
