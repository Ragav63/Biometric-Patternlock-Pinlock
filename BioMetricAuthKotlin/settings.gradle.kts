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
        maven { url = uri("https://www.jitpack.io")}
        jcenter()
        plugins {
            id("androidx.navigation.safeargs.kotlin") version "2.8.9"
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io")}
        maven { url =uri("https://repository.liferay.com/nexus/content/repositories/public/" )}
        jcenter()
    }
}

rootProject.name = "BioMetricAuth(Kotlin)"
include(":app")
 