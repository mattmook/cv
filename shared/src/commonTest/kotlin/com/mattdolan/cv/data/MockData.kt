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

package com.mattdolan.cv.data

import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill
import kotlin.random.Random

fun Random.Default.nextPersonalDetails() = PersonalDetails(
    name = nextString(),
    tagline = nextString(),
    location = nextString(),
    avatarUrl = nextString()
)

fun Random.Default.nextSkill() = Skill(
    skill = nextString(),
    since = if (nextBoolean()) nextInt(1970, 2100) else null
)

fun Random.Default.nextRole() = Role(
    title = nextString(),
    team = if (nextBoolean()) nextString() else null,
    period = nextString(),
    detailUrl = nextString()
)

fun Random.Default.nextExperience() = Experience(
    company = nextString(),
    logoUrl = nextString(),
    industry = nextString(),
    location = nextString(),
    period = nextString(),
    roles = nextList { nextRole() }
)

fun Random.Default.nextRoleDetails() = RoleDetails(
    description = nextList { nextString() }
)

fun <T> Random.Default.nextList(generator: () -> T) = (0..nextInt(0, 10)).map { generator() }

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
private fun Random.Default.nextString(length: Int = 20) = (1..length).map { charPool[nextInt(0, charPool.size)] }.joinToString("")
