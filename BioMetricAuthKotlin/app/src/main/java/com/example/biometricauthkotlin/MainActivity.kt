package com.example.biometricauthkotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.biometricauthkotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var pinStorage: PinStorageManager
    private lateinit var promptManager: BiometricPromptManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pinStorage = PinStorageManager(this)
        promptManager = BiometricPromptManager(this)

        binding.apply {



        btnBiometric.setOnClickListener {
            promptManager.showBiometricPrompt("Biometric Login", "Use fingerprint or device lock")
        }

        btnPin.setOnClickListener {
            val hasPin = !pinStorage.getPin().isNullOrEmpty()
            startActivity(Intent(this@MainActivity, if (hasPin) PinEntryActivity::class.java else SetPinActivity::class.java))
        }

        btnPattern.setOnClickListener {
            val hasPattern = !pinStorage.getPattern().isNullOrEmpty()
            startActivity(Intent(this@MainActivity, if (hasPattern) EnterPatternActivity::class.java else SetPatternActivity::class.java))
        }

        lifecycleScope.launch {
            promptManager.promptResults.collect { result ->
                when (result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }
                    is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                        textStatus.text = result.error
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                        textStatus.text = "Authentication failed"
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                        startActivity(Intent(this@MainActivity, SetPinActivity::class.java))
                    }
                    else -> Unit
                }
            }
        }
        }
    }
}