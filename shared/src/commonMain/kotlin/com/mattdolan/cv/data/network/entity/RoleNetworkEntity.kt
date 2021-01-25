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

package com.mattdolan.cv.data.network.entity

import com.mattdolan.cv.domain.Role
import kotlinx.serialization.Serializable

@Serializable
internal data class RoleNetworkEntity(
    val title: String,
    val team: String? = null,
    val period: String,
    val detailUrl: String
) {
    fun toDomainEntity() = Role(
        title = title,
        team = team,
        period = period,
        detailUrl = detailUrl
    )
}
