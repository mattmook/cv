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
import com.mattdolan.cv.data.MockProfileRepository
import com.mattdolan.cv.experience.RoleDetailArguments
import org.junit.Test
import org.orbitmvi.orbit.assert
import org.orbitmvi.orbit.test

class RoleDetailViewModelTest {
    private val savedStateHandle = SavedStateHandle()
    private val mockProfileRepository = MockProfileRepository()

    private val experience = mockProfileRepository._experiences[0]
    private val role = experience.roles[0]

    private val roleDetailViewModel = RoleDetailViewModel(mockProfileRepository, savedStateHandle).apply {
        setup(RoleDetailArguments(experience, role))
    }

    @Test
    fun `onCreate does nothing when header present and details ready`() {
        // Given an initial state with header and ready details
        val initialState = RoleDetailState(
            header = RoleDetailState.Header(experience.logoUrl, role.title, role.team, role.period),
            details = RoleDetailState.Details.Ready(mockProfileRepository._roleDetails.description)
        )

        // When onCreate called
        roleDetailViewModel.test(initialState, isolateFlow = false, runOnCreate = true)

        // Then we output no state changes
        roleDetailViewModel.assert(initialState)
    }

    @Test
    fun `onCreate loads header when not present`() {
        // Given an initial state with no header
        val initialState = RoleDetailState(
            header = null,
            details = RoleDetailState.Details.Ready(mockProfileRepository._roleDetails.description)
        )

        // When onCreate called
        roleDetailViewModel.test(initialState, isolateFlow = false, runOnCreate = true)

        // Then we output loading then ready
        roleDetailViewModel.assert(initialState) {
            states(
                { copy(header = RoleDetailState.Header(experience.logoUrl, role.title, role.team, role.period)) },
            )
        }
    }

    @Test
    fun `onCreate loads role details when initialState not ready`() {
        // Given an initial state with details not ready
        val initialState = RoleDetailState(
            header = RoleDetailState.Header(experience.logoUrl, role.title, role.team, role.period),
            details = RoleDetailState.Details.Error
        )

        // When onCreate called
        roleDetailViewModel.test(initialState, isolateFlow = false, runOnCreate = true)

        // Then we output loading then ready
        roleDetailViewModel.assert(initialState) {
            states(
                { copy(details = RoleDetailState.Details.Loading) },
                { copy(details = RoleDetailState.Details.Ready(mockProfileRepository._roleDetails.description)) }
            )
        }
    }

    @Test
    fun `onCreate outputs Loading then Error when roleDetails fails`() {
        // Given role details fails to load
        mockProfileRepository.mode = MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.RoleDetails)

        // And an initial state with details not ready
        val initialState = RoleDetailState(
            header = RoleDetailState.Header(experience.logoUrl, role.title, role.team, role.period),
            details = RoleDetailState.Details.Error
        )

        // When onCreate called
        roleDetailViewModel.test(initialState, isolateFlow = false, runOnCreate = true)

        // Then we output loading then error
        roleDetailViewModel.assert(initialState) {
            states(
                { copy(details = RoleDetailState.Details.Loading) },
                { copy(details = RoleDetailState.Details.Error) }
            )
        }
    }

    @Test
    fun `loadDetails outputs Loading then Ready when roleDetails succeeds`() {
        // Given an initial state with details not ready
        val initialState = RoleDetailState(
            details = RoleDetailState.Details.Error
        )

        // When loadDetails called
        roleDetailViewModel.test(initialState)
        roleDetailViewModel.loadDetails()

        // Then we output loading then ready
        roleDetailViewModel.assert(initialState) {
            states(
                { copy(details = RoleDetailState.Details.Loading) },
                { copy(details = RoleDetailState.Details.Ready(mockProfileRepository._roleDetails.description)) }
            )
        }
    }

    @Test
    fun `loadDetails outputs Loading then Error when roleDetails fails`() {
        // Given role details fails to load
        mockProfileRepository.mode = MockProfileRepository.Mode.ThrowException(MockProfileRepository.DataType.RoleDetails)

        // And an initial state with details not ready
        val initialState = RoleDetailState(
            details = RoleDetailState.Details.Error
        )

        // When loadDetails called
        roleDetailViewModel.test(initialState)
        roleDetailViewModel.loadDetails()

        // Then we output loading then error
        roleDetailViewModel.assert(initialState) {
            states(
                { copy(details = RoleDetailState.Details.Loading) },
                { copy(details = RoleDetailState.Details.Error) }
            )
        }
    }
}
