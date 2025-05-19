package com.example.biometricauthkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biometricauthkotlin.databinding.ActivitySetPinBinding
import kotlin.jvm.java

class SetPinActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetPinBinding
    private lateinit var pinStorage: PinStorageManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pinStorage = PinStorageManager(this)

        binding.btnSave.setOnClickListener {
            val pin = binding.editPin.text.toString()
            if (pin.isNotEmpty()) {
                pinStorage.savePin(pin)
                Toast.makeText(this, "PIN saved successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter a PIN", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}