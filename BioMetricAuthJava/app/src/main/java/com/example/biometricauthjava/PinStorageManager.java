package com.example.biometricauthjava;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class PinStorageManager {

    private SharedPreferences sharedPreferences;

    public PinStorageManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void savePin(String pin) {
        sharedPreferences.edit().putString("user_pin", pin).apply();
    }

    public String getPin() {
        return sharedPreferences.getString("user_pin", null);
    }

    public boolean isPinSet() {
        return sharedPreferences.contains("user_pin");
    }

    public void clearPin() {
        sharedPreferences.edit().remove("user_pin").apply();
    }

    public void savePattern(List<Integer> pattern) {
        StringBuilder patternString = new StringBuilder();
        for (int i = 0; i < pattern.size(); i++) {
            patternString.append(pattern.get(i));
            if (i < pattern.size() - 1) {
                patternString.append(",");
            }
        }
        sharedPreferences.edit().putString("user_pattern", patternString.toString()).apply();
    }

    public List<Integer> getPattern() {
        String patternString = sharedPreferences.getString("user_pattern", null);
        if (patternString == null) return null;

        List<Integer> patternList = new ArrayList<>();
        String[] parts = patternString.split(",");
        for (String part : parts) {
            try {
                patternList.add(Integer.parseInt(part));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return patternList;
    }

    public boolean isPatternSet() {
        return sharedPreferences.contains("user_pattern");
    }

    public void clearPattern() {
        sharedPreferences.edit().remove("user_pattern").apply();
    }
}
