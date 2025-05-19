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
import com.example.biometricauthkotlin.databinding.ActivitySetPatternBinding

class SetPatternActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetPatternBinding
    private var firstPattern: String? = null
    private lateinit var pinStorage: PinStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinStorage = PinStorageManager(this)

        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                val patternStr = PatternLockUtils.patternToString(binding.patternLockView, pattern)

                if (firstPattern == null) {
                    firstPattern = patternStr
                    Toast.makeText(this@SetPatternActivity, "Draw pattern again to confirm", Toast.LENGTH_SHORT).show()
                    binding.patternLockView.clearPattern()
                } else if (firstPattern == patternStr) {
                    // Save pattern securely using PinStorageManager
                    val patternInts = patternStr.map { it.toString().toInt() }
                    pinStorage.savePattern(patternInts)

                    Toast.makeText(this@SetPatternActivity, "Pattern saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetPatternActivity, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SetPatternActivity, "Patterns do not match. Try again.", Toast.LENGTH_SHORT).show()
                    firstPattern = null
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