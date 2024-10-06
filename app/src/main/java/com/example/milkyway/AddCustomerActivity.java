package com.example.milkyway;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText editTextCustomerName, editTextCustomerAddress, editTextCustomerPhone, editTextSubscription, editTextArea;
    private Button btnAddCustomer;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

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

        // Initialize fields
        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        editTextCustomerAddress = findViewById(R.id.editTextCustomerAddress);
        editTextCustomerPhone = findViewById(R.id.editTextCustomerPhone);
        editTextSubscription = findViewById(R.id.editTextSubscription);
        editTextArea = findViewById(R.id.editTextArea);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);

        // Set up button click listener
        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });
    }

    private void addCustomer() {
        String name = editTextCustomerName.getText().toString().trim();
        String address = editTextCustomerAddress.getText().toString().trim();
        String phone = editTextCustomerPhone.getText().toString().trim();
        String subscription = editTextSubscription.getText().toString().trim();
        String area = editTextArea.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(subscription) || TextUtils.isEmpty(area)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create customer data map including the shopId
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("address", address);
        customer.put("phone", phone);
        customer.put("subscription", subscription);
        customer.put("area", area);
        customer.put("shopId", shopId);  // Associate customer with the shop

        // Add customer data to Firestore under the "customers" collection
        db.collection("customers")
                .add(customer)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddCustomerActivity.this, "Customer added successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCustomerActivity.this, "Failed to add customer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        editTextCustomerName.setText("");
        editTextCustomerAddress.setText("");
        editTextCustomerPhone.setText("");
        editTextSubscription.setText("");
        editTextArea.setText("");
    }
}
