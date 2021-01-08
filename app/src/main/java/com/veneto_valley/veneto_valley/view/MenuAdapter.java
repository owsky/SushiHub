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

public class MenuAdapter extends ListAdapter<String, MenuAdapter.MenuViewHolder> {
	
	public MenuAdapter() {
		super(new DiffUtil.ItemCallback<String>() {
			@Override
			public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
				return oldItem.equals(newItem);
			}
			
			@Override
			public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
				return oldItem.equals(newItem);
			}
		});
	}
	
	@NonNull
	@Override
	public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_menu, parent, false);
		return new MenuViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
		holder.nomePiatto.setText(getItem(position));
		AggiuntaOrdineMenuDirections.ActionAggiuntaOrdineNavToUserInputNav action =
				AggiuntaOrdineMenuDirections.actionAggiuntaOrdineNavToUserInputNav();
		action.setCodicePiatto(getItem(position));
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public static class MenuViewHolder extends RecyclerView.ViewHolder {
		private final TextView nomePiatto;
		
		public MenuViewHolder(@NonNull View itemView) {
			super(itemView);
			nomePiatto = itemView.findViewById(R.id.piattoCodice);
		}
	}
}
