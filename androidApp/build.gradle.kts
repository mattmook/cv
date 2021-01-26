import dagger.hilt.android.plugin.HiltExtension

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
    testImplementation("androidx.navigation:navigation-testing:${Versions.AndroidX.navigation}")
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

    // Networking
    implementation("io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")

    // Memory leak detection and fixes
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}")
    implementation("com.squareup.leakcanary:plumber-android:${Versions.leakCanary}")

    // Dependency Injection
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Versions.AndroidX.hilt}")
    kapt("androidx.hilt:hilt-compiler:${Versions.AndroidX.hilt}")
    implementation("com.google.dagger:hilt-android:${Versions.Google.dagger}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.Google.dagger}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.Google.dagger}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.Google.dagger}")
    testImplementation("com.google.dagger:hilt-android-testing:${Versions.Google.dagger}")
    kaptTest("com.google.dagger:hilt-android-compiler:${Versions.Google.dagger}")

    // Testing
    implementation("androidx.test.espresso:espresso-idling-resource:${Versions.AndroidX.espresso}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}")
    testImplementation("androidx.test.espresso:espresso-core:${Versions.AndroidX.espresso}")
    testImplementation("androidx.test:runner:${Versions.AndroidX.testRunner}")
    testImplementation("androidx.test:rules:${Versions.AndroidX.testRules}")
    testImplementation("androidx.test.ext:junit-ktx:${Versions.AndroidX.testExtJunit}")
    testImplementation("org.robolectric:robolectric:4.5")
    debugImplementation("androidx.fragment:fragment-testing:${Versions.AndroidX.fragmentTesting}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugar}")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.mattdolan.cv"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
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
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

configure<HiltExtension> {
    enableTransformForLocalTests = true
}
