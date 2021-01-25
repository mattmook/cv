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

import android.os.Parcelable
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Skill
import kotlinx.parcelize.Parcelize

sealed class ProfileState : Parcelable {
    @Parcelize
    object Loading : ProfileState()

    @Parcelize
    object Error : ProfileState()

    // IDE bug highlights the types as not supported due to use of custom Parcelable class
    @Suppress("PARCELABLE_TYPE_NOT_SUPPORTED")
    @Parcelize
    data class Ready(
        val personalDetails: PersonalDetails,
        val skills: List<Skill>,
        val experiences: List<Experience>
    ) : ProfileState()
}
