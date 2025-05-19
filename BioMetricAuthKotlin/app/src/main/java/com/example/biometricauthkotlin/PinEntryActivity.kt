package com.example.biometricauthkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biometricauthkotlin.databinding.ActivityPinEntryBinding
import kotlin.jvm.java

class PinEntryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPinEntryBinding

    private lateinit var pinStorage: PinStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPinEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinStorage = PinStorageManager(this)

        binding.btnSubmitPin.setOnClickListener {
            val enteredPin = binding.etPin.text.toString()
            val correctPin = pinStorage.getPin()

            if (enteredPin == correctPin) {
                Toast.makeText(this, "PIN correct", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}