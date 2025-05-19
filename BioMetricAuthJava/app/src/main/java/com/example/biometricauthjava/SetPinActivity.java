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

public class SetPinActivity extends AppCompatActivity {
    private EditText editPin;
    private Button btnSave;
    private PinStorageManager pinStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        editPin = findViewById(R.id.editPin);
        btnSave = findViewById(R.id.btnSave);
        pinStorage = new PinStorageManager(this);

        btnSave.setOnClickListener(view -> {
            String pin = editPin.getText().toString();
            if (!pin.isEmpty()) {
                pinStorage.savePin(pin);
                Toast.makeText(this, "PIN saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetPinActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please enter a PIN", Toast.LENGTH_SHORT).show();
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