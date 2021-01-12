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
import com.veneto_valley.veneto_valley.model.entities.Piatto;

public class MenuAdapter extends ListAdapter<Piatto, MenuAdapter.MenuViewHolder> {
	
	public MenuAdapter() {
		super(new DiffUtil.ItemCallback<Piatto>() {
			@Override
			public boolean areItemsTheSame(@NonNull Piatto oldItem, @NonNull Piatto newItem) {
				return oldItem.idPiatto.equals(newItem.idPiatto);
			}
			
			@Override
			public boolean areContentsTheSame(@NonNull Piatto oldItem, @NonNull Piatto newItem) {
				return oldItem.nome.equals(newItem.nome);
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
		holder.nomePiatto.setText(getItem(position).nome);
		MenuAggiuntaOrdineDirections.ActionAggiuntaOrdineNavToUserInputMenu action =
				MenuAggiuntaOrdineDirections.actionAggiuntaOrdineNavToUserInputMenu();
		action.setPiatto(getItem(position));
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
