pluginManagement {
    repositories {
        /*google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }*/
        google()
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

rootProject.name = "loom"
include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":core:network")
include(":core:notifications")
include(":core:ui")
include(":feature:foryou")
include(":feature:foryou:api")
include(":feature:foryou:impl")
include(":feature:explore")
include(":feature:auth")
include(":sync")
include(":sync:work")


include(":feature:explore:api")
include(":feature:explore:impl")
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:home")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:post-editor")
include(":feature:post-editor:api")
include(":feature:post-editor:impl")
include(":feature:profile")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:settings")
include(":feature:settings:api")
include(":feature:settings:impl")
