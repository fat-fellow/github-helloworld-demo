# github-helloworld-demo

Android HelloWorld Demo

## Tech Stack

- **Architecture**: MVVM
- **Networking**: Ktor + Kotlin Serialization
- **UI**: Jetpack Compose / Jetpack Navigation
- **DI**: Dagger + Anvil
- **State**: Kotlin Flow 
- **Testing**: Unit tests + Instrumentation tests

## Overview

This repository serves as a sample project for code review. It demonstrates how I organize code, work with large language models (LLMs), and showcase my development practices. While the project is intentionally over-engineered, it is designed to support a modular architecture inspired by [Square's approach](https://www.droidcon.com/2019/11/15/android-at-scale-square/).

The project is a simple GitHub API viewer. The first screen returns the list of public repositories for a given user, and the second screen shows the activity for a selected repository, if available. There is no design polish, but care was taken to make UI responsive and clean (e.g., no flickers).

## Architecture

The project follows a **clean architecture** pattern, ensuring separation of concerns and scalability. Key architectural components include:

- **Anvil Components/Subcomponents**: Implemented as a showcase with two independent graphs and one subcomponent. The first approach avoids generating large Dagger graphs since no subcomponent is used. The second demonstrates the advantage of subcomponents in accessing dependencies from a parent graph without explicitly passing them.
- **Scopes**: `AppScope`, `ReposScope`, `InfoScope` — for app-wide, Repos feature, and Info feature lifecycles, respectively.
- **ViewModel Lifecycle-tied Scoping**: Feature component lifecycles are tied to the lifetime of corresponding ViewModels.
- **Two Screens**: The app features two main screens:
    - **Repo** feature: Displays public repositories for a given user.
    - **Info** feature: Displays activity information for a selected repository.

## Design Decisions

- Modularization is inspired by Square’s large-scale architecture.
- Feature APIs are split into `api` and `impl` modules.
- The `Resultat` class extends Kotlin's `Result` to provide a lightweight and expressive way to manage UI state, including success, loading, and error states.
- A shared interface and abstract base class are used for UseCases. This is a debatable design choice, but it helps ensure consistency and flexibility in logic invocation. It’s not required, but unifies usage across modules.
- In the data layer, error wrapping via `Resultat` is intentionally avoided. Errors are passed directly to the domain layer to retain semantic clarity and reduce boilerplate.

## Remaining Improvements

- Move common boilerplate (e.g., `build.gradle.kts` settings) to a convention plugin under `build-logic`.
- Add integration tests with proper fake/stub/mock infrastructure.
- Improve lifecycle management of scopes — current approach relies on `ViewModel` lifetime, which might be insufficient in real-world apps. A more robust solution would handle scope release upon navigation events in pure Compose.

## Testing

The project includes unit and instrumentation tests for the **Repos** feature only. The purpose of this sample is not to cover all edge cases but to demonstrate modular structure and basic testability.

## Project Layout

```
├── app/                         # Application entry point
│   ├── MainActivity.kt         # Navigation host and DI setup
│   └── di/                     # AppComponent definition
│
├── common/                     # Shared core modules
│   ├── di/                     # Shared di components
│   ├── domain/                 # Shared domain models (errors)
│   ├── network/                # Ktor client and API helpers
│   └── utils/                  # Resultat and UseCase (probably utils is not good enough name)
│
├── feature/                    # Feature modules
│   ├── info/                   # Info screen
│   │   ├── api/                # Interface + ViewModel + DI entry (presentation layer + usecase interfaces)
│   │   └── impl/               # Feature implementation (usecase impls, data layer)
│   └── repos/                  # Repos screen
│       ├── api/
│       └── impl/
│
└── build.gradle.kts            # Root Gradle file