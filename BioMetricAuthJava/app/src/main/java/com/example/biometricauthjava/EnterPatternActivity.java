package com.example.biometricauthjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.biometricauthjava.databinding.ActivityEnterPatternBinding;

import java.util.ArrayList;
import java.util.List;

public class EnterPatternActivity extends AppCompatActivity {

    private ActivityEnterPatternBinding binding;
    private PinStorageManager pinStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEnterPatternBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinStorageManager = new PinStorageManager(this);
        List<Integer> storedPattern = pinStorageManager.getPattern();

        if (storedPattern == null) {
            Toast.makeText(this, "No pattern set. Please set one first.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SetPatternActivity.class));
            finish();
            return;
        }

        binding.patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                // Do nothing
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                // Do nothing
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String enteredPatternStr = PatternLockUtils.patternToString(binding.patternLockView, pattern);
                List<Integer> enteredPattern = new ArrayList<>();

                for (char ch : enteredPatternStr.toCharArray()) {
                    enteredPattern.add(Character.getNumericValue(ch));
                }

                if (enteredPattern.equals(storedPattern)) {
                    startActivity(new Intent(EnterPatternActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(EnterPatternActivity.this, "Incorrect Pattern", Toast.LENGTH_SHORT).show();
                    binding.patternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {
                // Do nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}