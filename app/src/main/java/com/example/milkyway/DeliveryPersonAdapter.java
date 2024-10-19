package com.example.milkyway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DeliveryPersonAdapter extends RecyclerView.Adapter<DeliveryPersonAdapter.DeliveryPersonViewHolder> {

    private ArrayList<DeliveryPerson> deliveryPersonList;

    public DeliveryPersonAdapter(ArrayList<DeliveryPerson> deliveryPersonList) {
        this.deliveryPersonList = deliveryPersonList;
    }

    @NonNull
    @Override
    public DeliveryPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_person, parent, false);
        return new DeliveryPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryPersonViewHolder holder, int position) {
        DeliveryPerson deliveryPerson = deliveryPersonList.get(position);
        holder.nameTextView.setText(deliveryPerson.getName());
        holder.statusTextView.setText(deliveryPerson.getStatus()); // Display status if needed
    }

    @Override
    public int getItemCount() {
        return deliveryPersonList.size();
    }

    static class DeliveryPersonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView statusTextView;

        public DeliveryPersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.deliveryPersonName);
            statusTextView = itemView.findViewById(R.id.deliveryPersonStatus);
        }
    }
}
