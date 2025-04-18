# github-helloworld-demo

Android HelloWorld Demo

## Tech Stack

- **Architecture**: MVVM
- **Networking**: Ktor
- **UI**: Jetpack Compose
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
- **ViewModel Lifecycle-tied Scoping**: Feature component lifecycles are tied to the lifetime of corresponding ViewModels. Component holders manually control creation and release of scoped components.
- **Two Screens**: The app features two main screens:
    - **Repo** feature: Displays public repositories for a given user.
    - **Info** feature: Displays activity information for a selected repository.

## Design Decisions

- Modularization is inspired by Square’s large-scale architecture.
- Feature APIs are split into `api` and `impl` modules.
- `Composable` screens are exposed via public API interfaces.
- DI is handled using Anvil scopes with `@MergeComponent`, `@MergeSubcomponent`, and `@ContributesBinding`.
- Manual component lifecycle management allows flexible feature scoping without relying on Fragments or navigation backstack scoping.
- The `Resultat` class extends Kotlin's `Result` to provide a lightweight and expressive way to manage UI state, including success, loading, and error states.
- A shared interface and abstract base class are used for UseCases. This is a debatable design choice, but it helps ensure consistency and flexibility in logic invocation. It’s not required, but unifies usage across modules.
- In the data layer, error wrapping via `Resultat` is intentionally avoided. Errors are passed directly to the domain layer to retain semantic clarity and reduce boilerplate.

## Remaining Improvements

- Move common boilerplate (e.g., `build.gradle.kts` settings) to a convention plugin under `build-logic`.
- Add integration tests with proper fake/stub/mock infrastructure.
- Improve lifecycle management of scopes — current approach relies on `ViewModel` lifetime, which might be insufficient in real-world apps. A more robust solution would handle scope release upon navigation events in pure Compose.

## Testing

The project includes unit and instrumentation tests for the **Repos** feature only. The purpose of this sample is not to cover all edge cases but to demonstrate modular structure and basic testability.
