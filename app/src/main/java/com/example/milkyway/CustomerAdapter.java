package com.example.milkyway;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.textViewName.setText(customer.getName());
        holder.textViewAddress.setText(customer.getAddress());
        holder.textViewPhone.setText(customer.getPhone());
        holder.textViewSubscription.setText(customer.getSubscription());
        holder.textViewArea.setText(customer.getArea());

        // Set click listener to open EditCustomerActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), EditCustomerActivity.class);
            intent.putExtra("customerId", customer.getId()); // Make sure you have an 'id' property in your Customer class
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewPhone, textViewSubscription, textViewArea;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewSubscription = itemView.findViewById(R.id.textViewSubscription);
            textViewArea = itemView.findViewById(R.id.textViewArea);
        }
    }
}
