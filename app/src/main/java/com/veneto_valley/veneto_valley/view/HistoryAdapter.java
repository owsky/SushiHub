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
import com.veneto_valley.veneto_valley.model.entities.Table;

public class HistoryAdapter extends ListAdapter<Table, HistoryAdapter.TableViewHolder> {
	
	private static final DiffUtil.ItemCallback<Table> DIFF_CALLBACK = new DiffUtil.ItemCallback<Table>() {
		@Override
		public boolean areItemsTheSame(@NonNull Table oldItem, @NonNull Table newItem) {
			return oldItem.id.equals(newItem.id);
		}
		
		@Override
		public boolean areContentsTheSame(@NonNull Table oldItem, @NonNull Table newItem) {
			return oldItem.date.equals(newItem.date) &&
					oldItem.restaurant.equals(newItem.restaurant) &&
					oldItem.menuPrice == newItem.menuPrice;
		}
	};
	
	protected HistoryAdapter() {
		super(DIFF_CALLBACK);
	}
	
	@NonNull
	@Override
	public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
		return new TableViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
		Table table = getItem(position);
		holder.restaurant.setText(table.name);
		holder.data.setText(table.date.toString());
		
		// it creates a click listener that handles navigation with the proper safeargs
		ListOrdersPageDirections.ActionHistoryToHistoryDetailsNav action =
				ListOrdersPageDirections.actionHistoryToHistoryDetailsNav(table);
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public static class TableViewHolder extends RecyclerView.ViewHolder {
		private final TextView restaurant, data;
		
		public TableViewHolder(@NonNull View itemView) {
			super(itemView);
			restaurant = itemView.findViewById(R.id.history_item_name);
			data = itemView.findViewById(R.id.history_item_date);
		}
	}
}
