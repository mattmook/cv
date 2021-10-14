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

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization") version Versions.kotlin
}

apply(from = "$rootDir/gradle/scripts/jacoco-android.gradle.kts")

group = "com.mattdolan.cv"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:runtime:${Versions.sqlDelight}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlinx.serialization}")
                implementation("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.ktor:ktor-client-serialization:${Versions.ktor}")
                implementation("com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlinx.dateTime}")
                implementation("co.touchlab:stately-iso-collections:1.1.10-a1")
                implementation("co.touchlab:stately-isolate:1.1.10-a1")
                implementation("co.touchlab:stately-concurrency:1.1.10")
            }
        }
        val commonTest by getting {
            kotlin.srcDirs("$rootDir/test-common/src/main/kotlin")

            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-assertions-core:${Versions.kotest}")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:${Versions.sqlDelight}")
                implementation("io.ktor:ktor-client-android:${Versions.ktor}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:${Versions.junit4}")
                implementation("com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:${Versions.sqlDelight}")
                implementation("io.ktor:ktor-client-ios:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core") {
                    version {
                        strictly(Versions.coroutinesNative)
                    }
                }
            }
        }
        val iosTest by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:${Versions.sqlDelight}")
                implementation("co.touchlab:sqliter:${Versions.sqliter}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core") {
                    version {
                        strictly(Versions.coroutinesNative)
                    }
                }
            }
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.mattdolan.cv.data.database"
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 30
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString() }

val xcFrameworkPath = "$buildDir/xcode-frameworks/${project.name}.xcframework"

tasks.create<Delete>("deleteXcFramework") { delete = setOf(xcFrameworkPath) }

val buildXcFramework by tasks.registering {
    dependsOn("deleteXcFramework")
    group = "build"
    val mode = "Release"
    val frameworks = arrayOf("iosArm64", "iosX64")
        .map { kotlin.targets.getByName<KotlinNativeTarget>(it).binaries.getFramework(mode) }
    inputs.property("mode", mode)
    dependsOn(frameworks.map { it.linkTask })
    doLast { buildXcFramework(frameworks) }
}

fun Task.buildXcFramework(frameworks: List<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>) {
    val buildArgs: () -> List<String> = {
        val arguments = mutableListOf("-create-xcframework")
        frameworks.forEach {
            arguments += "-framework"
            arguments += "${it.outputDirectory}/${project.name}.framework"
        }
        arguments += "-output"
        arguments += xcFrameworkPath
        arguments
    }
    exec {
        executable = "xcodebuild"
        args = buildArgs()
    }
}

tasks.getByName("build").dependsOn(buildXcFramework)
