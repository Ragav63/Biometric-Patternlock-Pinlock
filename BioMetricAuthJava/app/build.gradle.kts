import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.biometricauthjava"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    defaultConfig {
        applicationId = "com.example.biometricauthjava"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // PatternLock source is now directly in app/, no need for project() dependencies
    // Local library logic from patternlockview and patternlockview-reactive is now inside app

    // Core dependencies
    implementation(rootProject.extra["supportV7"] as String)
    implementation(rootProject.extra["rxJava"] as String)
    implementation(rootProject.extra["rxAndroid"] as String)

    // AndroidX and Google dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.biometric)
    implementation(libs.security.crypto)


    implementation ("com.andrognito.patternlockview:patternlockview:1.0.0")
    // Optional, for RxJava2 adapter
    implementation ("com.andrognito.patternlockview:patternlockview-reactive:1.0.0")

    // JUnit and Instrumentation tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
