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
import com.veneto_valley.veneto_valley.model.entities.Restaurant;

public class ListRestaurantsAdapter extends ListAdapter<Restaurant, ListRestaurantsAdapter.RistoranteViewHolder> {
	
	public ListRestaurantsAdapter() {
		super(new DiffUtil.ItemCallback<Restaurant>() {
			@Override
			public boolean areItemsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
				return oldItem.id.equals(newItem.id);
			}
			
			@Override
			public boolean areContentsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
				return oldItem.name.equals(newItem.name) &&
						oldItem.address.equals(newItem.address) &&
						oldItem.city.equals(newItem.city);
			}
		});
	}
	
	@NonNull
	@Override
	public RistoranteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.restaurant_item, parent, false);
		return new RistoranteViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull final RistoranteViewHolder holder, int position) {
		Restaurant curr = getItem(position);
		holder.restaurant.setText(curr.name);
		holder.address.setText(curr.address);
		
		ListRestaurantsPageDirections.ActionListRestaurantsNavToConfigureTableNav action =
				ListRestaurantsPageDirections.actionListRestaurantsNavToConfigureTableNav();
		action.setRestaurant(curr);
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public static class RistoranteViewHolder extends RecyclerView.ViewHolder {
		public final TextView restaurant, address;
		
		public RistoranteViewHolder(View view) {
			super(view);
			restaurant = view.findViewById(R.id.restaurant_item_name);
			address = view.findViewById(R.id.restaurant_item_address);
		}
	}
}