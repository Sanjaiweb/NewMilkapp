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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDeliveryPersonActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextPassword, editTextArea;
    private Button btnAddDeliveryPerson;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_person);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextArea = findViewById(R.id.editTextArea);
        btnAddDeliveryPerson = findViewById(R.id.btnAddDeliveryPerson);

        // Set up button click listener
        btnAddDeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeliveryPerson();
            }
        });
    }

    private void addDeliveryPerson() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String area = editTextArea.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(area)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 1: Register delivery person in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, get user ID
                            String uid = mAuth.getCurrentUser().getUid();
                            addDeliveryPersonToFirestore(uid, name, phone, email, area);
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(AddDeliveryPersonActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addDeliveryPersonToFirestore(String uid, String name, String phone, String email, String area) {
        // Prepare delivery person data
        Map<String, Object> deliveryPerson = new HashMap<>();
        deliveryPerson.put("uid", uid);  // Link to Firebase Authentication UID
        deliveryPerson.put("name", name);
        deliveryPerson.put("phone", phone);
        deliveryPerson.put("email", email);
        deliveryPerson.put("area", area);

        // Step 2: Add delivery person data to Firestore
        db.collection("deliveryPersons").document(uid)
                .set(deliveryPerson)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddDeliveryPersonActivity.this, "Delivery person added successfully.", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(AddDeliveryPersonActivity.this, "Failed to add delivery person.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearFields() {
        editTextName.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextArea.setText("");
    }
}
