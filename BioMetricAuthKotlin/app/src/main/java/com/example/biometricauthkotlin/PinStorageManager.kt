package com.example.biometricauthkotlin

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.text.split
import kotlin.text.toInt

class PinStorageManager(context: Context) {

    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun savePin(pin: String) {
        sharedPreferences.edit().putString("user_pin", pin).apply()
    }

    fun getPin(): String? {
        return sharedPreferences.getString("user_pin", null)
    }

    fun isPinSet(): Boolean {
        return sharedPreferences.contains("user_pin")
    }

    fun clearPin() {
        sharedPreferences.edit().remove("user_pin").apply()
    }

    // Save the pattern as a comma-separated string
    fun savePattern(pattern: List<Int>) {
        val patternString = pattern.joinToString(",")
        sharedPreferences.edit().putString("user_pattern", patternString).apply()
    }

    // Get the saved pattern
    fun getPattern(): List<Int>? {
        val patternString = sharedPreferences.getString("user_pattern", null)
        return patternString?.split(",")?.map { it.toInt() }
    }

    // Check if the pattern is set
    fun isPatternSet(): Boolean {
        return sharedPreferences.contains("user_pattern")
    }

    // Clear the pattern
    fun clearPattern() {
        sharedPreferences.edit().remove("user_pattern").apply()
    }
}