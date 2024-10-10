package com.example.milkyway;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewDeliveryPersonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryPersonAdapter adapter;
    private FirebaseFirestore db;
    private String shopId;
    private FirebaseAuth auth;
    private List<DeliveryPerson> deliveryPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivery_person);

        recyclerView = findViewById(R.id.recyclerViewDeliveryPersons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        deliveryPersonList = new ArrayList<>();
        adapter = new DeliveryPersonAdapter(deliveryPersonList);
        recyclerView.setAdapter(adapter);

        if (auth.getCurrentUser() != null) {
            shopId = auth.getCurrentUser().getUid();
            Log.d("ViewDeliveryPerson", "Current Shop ID: " + shopId);
            loadDeliveryPersons();
        } else {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadDeliveryPersons() {
        db.collection("deliveryPersons")
                .whereEqualTo("shopId", shopId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DeliveryPerson deliveryPerson = document.toObject(DeliveryPerson.class);
                            deliveryPerson.setId(document.getId());
                            deliveryPersonList.add(deliveryPerson);
                            Log.d("DeliveryPerson", "Found delivery person: " + deliveryPerson.getName());
                        }
                        if (deliveryPersonList.isEmpty()) {
                            Log.d("DeliveryPerson", "No delivery persons found for shopId: " + shopId);
                            Toast.makeText(ViewDeliveryPersonActivity.this, "No delivery persons found", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ViewDeliveryPersonActivity.this, "Failed to load delivery persons", Toast.LENGTH_SHORT).show();
                        Log.e("ViewDeliveryPerson", "Error retrieving documents: ", task.getException());
                    }
                });
    }

}
