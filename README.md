# 🔐 Biometric + Pattern Lock + Pin Lock Authentication (Android)

This Android project implements a **secure multi-authentication system** using:

- **Biometric Authentication** (Fingerprint)
- **Pattern Lock** using [PatternLockView](https://github.com/aritraroy/PatternLockView)
- **PIN Lock**

> Built using **Kotlin & XML**, compatible with Android API 21+.

## 🧠 Features

- Fingerprint authentication using `BiometricPrompt`
- Custom PIN entry screen
- Pattern lock screen using `PatternLockView`
- Fallback options between auth methods
- Minimal, clean UI
- Supports saving and validating user-set PIN or Pattern

---

## 📲 Screenshots

![PatternLockView](https://github.com/aritraroy/PatternLockView/blob/master/screenshots/pattern-lock-view-banner.png?raw=true)

---

## 🛠 Tech Stack

- **Language:** Kotlin (Android)
- **Architecture:** MVVM
- **UI:** XML Layouts
- **Security:** BiometricPrompt API, encrypted `SharedPreferences`
- **Libraries:**  
  - [PatternLockView](https://github.com/aritraroy/PatternLockView)

---

## 🚀 Getting Started

### Clone the Repo

```bash
git clone https://github.com/Ragav63/Biometric-Patternlock-Pinlock.git
cd Biometric-Patternlock-Pinlock
```

### Open in Android Studio and Build
Open the project in Android Studio.

Let Gradle sync and resolve dependencies.

Run the app on an emulator or physical device (API 21+).

### 🔐 About PatternLockView
Overview
PatternLockView is a fully customizable and Material Design-compliant view for drawing pattern locks in Android.



Gradle Setup
gradle
Copy
Edit

```
dependencies {
    implementation 'com.andrognito.patternlockview:patternlockview:1.0.0'
    // Optional RxJava2 support
    implementation 'com.andrognito.patternlockview:patternlockview-reactive:1.0.0'
}
```

Usage
In XML:

xml
Copy
Edit
```
<com.andrognito.patternlockview.PatternLockView
    android:id="@+id/pattern_lock_view"
    android:layout_width="280dp"
    android:layout_height="280dp"/>
```

In Kotlin/Java:

kotlin
Copy
Edit
```
val patternLockView = findViewById<PatternLockView>(R.id.pattern_lock_view)
patternLockView.addPatternLockListener(object : PatternLockViewListener {
    override fun onStarted() { }
    override fun onProgress(pattern: List<PatternLockView.Dot>) { }
    override fun onComplete(pattern: List<PatternLockView.Dot>) {
        val patternStr = PatternLockUtils.patternToString(patternLockView, pattern)
        // Save or compare patternStr
    }
    override fun onCleared() { }
})
```

### 🎨 PatternLock Customization
You can change colors, sizes, and behavior via XML:

xml
Copy
Edit
```
app:dotCount="3"
app:dotNormalSize="12dp"
app:dotSelectedSize="24dp"
app:pathWidth="4dp"
app:normalStateColor="@color/white"
app:correctStateColor="@color/primary"
app:wrongStateColor="@color/red"
```
Or via code:

kotlin
Copy
Edit
patternLockView.setInStealthMode(true)
patternLockView.setTactileFeedbackEnabled(true)
patternLockView.setDotCount(3)
🧪 Sample Pattern Listener
kotlin
Copy
Edit
```
override fun onComplete(pattern: List<PatternLockView.Dot>) {
    val enteredPattern = PatternLockUtils.patternToString(patternLockView, pattern)
    if (enteredPattern == savedPattern) {
        // Success
    } else {
        // Failure
    }
}
```

### 📜 License
pgsql
Copy
Edit
Copyright 2017 aritraroy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0


### 🙋‍♂️ Author
Ragav
Android Developer | GitHub

PatternLockView by Aritra Roy
Twitter | LinkedIn

🌟 Show Your Support
If you like this project:

⭐️ Star this repo
🍴 Fork it
📢 Share it
