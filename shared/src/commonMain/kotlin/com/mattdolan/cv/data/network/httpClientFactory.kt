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

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.URLProtocol

internal fun httpClientFactory() = HttpClient {
    defaultRequest {
        if (url.host == "localhost") {
            url.host = "mattdolan.com"
            url.protocol = URLProtocol.HTTPS
            url.encodedPath = "/cv/api/" + url.encodedPath
        }
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}
