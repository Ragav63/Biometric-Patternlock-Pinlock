package com.example.biometricauthjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PinEntryActivity extends AppCompatActivity {

    private EditText etPin;
    private Button btnSubmitPin;
    private PinStorageManager pinStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);

        etPin = findViewById(R.id.etPin);
        btnSubmitPin = findViewById(R.id.btnSubmitPin);
        pinStorage = new PinStorageManager(this);

        btnSubmitPin.setOnClickListener(view -> {
            String enteredPin = etPin.getText().toString();
            String savedPin = pinStorage.getPin();

            if (enteredPin.equals(savedPin)) {
                Toast.makeText(this, "PIN correct", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PinEntryActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
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