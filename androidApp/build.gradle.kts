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
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

apply(from = "$rootDir/gradle/scripts/jacoco-android.gradle.kts")
apply(plugin = "dagger.hilt.android.plugin")

group = "com.mattdolan.cv"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")

    // Architecture
    implementation("androidx.fragment:fragment-ktx:${Versions.AndroidX.fragment}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecycle}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigation}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.AndroidX.navigation}")
    implementation("org.orbit-mvi:orbit-viewmodel:${Versions.orbitMvi}")
    testImplementation("org.orbit-mvi:orbit-test:${Versions.orbitMvi}")

    // UI
    implementation("com.google.android.material:material:${Versions.Google.material}")
    implementation("androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}")
    implementation("androidx.vectordrawable:vectordrawable:${Versions.AndroidX.vectorDrawable}")
    implementation("com.xwray:groupie:${Versions.groupie}")
    implementation("com.xwray:groupie-viewbinding:${Versions.groupie}")
    implementation("io.coil-kt:coil-svg:${Versions.coil}")
    implementation("io.coil-kt:coil-compose:${Versions.coil}")

    implementation("com.google.accompanist:accompanist-flowlayout:${Versions.accompanist}")

    implementation("androidx.compose.ui:ui:${Versions.AndroidX.compose}")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:${Versions.AndroidX.compose}")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:${Versions.AndroidX.compose}")
    // Material Design
    implementation("androidx.compose.material:material:${Versions.AndroidX.compose}")
    // Integration with activities
    implementation("androidx.activity:activity-compose:${Versions.AndroidX.activityCompose}")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.AndroidX.lifecycleViewmodelCompose}")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.AndroidX.compose}")
    // Navigation
    implementation("androidx.navigation:navigation-compose:${Versions.AndroidX.navigationCompose}")
    // Animated Vector Drawables
    implementation("androidx.compose.animation:animation-graphics:${Versions.AndroidX.animationGraphics}")
    // Constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.AndroidX.constraintLayoutCompose}")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:${Versions.AndroidX.splashscreen}")

    // Networking
    implementation("io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")

    // Memory leak detection and fixes
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}")
    implementation("com.squareup.leakcanary:plumber-android:${Versions.leakCanary}")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:${Versions.Google.dagger}")
    kapt("com.google.dagger:hilt-compiler:${Versions.Google.dagger}")
    kaptAndroidTest("com.google.dagger:hilt-compiler:${Versions.Google.dagger}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.Google.dagger}")

    // Testing
    implementation("androidx.test.espresso:espresso-idling-resource:${Versions.AndroidX.espresso}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}")
    testImplementation("junit:junit:${Versions.junit4}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.AndroidX.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.AndroidX.espresso}")
    androidTestImplementation("androidx.test:runner:${Versions.AndroidX.testRunner}")
    androidTestImplementation("androidx.test:rules:${Versions.AndroidX.testRules}")
    androidTestImplementation("androidx.test.ext:junit-ktx:${Versions.AndroidX.testExtJunit}")
    androidTestImplementation("androidx.fragment:fragment-testing:${Versions.AndroidX.fragmentTesting}")
    androidTestImplementation("com.kaspersky.android-components:kaspresso:${Versions.kaspresso}")
    testImplementation("io.kotest:kotest-assertions-core:${Versions.kotest}")
    androidTestImplementation("io.kotest:kotest-assertions-core:${Versions.kotest}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugar}")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.mattdolan.cv"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "com.mattdolan.cv.test.HiltTestRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-alpha05"
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
        if (name.startsWith("test") || name.startsWith("androidTest")) {
            java.srcDirs("$rootDir/test-common/src/main/kotlin")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
