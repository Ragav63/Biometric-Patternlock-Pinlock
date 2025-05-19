package com.example.biometricauthjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricPromptManager {

    private final AppCompatActivity activity;
    private final Executor executor;
    private ResultCallback callback;

    public BiometricPromptManager(AppCompatActivity activity) {
        this.activity = activity;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public interface ResultCallback {
        void onAuthenticationSuccess();
        void onAuthenticationFailed();
        void onAuthenticationError(String error);
        void onHardwareUnavailable();
        void onFeatureUnavailable();
        void onAuthenticationNotSet();
    }

    public void setCallback(ResultCallback callback) {
        this.callback = callback;
    }

    public void showBiometricPrompt(String title, String description) {
        int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG;

        BiometricManager manager = BiometricManager.from(activity);

        int authStatus = manager.canAuthenticate(authenticators);
        switch (authStatus) {
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                if (callback != null) callback.onHardwareUnavailable();
                return;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                if (callback != null) callback.onFeatureUnavailable();
                return;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                if (callback != null) callback.onAuthenticationNotSet();
                return;
            default:
                break;
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(authenticators)
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (callback != null) callback.onAuthenticationError(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (callback != null) callback.onAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (callback != null) callback.onAuthenticationFailed();
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }
}
