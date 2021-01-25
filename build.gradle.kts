import io.gitlab.arturbosch.detekt.Detekt

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

plugins {
    id("io.gitlab.arturbosch.detekt") version Versions.detektGradlePlugin
    id("com.appmattus.markdown") version Versions.markdownlintGradlePlugin
}

buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.android.tools.build:gradle:${Versions.androidGradlePlugin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.Google.dagger}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.AndroidX.navigation}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}")
    }
}

group = "com.mattdolan.cv"
version = "1.0-SNAPSHOT"

apply(from = "$rootDir/gradle/scripts/dependencyUpdates.gradle.kts")

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detektGradlePlugin}")
}

tasks.withType<Detekt> {
    jvmTarget = "1.8"
}

detekt {
    input = files(subprojects.map { File(it.projectDir, "src") })

    buildUponDefaultConfig = true

    autoCorrect = true

    config = files("detekt-config.yml")
}

tasks.maybeCreate("check").dependsOn(tasks.named("detekt"))

tasks.maybeCreate("check").dependsOn(tasks.named("markdownlint"))
