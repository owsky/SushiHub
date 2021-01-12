package com.veneto_valley.veneto_valley.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

public class StoricoDettagliAdapter extends ListAdapter<Ordine, StoricoDettagliAdapter.StoricoDettagliViewHolder> {

	// callback che consente al listadapter di confrontare gli elementi della lista
	private static final DiffUtil.ItemCallback<Ordine> DIFF_CALLBACK = new DiffUtil.ItemCallback<Ordine>() {
		@Override
		public boolean areItemsTheSame(@NonNull Ordine oldItem, @NonNull Ordine newItem) {
			return oldItem.idOrdine == newItem.idOrdine;
		}
		
		@Override
		public boolean areContentsTheSame(@NonNull Ordine oldItem, @NonNull Ordine newItem) {
			return oldItem.piatto.equals(newItem.piatto) &&
					oldItem.desc.equals(newItem.desc) &&
					oldItem.quantita == newItem.quantita;
		}
	};
	
	public StoricoDettagliAdapter() {
		super(DIFF_CALLBACK);
	}
	
	@NonNull
	@Override
	public StoricoDettagliViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_ordine, parent, false);
		return new StoricoDettagliViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull StoricoDettagliViewHolder holder, int position) {
		Ordine currentOrdine = getItem(position);
		holder.codice.setText(String.valueOf(currentOrdine.piatto));
		String desc = currentOrdine.desc;
		if (desc != null)
			holder.descrizione.setText(desc);
	}
	
	public static class StoricoDettagliViewHolder extends RecyclerView.ViewHolder {
		private final TextView codice, descrizione;
		
		public StoricoDettagliViewHolder(@NonNull View itemView) {
			super(itemView);
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
		}
	}
}
