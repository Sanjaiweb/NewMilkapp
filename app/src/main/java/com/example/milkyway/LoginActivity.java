package com.example.milkyway;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button shopkeeperTab, deliveryTab, loginButton;
    private boolean isShopkeeper = true; // Track the selected role
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        shopkeeperTab = findViewById(R.id.shopkeeperTab);
        deliveryTab = findViewById(R.id.deliveryTab);
        loginButton = findViewById(R.id.loginButton);

        // Set default selection
        updateUI();

        // Tab click listeners
        shopkeeperTab.setOnClickListener(v -> {
            isShopkeeper = true;
            updateUI();
        });

        deliveryTab.setOnClickListener(v -> {
            isShopkeeper = false;
            updateUI();
        });

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            if (isShopkeeper) {
                loginAsShopkeeper();
            } else {
                loginAsDeliveryPerson();
            }
        });
    }

    private void updateUI() {
        if (isShopkeeper) {
            shopkeeperTab.setBackgroundColor(Color.BLUE);
            deliveryTab.setBackgroundColor(Color.GRAY);
            loginButton.setText("Login as Shopkeeper");
        } else {
            deliveryTab.setBackgroundColor(Color.BLUE);
            shopkeeperTab.setBackgroundColor(Color.GRAY);
            loginButton.setText("Login as Delivery Person");
        }
    }

    private void loginAsShopkeeper() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Successful login, proceed to the Shopkeeper dashboard
                            Intent intent = new Intent(this, ShopkeeperDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginAsDeliveryPerson() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Successful login, proceed to the Delivery Person dashboard
                            Intent intent = new Intent(this, DeliveryDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
