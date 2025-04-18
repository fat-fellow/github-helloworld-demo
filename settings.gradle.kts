pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Github HelloWorld Demo"
include(":app")
include(":feature:repos:api")
include(":feature:repos:impl")
include(":feature:info:api")
include(":feature:info:impl")
include(":common:utils")
include(":common:network")
include(":common:di")
include(":common:domain")
