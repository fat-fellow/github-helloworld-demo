# github-helloworld-demo

An android application showcasing how I approach to code with modern development practices.

## Tech Stack

- **Architecture**: MVVM with Clean Architecture principles
- **Networking**: Ktor + Kotlin Serialization
- **UI**: Jetpack Compose with Material Design 3
- **Navigation**: Compose Navigation
- **Dependency Injection**: Hilt
- **Concurrency**: Kotlin Flow and Coroutines
- **Testing**: Unit tests + Instrumentation tests with MockK, and Compose Testing + Jacoco for code coverage

## Overview

A very basic 2 screens GitHub API viewer

The application features two screens: one displaying public repositories for a given user, and another showing activity history for a selected repository. Each screen maintains its own top bar and navigation, providing a smooth and responsive user experience with careful attention to UI state management.

Design made by the developer, sorry :)

## Architecture

- **Modular Feature Design**: Each feature contains its own presentation, domain, and data layers
- **Scoped Dependencies**: Dependencies are scoped to `Application` and `ViewModel` for lifecycle management
- **ViewModel Integration**: Hilt's assisted injection allows ViewModels to receive dynamic parameters (owner and repository names)
- **State Management**: UI state is expressed through sealed classes, helping visualize state transitions
- **Two Features**:
    - **Repos**: Browse public repositories by username
    - **Info**: View activity history for selected repositories
-- **Convention Plugins**: Custom Gradle plugins in `build-logic` to standardize module configurations and reduce boilerplate

## Testing

The project includes both unit and instrumentation tests:

### Running Tests

```bash
# Run unit tests
./gradlew app:testDebugUnitTest

# Run instrumentation tests on connected device/emulator
./gradlew app:connectedDebugAndroidTest

# Check code formatting
./gradlew ktlintCheck

# Check code style with Detekt
./gradlew detekt

# Run lint
./gradlew lint

# Run jacoco after tests to check code coverage (build/reports in corresponding dirs)
./gradlew createDebugCombinedCoverageReport
```

Tests currently cover the **Repos** feature, showing how tests can be structured in a modular architecture.

## Areas to Explore

- Konsist for architecture tests and consistency validation
- Add a rule example for Lint and Detekt
- Add description for the catching an error design decision

## Project Layout

```
├── app/                        # Application entry point
│   ├── MainActivity.kt         # Navigation host and DI setup
│
├── common/                     # Shared core modules
│   ├── domain/                 # Shared domain models
│   └── network/                # Ktor client and API helpers
│
├── feature/                    # Feature modules
│   ├── info/                   # Repository info screen
│   └── repos/                  # Repositories list screen
│
└── build.gradle.kts            # Root Gradle configuration