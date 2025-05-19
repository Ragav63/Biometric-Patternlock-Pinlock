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
import com.example.biometricauthjava.databinding.ActivitySetPatternBinding;

import java.util.ArrayList;
import java.util.List;

public class SetPatternActivity extends AppCompatActivity {

    private ActivitySetPatternBinding binding;
    private String firstPattern = null;
    private PinStorageManager pinStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivitySetPatternBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pinStorageManager = new PinStorageManager(this);

        binding.patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                // no-op
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                // no-op
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String patternStr = PatternLockUtils.patternToString(binding.patternLockView, pattern);

                if (firstPattern == null) {
                    firstPattern = patternStr;
                    Toast.makeText(SetPatternActivity.this, "Draw pattern again to confirm", Toast.LENGTH_SHORT).show();
                    binding.patternLockView.clearPattern();
                } else if (firstPattern.equals(patternStr)) {
                    // Convert pattern string to list of integers
                    List<Integer> patternInts = new ArrayList<>();
                    for (char c : patternStr.toCharArray()) {
                        patternInts.add(Character.getNumericValue(c));
                    }
                    pinStorageManager.savePattern(patternInts);

                    Toast.makeText(SetPatternActivity.this, "Pattern saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetPatternActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(SetPatternActivity.this, "Patterns do not match. Try again.", Toast.LENGTH_SHORT).show();
                    firstPattern = null;
                    binding.patternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {
                // no-op
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