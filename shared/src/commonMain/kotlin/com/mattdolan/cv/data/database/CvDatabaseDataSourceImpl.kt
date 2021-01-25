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

import com.mattdolan.cv.data.database.mapper.toDomainEntity
import com.mattdolan.cv.data.database.mapper.toPersonalDetailsDatabaseEntity
import com.mattdolan.cv.data.database.mapper.toRoleDatabaseEntity
import com.mattdolan.cv.data.database.mapper.toSkillDatabaseEntity
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.PersonalDetails
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.domain.RoleDetails

internal class CvDatabaseDataSourceImpl(
    private val database: AppDatabase
) : CvDatabaseDataSource {

    override fun clearDatabase() = database.transaction {
        database.personalDetailsQueries.deletePersonalDetails()
        database.skillQueries.deleteSkills()
        database.experienceQueries.deleteExperiences()
        database.roleQueries.deleteRoles()
        database.roleDetailsQueries.deleteAllRoleDetails()
    }

    override fun getExperiences(): List<Experience>? = database.transactionWithResult<List<Experience>> {
        database.experienceQueries.experiences().executeAsList().map {
            val roles = database.roleQueries.rolesForExperience(it.experienceId).executeAsList()
            it.toDomainEntity(roles)
        }
    }.takeIf { experiences ->
        experiences.isNotEmpty() && experiences.all { it.roles.isNotEmpty() }
    }

    override fun setExperiences(value: List<Experience>) = database.transaction {
        database.experienceQueries.deleteExperiences()
        database.roleQueries.deleteRoles()
        value.forEach { experience ->
            database.experienceQueries.insertExperience(
                experience.company,
                experience.logoUrl,
                experience.industry,
                experience.location,
                experience.period
            )
            val experienceId = database.experienceQueries.lastInsertRowId().executeAsOne()
            experience.roles.forEach {
                val role = it.toRoleDatabaseEntity(experienceId)
                database.roleQueries.insertRole(
                    experienceId = role.experienceId,
                    title = role.title,
                    team = role.team,
                    period = role.period,
                    detailUrl = role.detailUrl
                )
            }
        }
    }

    override fun getPersonalDetails() = database.personalDetailsQueries.personalDetails().executeAsOneOrNull()?.toDomainEntity()

    override fun setPersonalDetails(value: PersonalDetails) {
        val entity = value.toPersonalDetailsDatabaseEntity()
        database.personalDetailsQueries.transaction {
            database.personalDetailsQueries.deletePersonalDetails()
            database.personalDetailsQueries.insertPersonalDetails(entity.name, entity.tagline, entity.location, entity.avatarUrl)
        }
    }

    override fun getSkills() = database.skillQueries.skills().executeAsList().takeIf { it.isNotEmpty() }?.map(Skill::toDomainEntity)

    override fun setSkills(value: List<com.mattdolan.cv.domain.Skill>) = database.skillQueries.transaction {
        val entity = value.map(com.mattdolan.cv.domain.Skill::toSkillDatabaseEntity)
        database.skillQueries.deleteSkills()
        entity.forEach {
            database.skillQueries.insertSkill(it.skill, it.since)
        }
    }

    override fun getRoleDetails(role: Role) =
        database.roleDetailsQueries.detailsForRole(role.detailUrl).executeAsList().takeIf { it.isNotEmpty() }?.let {
            RoleDetails(description = it.map(com.mattdolan.cv.data.database.RoleDetails::text))
        }

    override fun setRoleDetails(role: Role, roleDetails: RoleDetails) = database.roleDetailsQueries.transaction {
        database.roleDetailsQueries.deleteRoleDetails(role.detailUrl)
        roleDetails.description.forEach {
            database.roleDetailsQueries.insertRoleDetails(role.detailUrl, it)
        }
    }
}
