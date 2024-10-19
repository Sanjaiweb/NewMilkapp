package com.example.milkyway;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.milkyway.databinding.ItemDisplayCustomerBinding;
import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList;
    private final List<Customer> originalList;
    private final Context context;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList != null ? customerList : new ArrayList<>();
        this.originalList = new ArrayList<>(this.customerList);
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDisplayCustomerBinding binding = ItemDisplayCustomerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomerDetailsActivity.class);
            intent.putExtra("CUSTOMER_ID", customer.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void filterList(String query) {
        List<Customer> filteredList;
        if (query.isEmpty()) {
            filteredList = new ArrayList<>(originalList);
        } else {
            filteredList = new ArrayList<>();
            for (Customer customer : originalList) {
                if (customer.getName().toLowerCase().contains(query.toLowerCase()) ||
                        customer.getPhone().contains(query)) {
                    filteredList.add(customer);
                }
            }
        }

        updateCustomerList(filteredList);
    }

    private void updateCustomerList(List<Customer> newCustomerList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CustomerDiffCallback(this.customerList, newCustomerList));
        this.customerList = new ArrayList<>(newCustomerList);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final ItemDisplayCustomerBinding binding;

        public CustomerViewHolder(ItemDisplayCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Customer customer) {
            binding.textViewName.setText(customer.getName());
            binding.textViewAddress.setText(customer.getAddress());

        }
    }

    private static class CustomerDiffCallback extends DiffUtil.Callback {
        private final List<Customer> oldList;
        private final List<Customer> newList;

        public CustomerDiffCallback(List<Customer> oldList, List<Customer> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
