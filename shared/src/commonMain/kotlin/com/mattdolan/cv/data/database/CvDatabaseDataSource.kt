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

package com.mattdolan.cv.data.database

import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails
import com.mattdolan.cv.domain.Skill

internal interface CvDatabaseDataSource {
    fun clearDatabase()
    fun getExperiences(): List<Experience>?
    fun setExperiences(value: List<Experience>)
    fun getPersonalDetails(): PersonalDetails?
    fun setPersonalDetails(value: PersonalDetails)
    fun getSkills(): List<Skill>?
    fun setSkills(value: List<Skill>)
    fun getRoleDetails(role: Role): RoleDetails?
    fun setRoleDetails(role: Role, roleDetails: RoleDetails)
}
