# CV sample app

## Overview

A [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/) app with
list-detail views.

Currently only the Android app has been implementd.

### Setup instructions

Open the project in Android Studio 4.1.2, or execute `./gradlew installDebug`
from the command line.

Tests and static analysis can be run with `./gradlew check`.

## Approach

### Architecture

The project uses [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/)
for the `shared` repository layer of the app. This layer users [Ktor](https://ktor.io)
for networking and [SQLDelight](https://cashapp.github.io/sqldelight/) to cache
responses in a database.

The Android app is built using MVI with the [Orbit MVI](https://github.com/babylonhealth/orbit-mvi)
library that I co-authored.

Dependency injection is setup using [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

Views are implemented using RecyclerViews and [Groupie](https://github.com/lisawray/groupie)
to help build reusable shared components that can be configured to match a
design system.

Animations have been implemented through [AnimatedVectorDrawable](https://developer.android.com/guide/topics/graphics/drawable-animation#AnimVector)
created using [Shape Shifter](https://shapeshifter.design) and [MotionLayout](https://developer.android.com/training/constraint-layout/motionlayout).

### Testing

Logic in ViewModels is tested using the [Orbit Unit Testing module](https://github.com/orbit-mvi/orbit-mvi/tree/main/orbit-test)
which allows the interactions to be tested in isolation.

The network layer is tested with an integration test to verify the contract
with the backend.

## Future improvements

- Implement iOS application

- Investigate [MotionLayoutSavedStateViewModel](androidApp/src/main/kotlin/com/mattdolan/cv/common/ui/MotionLayoutSavedStateViewModel.kt)
  for memory leaks, potentially using lifecycle aware components for binding

- Additional testing
  - Implement Espresso tests with the repository layer mocked. This can be used
    to verify data is displayed and navigation functions as expected
  - Implement screenshot/interaction tests once Orbit MVI has support

- Setup CI
  - [GitHub Actions](https://github.com/features/actions)
  - Tags to upload APK to GitHub
  - [codecov.io](https://about.codecov.io)

- Add additional content
  - Education section
  - Medium blog posts
  - Conference talks
  - Open source software

- Migrate to [Jetpack Compose](https://developer.android.com/jetpack/compose)

- The app currently doesn't log any data or exceptions. This could be
  implemented using [Timber](https://github.com/JakeWharton/timber).
