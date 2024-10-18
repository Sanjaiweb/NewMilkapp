package com.example.milkyway;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText editTextCustomerName, editTextCustomerAddress, editTextCustomerPhone, editTextSubscription, editTextArea;
    private Spinner spinnerDeliveryPerson;
    private Button btnAddCustomer;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String shopId;
    private ArrayAdapter<String> deliveryPersonAdapter;
    private List<String> deliveryPersonIds = new ArrayList<>();

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
        spinnerDeliveryPerson = findViewById(R.id.spinnerDeliveryPerson);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);

        // Set up ArrayAdapter for spinner
        deliveryPersonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        deliveryPersonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeliveryPerson.setAdapter(deliveryPersonAdapter);

        // Load delivery persons from Firestore
        db.collection("deliveryPersons")
                .whereEqualTo("shopId", shopId)  // Only delivery persons from this shop
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String id = doc.getId();
                            deliveryPersonAdapter.add(name);  // Add name to spinner
                            deliveryPersonIds.add(id);  // Store ID for later use
                        }
                        deliveryPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddCustomerActivity.this, "Failed to load delivery persons", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up button click listener
        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });
    }



    // In the addCustomer() method
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

        // Get selected delivery person ID
        int selectedIndex = spinnerDeliveryPerson.getSelectedItemPosition();
        if (selectedIndex == -1) {
            Toast.makeText(this, "Please select a delivery person", Toast.LENGTH_SHORT).show();
            return;
        }
        String deliveryPersonId = deliveryPersonIds.get(selectedIndex);

        // Create customer data map including the shopId and deliveryPersonId
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("address", address);
        customer.put("phone", phone);
        customer.put("subscription", subscription);
        customer.put("area", area);
        customer.put("shopId", shopId);  // Associate customer with the shop
        customer.put("deliveryPersonId", deliveryPersonId);  // Associate delivery person with customer

        // Add customer data to Firestore under the "customers" collection
        db.collection("customers")
                .add(customer)
                .addOnSuccessListener(documentReference -> {
                    String customerId = documentReference.getId(); // Get the new customer ID

                    // Now add this customer ID to the delivery person's assignedCustomers array
                    db.collection("deliveryPersons").document(deliveryPersonId)
                            .update("assignedCustomers", FieldValue.arrayUnion(customerId))
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AddCustomerActivity.this, "Customer added and assigned successfully", Toast.LENGTH_SHORT).show();
                                clearFields();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddCustomerActivity.this, "Failed to assign customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(AddCustomerActivity.this, "Failed to add customer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void clearFields() {
        editTextCustomerName.setText("");
        editTextCustomerAddress.setText("");
        editTextCustomerPhone.setText("");
        editTextSubscription.setText("");
        editTextArea.setText("");
        spinnerDeliveryPerson.setSelection(0);
    }
}
