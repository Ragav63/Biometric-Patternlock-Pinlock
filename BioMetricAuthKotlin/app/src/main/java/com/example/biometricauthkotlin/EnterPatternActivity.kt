package com.example.biometricauthkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.example.biometricauthkotlin.databinding.ActivityEnterPatternBinding

class EnterPatternActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEnterPatternBinding

    private lateinit var pinStorageManager: PinStorageManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEnterPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinStorageManager = PinStorageManager(this)

        val storedPattern = pinStorageManager.getPattern()

        if (storedPattern == null) {
            Toast.makeText(this, "No pattern set. Please set one first.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SetPatternActivity::class.java))
            finish()
            return
        }

        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                val enteredPatternStr = PatternLockUtils.patternToString(binding.patternLockView, pattern)
                val enteredPattern = enteredPatternStr.map { it.toString().toInt() }

                if (enteredPattern == storedPattern) {
                    startActivity(Intent(this@EnterPatternActivity, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@EnterPatternActivity, "Incorrect Pattern", Toast.LENGTH_SHORT).show()
                    binding.patternLockView.clearPattern()
                }
            }

            override fun onStarted() {}
            override fun onCleared() {}
            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}