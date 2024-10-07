package com.example.milkyway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryPersonAdapter extends RecyclerView.Adapter<DeliveryPersonAdapter.DeliveryPersonViewHolder> {

    private List<DeliveryPerson> deliveryPersonList;

    public DeliveryPersonAdapter(List<DeliveryPerson> deliveryPersonList) {
        this.deliveryPersonList = deliveryPersonList;
    }

    @NonNull
    @Override
    public DeliveryPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_person, parent, false);
        return new DeliveryPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryPersonViewHolder holder, int position) {
        DeliveryPerson deliveryPerson = deliveryPersonList.get(position);
        holder.textViewName.setText(deliveryPerson.getName());
        holder.textViewPhone.setText(deliveryPerson.getPhone());
        holder.textViewEmail.setText(deliveryPerson.getEmail());
        holder.textViewArea.setText(deliveryPerson.getArea());
    }

    @Override
    public int getItemCount() {
        return deliveryPersonList.size();
    }

    static class DeliveryPersonViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhone, textViewEmail, textViewArea;

        public DeliveryPersonViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewArea = itemView.findViewById(R.id.textViewArea);
        }
    }
}
