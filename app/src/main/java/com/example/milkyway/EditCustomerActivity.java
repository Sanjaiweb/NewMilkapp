package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCustomerActivity extends AppCompatActivity {

    private EditText editTextName, editTextAddress, editTextPhone, editTextSubscription, editTextArea;
    private Button buttonUpdateCustomer;
    private FirebaseFirestore db;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve customer ID from Intent
        Intent intent = getIntent();
        customerId = intent.getStringExtra("customerId");

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextSubscription = findViewById(R.id.editTextSubscription);
        editTextArea = findViewById(R.id.editTextArea);
        buttonUpdateCustomer = findViewById(R.id.buttonUpdateCustomer);

        // Load existing customer data
        loadCustomerData();

        // Set OnClickListener for Update button
        buttonUpdateCustomer.setOnClickListener(view -> updateCustomer());
    }

    private void loadCustomerData() {
        db.collection("customers").document(customerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        if (customer != null) {
                            editTextName.setText(customer.getName());
                            editTextAddress.setText(customer.getAddress());
                            editTextPhone.setText(customer.getPhone());
                            editTextSubscription.setText(customer.getSubscription());
                            editTextArea.setText(customer.getArea());
                        }
                    } else {
                        Toast.makeText(EditCustomerActivity.this, "Customer not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(EditCustomerActivity.this, "Error loading customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateCustomer() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String subscription = editTextSubscription.getText().toString().trim();
        String area = editTextArea.getText().toString().trim();

        // Create a Customer object with updated values
        // Use customerId directly as it holds the ID of the customer being updated
        Customer updatedCustomer = new Customer(customerId, name, address, phone, subscription, area, null);

        // Update the customer document in Firestore
        db.collection("customers").document(customerId)
                .update("name", name,
                        "address", address,
                        "phone", phone,
                        "subscription", subscription,
                        "area", area)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditCustomerActivity.this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditCustomerActivity.this, "Error updating customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }}
