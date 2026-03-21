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

rootProject.name = "Washing_app"
include(":app")
include(":core")
include(":core:navigation")
include(":core:ui")
include(":core:common")
include(":feature")
include(":feature:machines")
include(":feature:booking")
include(":feature:payment")
include(":feature:qr")
include(":data")
include(":domain")
include(":feature:auth")
