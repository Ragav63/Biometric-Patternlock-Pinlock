pluginManagement {
    plugins {
        id("com.android.application") version "8.9.1"
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://www.jitpack.io") } // for GitHub libs
        // jcenter() // Only include if a library fails without it
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://repository.liferay.com/nexus/content/repositories/public/") }
        // jcenter() // Only if strictly necessary
    }
}

rootProject.name = "BioMetricAuthJava"

// Include all modules you are using
include(":app")

