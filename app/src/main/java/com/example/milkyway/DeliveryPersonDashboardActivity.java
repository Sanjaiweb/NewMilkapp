package com.example.milkyway;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPersonDashboardActivity extends AppCompatActivity {

    private static final String TAG = "DeliveryPersonDashboard";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView textView;
    private RecyclerView recyclerViewCustomers;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_person_dashboard);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Retrieve the TextView and RecyclerView
        textView = findViewById(R.id.textViewName);
        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);

        // Set up RecyclerView
        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(this, customerList);
        recyclerViewCustomers.setAdapter(customerAdapter);

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Retrieve the user's ID
            String userId = currentUser.getUid();

            // Fetch the username and assigned customers from Firestore
            fetchUserData(userId);
        } else {
            textView.setText("User not logged in");
        }
    }

    private void fetchUserData(String userId) {
        db.collection("deliveryPersons").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String username = document.getString("name");
                            List<String> assignedCustomers = (List<String>) document.get("assignedCustomers");

                            if (username != null) {
                                textView.setText("Welcome, " + username);
                            } else {
                                textView.setText("Username not found");
                            }

                            // Fetch assigned customers' details if available
                            if (assignedCustomers != null && !assignedCustomers.isEmpty()) {
                                fetchAssignedCustomers(assignedCustomers);
                            } else {
                                textView.append("\nNo customers assigned.");
                            }

                        } else {
                            textView.setText("User document not found");
                        }
                    } else {
                        Log.e(TAG, "Error fetching username", task.getException());
                        textView.setText("Failed to load username");
                    }
                });
    }

    private void fetchAssignedCustomers(List<String> assignedCustomers) {
        for (String customerId : assignedCustomers) {
            db.collection("customers").document(customerId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String customerName = documentSnapshot.getString("name");
                            String customerAddress = documentSnapshot.getString("address");
                            String customerPhone = documentSnapshot.getString("phone");
                            String customerSubscription = documentSnapshot.getString("subscription");
                            String customerArea = documentSnapshot.getString("area");
                            String shopId = documentSnapshot.getString("shopId");

                            // Create a new Customer object with all the data
                            Customer customer = new Customer(customerId, customerName, customerAddress, customerPhone, customerSubscription, customerArea, shopId);
                            // Add each customer to the list and notify the adapter
                            customerList.add(customer);
                            customerAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching customer: " + customerId, e);
                    });
        }
    }
}
