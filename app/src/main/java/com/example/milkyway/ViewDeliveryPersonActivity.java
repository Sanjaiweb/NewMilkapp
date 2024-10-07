package com.example.milkyway;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewDeliveryPersonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryPersonAdapter adapter;
    private FirebaseFirestore db;
    private String shopId;
    private List<DeliveryPerson> deliveryPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivery_person);

        recyclerView = findViewById(R.id.recyclerViewDeliveryPersons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        shopId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        deliveryPersonList = new ArrayList<>();
        adapter = new DeliveryPersonAdapter(deliveryPersonList);
        recyclerView.setAdapter(adapter);

        loadDeliveryPersons();
    }

    private void loadDeliveryPersons() {
        CollectionReference deliveryPersonsRef = db.collection("shops").document(shopId).collection("deliveryPersons");

        deliveryPersonsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    deliveryPersonList.clear(); // Clear the list before adding new data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DeliveryPerson deliveryPerson = document.toObject(DeliveryPerson.class);
                        deliveryPerson.setId(document.getId()); // Set document ID if needed
                        deliveryPersonList.add(deliveryPerson);
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter to refresh the list
                } else {
                    Toast.makeText(ViewDeliveryPersonActivity.this, "Failed to load delivery persons", Toast.LENGTH_SHORT).show();
                    Log.e("ViewDeliveryPerson", "Error retrieving documents: ", task.getException());
                }
            }
        });
    }

}
