package com.example.milkyway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerDetailsActivity extends AppCompatActivity {

    private TextView customerName, customerAddress, subscriptionStatus;
    private Button callButton, messageButton, updateStatusButton, backButton;
    private SwitchCompat deliveryStatusSwitch;
    private ProgressBar progressBar; // ProgressBar for loading indicator

    private DatabaseReference customerRef;
    private String customerId, customerPhone;
    private boolean isDelivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        // Initialize UI components
        customerName         = findViewById(R.id.customerName);
        customerAddress      = findViewById(R.id.customerAddress);
        subscriptionStatus   = findViewById(R.id.subscriptionStatus);
        callButton           = findViewById(R.id.callButton);
        messageButton        = findViewById(R.id.messageButton);
        deliveryStatusSwitch = findViewById(R.id.deliveryStatusSwitch);
        updateStatusButton   = findViewById(R.id.updateStatusButton);
        backButton           = findViewById(R.id.backButton);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar); // Add this to your layout XML
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator

        // Retrieve the customer ID from intent
        customerId = getIntent().getStringExtra("customerId");

        // Initialize Firebase reference
        customerRef = FirebaseDatabase.getInstance().getReference("customers").child(customerId);

        // Load customer data
        loadCustomerData();

        // Set up listeners for buttons with animations
        callButton.setOnClickListener(v -> {
            animateButtonClick(v);
            makeCall();
        });

        messageButton.setOnClickListener(v -> {
            animateButtonClick(v);
            sendMessage();
        });

        updateStatusButton.setOnClickListener(v -> {
            animateButtonClick(v);
            updateDeliveryStatus();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void loadCustomerData() {
        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    customerPhone = snapshot.child("phone").getValue(String.class);
                    boolean subscriptionActive = snapshot.child("subscriptionActive").getValue(Boolean.class);
                    isDelivered = snapshot.child("isDelivered").getValue(Boolean.class);

                    // Set values to UI components
                    customerName.setText(name);
                    customerAddress.setText(address);
                    subscriptionStatus.setText(subscriptionActive ? "Subscription: Active" : "Subscription: Inactive");
                    deliveryStatusSwitch.setChecked(isDelivered);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                Toast.makeText(CustomerDetailsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeCall() {
        if (isPhoneNumberValid(customerPhone)) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + customerPhone));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, "Invalid customer phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage() {
        if (isPhoneNumberValid(customerPhone)) {
            Intent messageIntent = new Intent(Intent.ACTION_VIEW);
            messageIntent.setData(Uri.parse("sms:" + customerPhone));
            startActivity(messageIntent);
        } else {
            Toast.makeText(this, "Invalid customer phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDeliveryStatus() {
        isDelivered = deliveryStatusSwitch.isChecked();
        customerRef.child("isDelivered").setValue(isDelivered)
                .addOnSuccessListener(aVoid -> Toast.makeText(CustomerDetailsActivity.this, "Status updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(CustomerDetailsActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }

    // Validate phone number format
    private boolean isPhoneNumberValid(String phone) {
        return phone != null && phone.matches("\\d{10}"); // Example regex for a 10-digit number
    }

    // Animate button click with a fade-out and fade-in effect
    private void animateButtonClick(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(150);
        view.startAnimation(alphaAnimation);
    }
}
