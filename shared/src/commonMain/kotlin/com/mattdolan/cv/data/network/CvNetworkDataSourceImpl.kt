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

package com.mattdolan.cv.data.network

import com.mattdolan.cv.data.network.entity.ExperienceNetworkEntity
import com.mattdolan.cv.data.network.entity.PersonalDetailsNetworkEntity
import com.mattdolan.cv.data.network.entity.RoleDetailsNetworkEntity
import com.mattdolan.cv.data.network.entity.SkillNetworkEntity
import com.mattdolan.cv.domain.Role
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType

internal class CvNetworkDataSourceImpl(
    private val client: HttpClient = httpClientFactory()
) : CvNetworkDataSource {

    override suspend fun getExperiences() =
        client.getJson<List<ExperienceNetworkEntity>>("experiences.json").map(ExperienceNetworkEntity::toDomainEntity)

    override suspend fun getSkills() =
        client.getJson<List<SkillNetworkEntity>>("skills.json").map(SkillNetworkEntity::toDomainEntity)

    override suspend fun getPersonalDetails() =
        client.getJson<PersonalDetailsNetworkEntity>("personal-details.json").toDomainEntity()

    override suspend fun getRoleDetails(role: Role) =
        RoleDetailsNetworkEntity(client.getJson(role.detailUrl)).toDomainEntity()

    private suspend inline fun <reified T> HttpClient.getJson(urlString: String) = get<T>(Url(urlString)) {
        contentType(ContentType.Application.Json)
    }
}
