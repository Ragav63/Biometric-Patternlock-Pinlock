// Top-level build file where you can add configuration options common to all sub-projects/modules.

extra.apply {
    set("compileSdkVersion", 36)     // ⬅️ Bump this from 34 to 36
    set("minSdkVersion", 21)
    set("targetSdkVersion", 36)      // ⬅️ Recommended to match compileSdk
    set("buildToolsVersion", "34.0.0")

    set("supportV7", "androidx.appcompat:appcompat:1.6.1")
    set("rxJava", "io.reactivex.rxjava3:rxjava:3.1.8")
    set("rxAndroid", "io.reactivex.rxjava3:rxandroid:3.0.2")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
