package com.example.biometricauthjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biometricauthjava.databinding.ActivityMainBinding;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PinStorageManager pinStorage;
    private BiometricPromptManager promptManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinStorage = new PinStorageManager(this);
        promptManager = new BiometricPromptManager(this);

        // Set up the biometric prompt callback
        promptManager.setCallback(new BiometricPromptManager.ResultCallback() {
            @Override
            public void onAuthenticationSuccess() {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                runOnUiThread(() -> binding.textStatus.setText("Authentication failed"));
            }

            @Override
            public void onAuthenticationError(String error) {
                runOnUiThread(() -> binding.textStatus.setText(error));
            }

            @Override
            public void onHardwareUnavailable() {
                runOnUiThread(() -> binding.textStatus.setText("Biometric hardware unavailable"));
            }

            @Override
            public void onFeatureUnavailable() {
                runOnUiThread(() -> binding.textStatus.setText("Biometric feature not available"));
            }

            @Override
            public void onAuthenticationNotSet() {
                startActivity(new Intent(MainActivity.this, SetPinActivity.class));
            }
        });

        binding.btnBiometric.setOnClickListener(view ->
                promptManager.showBiometricPrompt("Biometric Login", "Use fingerprint or device lock"));

        binding.btnPin.setOnClickListener(view -> {
            boolean hasPin = pinStorage.getPin() != null && !pinStorage.getPin().isEmpty();
            Class<?> target = hasPin ? PinEntryActivity.class : SetPinActivity.class;
            startActivity(new Intent(MainActivity.this, target));
        });

        binding.btnPattern.setOnClickListener(view -> {
            boolean hasPattern = pinStorage.getPattern() != null && !pinStorage.getPattern().isEmpty();
            Class<?> target = hasPattern ? EnterPatternActivity.class : SetPatternActivity.class;
            startActivity(new Intent(MainActivity.this, target));
        });
    }


    }
