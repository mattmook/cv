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

apply<JacocoPlugin>()

val jacocoTask = tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("testDebugUnitTest"))

    reports {
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = false
    }

    val fileFilter = setOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*$[0-9].*",
        "**/dagger/**",
        "**/databinding/**"
    )

    val mainSrc = listOf(
        "${project.projectDir}/src/commonMain/kotlin",
        "${project.projectDir}/src/androidMain/kotlin",
        "${project.projectDir}/src/main/kotlin"
    )

    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(listOf(debugTree)))
    executionData.setFrom(
        fileTree(project.buildDir) {
            include(listOf("jacoco/testDebugUnitTest.exec"))
        }
    )
}

tasks.named("check") {
    finalizedBy(jacocoTask)
}
