package com.example.milkyway;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddDeliveryPersonActivity extends AppCompatActivity {


    private EditText editTextName, editTextPhone, editTextEmail, editTextPassword, editTextArea;
    private Button btnAddDeliveryPerson;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String shopId; // Retrieved from shop owner's account or session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_person);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve the shop ID from the shop owner's account (placeholder logic)
        FirebaseUser currentUser = auth.getCurrentUser();
        shopId = currentUser != null ? currentUser.getUid() : "defaultShopId"; // Retrieve actual shop ID


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
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String area = editTextArea.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || area.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register the delivery person in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String deliveryPersonId = task.getResult().getUser().getUid();

                        // Prepare delivery person data
                        Map<String, Object> deliveryPerson = new HashMap<>();
                        deliveryPerson.put("name", name);
                        deliveryPerson.put("phone", phone);
                        deliveryPerson.put("email", email);
                        deliveryPerson.put("area", area);
                        deliveryPerson.put("shopId", shopId);

                        // Add delivery person data to Firestore under the shop's collection

                        db.collection("deliveryPersons").document(deliveryPersonId)
                                .set(deliveryPerson)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddDeliveryPersonActivity.this, "Delivery person added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(AddDeliveryPersonActivity.this, "Error adding delivery person: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(AddDeliveryPersonActivity.this, "Error creating account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
