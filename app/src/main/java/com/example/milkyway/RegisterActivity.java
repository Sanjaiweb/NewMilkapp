package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText shopNameEditText, accountNameEditText, emailEditText, passwordEditText, fssaiEditText;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        shopNameEditText = findViewById(R.id.shopNameEditText);
        accountNameEditText = findViewById(R.id.accountNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        fssaiEditText = findViewById(R.id.fssaiEditText);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String shopName = shopNameEditText.getText().toString().trim();
        String accountName = accountNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String fssai = fssaiEditText.getText().toString().trim();

        if (shopName.isEmpty() || accountName.isEmpty() || email.isEmpty() || password.isEmpty() || fssai.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success, add user details to Firestore
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Create a map for user details
                            Map<String, Object> userDetails = new HashMap<>();
                            userDetails.put("accountName", accountName);
                            userDetails.put("email", email);
                            userDetails.put("fssai", fssai);
                            userDetails.put("uid", userId);
                            userDetails.put("shopName", shopName); // Store shop name in user collection

                            // Store user details in "users" collection
                            firestore.collection("users")
                                    .document(userId)
                                    .set(userDetails)
                                    .addOnSuccessListener(aVoid -> {
                                        // Now store shop details in "shops" collection
                                        Map<String, Object> shopDetails = new HashMap<>();
                                        shopDetails.put("shopName", shopName);
                                        shopDetails.put("userId", userId); // Link shop to the user

                                        // Store shop details in "shops" collection
                                        firestore.collection("shops")
                                                .document(userId) // Use userId as document ID for shops
                                                .set(shopDetails)
                                                .addOnSuccessListener(aVoid1 -> {
                                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this,ShopkeeperDashboardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("RegisterActivity", "Firestore Error: ", e);
                                                    Toast.makeText(RegisterActivity.this, "Failed to save shop details", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("RegisterActivity", "Firestore Error: ", e);
                                        Toast.makeText(RegisterActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // If registration fails, display a message to the user.
                            Log.e("RegisterActivity", "Registration Failed", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
