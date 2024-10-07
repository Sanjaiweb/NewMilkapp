package com.example.milkyway;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditDeliveryPersonActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextArea;
    private Button btnUpdateDeliveryPerson;
    private FirebaseFirestore db;
    private String shopId, deliveryPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_person);

        // Initialize Firestore and retrieve the shop and delivery person IDs
        db = FirebaseFirestore.getInstance();
        shopId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deliveryPersonId = getIntent().getStringExtra("deliveryPersonId");

        // Initialize fields
        editTextName = findViewById(R.id.editTextDeliveryPersonName);
        editTextPhone = findViewById(R.id.editTextDeliveryPersonPhone);
        editTextEmail = findViewById(R.id.editTextDeliveryPersonEmail);
        editTextArea = findViewById(R.id.editTextDeliveryPersonArea);
        btnUpdateDeliveryPerson = findViewById(R.id.btnUpdateDeliveryPerson);

        // Load delivery person data
        loadDeliveryPersonData();

        // Set up button click listener
        btnUpdateDeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDeliveryPerson();
            }
        });
    }

    private void loadDeliveryPersonData() {
        db.collection("shops").document(shopId)
                .collection("deliveryPersons").document(deliveryPersonId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        DeliveryPerson deliveryPerson = documentSnapshot.toObject(DeliveryPerson.class);
                        editTextName.setText(deliveryPerson.getName());
                        editTextPhone.setText(deliveryPerson.getPhone());
                        editTextEmail.setText(deliveryPerson.getEmail());
                        editTextArea.setText(deliveryPerson.getArea());
                    } else {
                        Toast.makeText(EditDeliveryPersonActivity.this, "Delivery Person not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(EditDeliveryPersonActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show());
    }

    private void updateDeliveryPerson() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String area = editTextArea.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(area)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", name);
        updatedData.put("phone", phone);
        updatedData.put("email", email);
        updatedData.put("area", area);

        db.collection("shops").document(shopId)
                .collection("deliveryPersons").document(deliveryPersonId)
                .update(updatedData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditDeliveryPersonActivity.this, "Delivery Person updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditDeliveryPersonActivity.this, "Failed to update Delivery Person", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
