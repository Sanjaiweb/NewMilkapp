package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ShopkeeperDashboardActivity extends AppCompatActivity {

    // Declare buttons for each section
    private Button btnAddCustomer, btnViewCustomers, btnEditCustomer, btnDeleteCustomer;
    private Button btnAddDeliveryPerson, btnViewDeliveryPersons, btnEditDeliveryPerson, btnDeleteDeliveryPerson;
    private Button btnGenerateInvoices, btnViewAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);

        // Initialize customer management buttons
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnViewCustomers = findViewById(R.id.btnViewCustomers);
        btnEditCustomer = findViewById(R.id.btnEditCustomer);
        btnDeleteCustomer = findViewById(R.id.btnDeleteCustomer);

        // Initialize delivery management buttons
        btnAddDeliveryPerson = findViewById(R.id.btnAddDeliveryPerson);
        btnViewDeliveryPersons = findViewById(R.id.btnViewDeliveryPersons);
        btnEditDeliveryPerson = findViewById(R.id.btnEditDeliveryPerson);
        btnDeleteDeliveryPerson = findViewById(R.id.btnDeleteDeliveryPerson);

        // Initialize invoices and analytics buttons
        btnGenerateInvoices = findViewById(R.id.btnGenerateInvoices);
        btnViewAnalytics = findViewById(R.id.btnViewAnalytics);

        // Set up button listeners
        setupButtonListeners();
    }

    // Modular method to set up all button click listeners
    private void setupButtonListeners() {
        // Customer Management
        btnAddCustomer.setOnClickListener(view -> openAddCustomerActivity());
        btnViewCustomers.setOnClickListener(view -> openViewCustomersActivity());
        btnEditCustomer.setOnClickListener(view -> openEditCustomerActivity());
        btnDeleteCustomer.setOnClickListener(view -> deleteCustomer());

        // Delivery Management
        btnAddDeliveryPerson.setOnClickListener(view -> openAddDeliveryPersonActivity());
        btnViewDeliveryPersons.setOnClickListener(view -> openViewDeliveryPersonsActivity());
        btnEditDeliveryPerson.setOnClickListener(view -> openEditDeliveryPersonActivity());
        btnDeleteDeliveryPerson.setOnClickListener(view -> deleteDeliveryPerson());

        // Invoices and Analytics
        btnGenerateInvoices.setOnClickListener(view -> generateInvoices());
        btnViewAnalytics.setOnClickListener(view -> viewAnalytics());
    }

    // Methods for Customer Management
    private void openAddCustomerActivity() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivity(intent);
    }

    private void openViewCustomersActivity() {
        Intent intent = new Intent(this, ViewCustomerActivity.class);
        startActivity(intent);
    }

    private void openEditCustomerActivity() {
        Intent intent = new Intent(this, EditCustomerActivity.class);
        startActivity(intent);
    }

    private void deleteCustomer() {
        Toast.makeText(this, "Customer deletion feature not implemented yet", Toast.LENGTH_SHORT).show();
    }

    // Methods for Delivery Management
    private void openAddDeliveryPersonActivity() {
        Intent intent = new Intent(this, AddDeliveryPersonActivity.class);
        startActivity(intent);
    }

    private void openViewDeliveryPersonsActivity() {
        Intent intent = new Intent(this, ViewDeliveryPersonActivity.class);
        startActivity(intent);
    }

    private void openEditDeliveryPersonActivity() {
        Intent intent = new Intent(this, EditDeliveryPersonActivity.class);
        startActivity(intent);
    }

    private void deleteDeliveryPerson() {
        Toast.makeText(this, "Delivery person deletion feature not implemented yet", Toast.LENGTH_SHORT).show();
    }

    // Methods for Invoices and Analytics
    private void generateInvoices() {
        Toast.makeText(this, "Invoice generation feature not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void viewAnalytics() {
        Toast.makeText(this, "Analytics viewing feature not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
