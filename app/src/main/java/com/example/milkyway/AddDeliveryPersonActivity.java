package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDeliveryPersonActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextPassword, editTextArea;
    private Button btnAddDeliveryPerson;
    private FirebaseFirestore db;
    private String shopId;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_person);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve shopId from the current user's UID
        if (auth.getCurrentUser() != null) {
            shopId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextArea = findViewById(R.id.editTextArea);
        btnAddDeliveryPerson = findViewById(R.id.btnAddDeliveryPerson);

        // Set OnClickListener for the Add Delivery Person button
        btnAddDeliveryPerson.setOnClickListener(view -> addDeliveryPerson());
    }

    private void addDeliveryPerson() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String area = editTextArea.getText().toString().trim();

        // Basic validation
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || area.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Firebase Authentication user first
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the UID of the newly created user
                        FirebaseUser newUser = task.getResult().getUser();
                        if (newUser != null) {
                            String deliveryPersonId = newUser.getUid();  // This will be used as document ID in Firestore

                            // Prepare delivery person data
                            Map<String, Object> deliveryPerson = new HashMap<>();
                            deliveryPerson.put("name", name);
                            deliveryPerson.put("phone", phone);
                            deliveryPerson.put("email", email);
                            deliveryPerson.put("area", area);
                            deliveryPerson.put("shopId", shopId);

                            // Add delivery person data to Firestore under the "deliveryPersons" collection using UID as document ID
                            db.collection("deliveryPersons")
                                    .document(deliveryPersonId)
                                    .set(deliveryPerson)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(AddDeliveryPersonActivity.this, "Delivery person added successfully", Toast.LENGTH_SHORT).show();
                                        clearFields();
                                        Intent intent = new Intent(this, ShopkeeperDashboardActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(AddDeliveryPersonActivity.this, "Failed to add delivery person: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        // Handle sign-up failure
                        Toast.makeText(AddDeliveryPersonActivity.this, "Failed to create account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void clearFields() {
        editTextName.setText("");
        editTextPassword.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");
        editTextArea.setText("");
    }
}
