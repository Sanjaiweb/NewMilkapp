package com.example.milkyway;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCustomers;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

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

        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(this));
        customerList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(customerList);
        recyclerViewCustomers.setAdapter(customerAdapter);

        loadCustomers();
    }

    private void loadCustomers() {
        db.collection("customers")
                .whereEqualTo("shopId", shopId) // Filter customers by shopId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Customer customer = document.toObject(Customer.class);
                            customer.setId(document.getId()); // Set the customer ID
                            customerList.add(customer);
                        }
                        customerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ViewCustomerActivity.this, "Failed to load customers: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
