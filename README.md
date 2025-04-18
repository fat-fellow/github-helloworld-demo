# github-helloworld-demo

Android HelloWorld Demo

## Overview

This repository serves as a sample project for code review. It demonstrates how I organize code, work with large language models (LLMs), and showcase my development practices. While the project is intentionally over-engineered, it is designed to support a modular architecture inspired by [Square's approach](https://www.droidcon.com/2019/11/15/android-at-scale-square/).

## Architecture

The project follows a **clean architecture** pattern, ensuring separation of concerns and scalability. Key architectural components include:

- **Anvil Components/Subcomponents**: Used to simplify dependency injection and avoid generating long Dagger graphs. Subcomponents are utilized for feature-specific dependencies.
- **Scopes**: Scopes are defined to manage the lifecycle of dependencies effectively, ensuring proper resource management and modularity. 
- **Two Screens**: The project includes two screens to demonstrate navigation and feature modularization.

## Design Decisions

- The repository is designed to support modularization, making it easier to scale and maintain.
- While the project lacks integration tests and a convention plugin in `build-logic` for simplicity, these can be added in a real-world scenario.
- The use of Anvil and Subcomponents reduces boilerplate and improves developer productivity.

## Testing

The project includes unit tests to validate core functionality. However, integration tests are not included to keep the repository simple and focused on demonstrating architecture and modularization. [View Tests](./app/src/test/java/com/example/githubhelloworlddemo)

## Notes

This repository is a demonstration of how to build a scalable and modular Android application. While it may be overkill for a simple HelloWorld app, it showcases best practices and design principles for larger projects.

