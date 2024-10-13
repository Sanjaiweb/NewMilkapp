package com.example.milkyway;

import android.content.Context; // Add this import
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
    private Context context; // Use a generic Context instead of a specific activity

    // Constructor to initialize the customer list and context
    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context; // Store the context reference
        this.customerList = customerList; // Initialize the customer list
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_customer layout for each customer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        // Get the current customer
        Customer customer = customerList.get(position);

        // Bind customer data to the views
        holder.textViewName.setText(customer.getName());
        holder.textViewAddress.setText(customer.getAddress());
        holder.textViewPhone.setText(customer.getPhone());
        holder.textViewSubscription.setText(customer.getSubscription());
        holder.textViewArea.setText(customer.getArea());

        // Set click listener to open EditCustomerActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCustomerActivity.class); // Use context
            intent.putExtra("customerId", customer.getId()); // Pass customer ID to the EditCustomerActivity
            context.startActivity(intent); // Use context to start the activity
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the customer list
        return customerList.size();
    }

    // ViewHolder class to hold customer item views
    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewPhone, textViewSubscription, textViewArea;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews from the layout
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewSubscription = itemView.findViewById(R.id.textViewSubscription);
            textViewArea = itemView.findViewById(R.id.textViewArea);
        }
    }
}
