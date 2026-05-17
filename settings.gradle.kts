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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TodoHabitFocus"
include(":app")
include(":core")
include(":core-ui")
include(":core-designsystem")
include(":core-database")
include(":core-network")
include(":core-notification")
include(":core-domain")
include(":feature-auth")
include(":feature-onboarding")
include(":feature-home")
include(":feature-task")
include(":feature-habit")
include(":feature-focus")
include(":feature-analytics")
include(":feature-settings")
