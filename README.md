# CV sample app

## Overview

The project demonstrates a [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/)
app with list-detail views.

Only the Android app currently functions. I will implement the iOS app later.

### Setup instructions

Open the project in Android Studio 4.1.2, or execute `./gradlew installDebug`
from the command line.

Run tests and static analysis with `./gradlew check`. Run espresso tests with
`./gradlew connectedCheck`.

## Approach

### Architecture

The project uses [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/)
for the `shared` repository layer of the app. This layer users [Ktor](https://ktor.io)
for networking and [SQLDelight](https://cashapp.github.io/sqldelight/) to cache
responses in a database.

The Android app is built using MVI with the [Orbit MVI](https://github.com/babylonhealth/orbit-mvi)
library that I co-authored.

Dependency injection is setup using [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

Views use RecyclerViews and [Groupie](https://github.com/lisawray/groupie)
to help build reusable shared components configured to match a design system.

Animations have been implemented through [AnimatedVectorDrawable](https://developer.android.com/guide/topics/graphics/drawable-animation#AnimVector)
created using [Shape Shifter](https://shapeshifter.design) and [MotionLayout](https://developer.android.com/training/constraint-layout/motionlayout).

### Testing

ViewModels logic tests use the [Orbit Unit Testing module](https://github.com/orbit-mvi/orbit-mvi/tree/main/orbit-test),
which allows testing the interactions in isolation.

The network layer has an integration test to verify the contract with the
backend.

## Future improvements

- Implement iOS application

- Investigate [MotionLayoutSavedStateViewModel](androidApp/src/main/kotlin/com/mattdolan/cv/common/ui/MotionLayoutSavedStateViewModel.kt)
  for memory leaks, potentially using lifecycle aware components for binding

- Additional testing
  - Improvements to Espresso tests as currently, MotionLayout does not animate
    correctly. 
  - Implement screenshot/interaction tests once Orbit MVI has support

- Setup CI
  - [GitHub Actions](https://github.com/features/actions)
  - Tags to upload APK to GitHub
  - [codecov.io](https://about.codecov.io)

- Add additional content
  - Education section
  - Medium blog posts
  - Conference talks
  - Open-source software

- Migrate to [Jetpack Compose](https://developer.android.com/jetpack/compose)

- The app currently doesn't log any data or exceptions, and could potentially
  use [Timber](https://github.com/JakeWharton/timber).
