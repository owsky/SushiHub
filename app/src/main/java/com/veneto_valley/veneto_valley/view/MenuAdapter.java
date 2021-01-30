package com.veneto_valley.veneto_valley.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Dish;

public class MenuAdapter extends ListAdapter<Dish, MenuAdapter.MenuViewHolder> {

    public MenuAdapter() {
        super(new DiffUtil.ItemCallback<Dish>() {
            @Override
            public boolean areItemsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
                return oldItem.name.equals(newItem.name);
            }
        });
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.dishName.setText(getItem(position).name);
        AffiliatedUserInputPageDirections.ActionAffiliatedUserInputNavToUserInputMenuNav action =
                AffiliatedUserInputPageDirections.actionAffiliatedUserInputNavToUserInputMenuNav(getItem(position));
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private final TextView dishName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.order_item_code);
        }
    }
}
