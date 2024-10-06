package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

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
                        editTextName.setText(customer.getName());
                        editTextAddress.setText(customer.getAddress());
                        editTextPhone.setText(customer.getPhone());
                        editTextSubscription.setText(customer.getSubscription());
                        editTextArea.setText(customer.getArea());
                    } else {
                        Toast.makeText(EditCustomerActivity.this, "Customer not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(EditCustomerActivity.this, "Error loading customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateCustomer() {
        String name = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        String phone = editTextPhone.getText().toString();
        String subscription = editTextSubscription.getText().toString();
        String area = editTextArea.getText().toString();

        // Create a Customer object with updated values
        Customer updatedCustomer = new Customer(name, address, phone, subscription, area, null); // Keep shopId as null since we don't want to update it.

        db.collection("customers").document(customerId)
                .set(updatedCustomer)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditCustomerActivity.this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditCustomerActivity.this, "Error updating customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
