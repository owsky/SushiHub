package com.veneto_valley.veneto_valley.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Order;

public class OrdersAdapter extends ListAdapter<Order, OrdersAdapter.OrdersViewHolder> {
    private final ListOrdersPage.ListOrdersType listOrdersType;

    public OrdersAdapter(ListOrdersPage.ListOrdersType listOrdersType) {
        super(new DiffUtil.ItemCallback<Order>() {
            @Override
            public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
                return oldItem.dish.equals(newItem.dish);
            }
        });
        this.listOrdersType = listOrdersType;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrdersViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Order currentOrder = getItem(position);
        holder.code.setText(String.valueOf(currentOrder.dish));
        String desc = currentOrder.desc;
        // it edits the visibility options depending upon the actual view it's used in
        if (listOrdersType == ListOrdersPage.ListOrdersType.allOrders) {
            holder.user.setText(currentOrder.user);
            holder.user.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
        }
        if (desc != null)
            holder.desc.setText(desc);
    }

    public Order getOrderAt(int position) {
        return getItem(position);
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        private final TextView code, desc, user;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.order_item_code);
            desc = itemView.findViewById(R.id.order_item_description);
            user = itemView.findViewById(R.id.order_item_user);
        }
    }
}
