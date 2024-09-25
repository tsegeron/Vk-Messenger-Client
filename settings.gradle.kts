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

rootProject.name = "VK Messenger"
include(":app")
include(":feature")
include(":core")
include(":core:ui")
include(":core:notifications")
include(":core:datastore")
include(":core:common")
include(":core:data")
include(":feature:chat")
include(":feature:settings")
include(":feature:chats")
include(":feature:auth")
include(":feature:friends")
include(":feature:chatinfo")
