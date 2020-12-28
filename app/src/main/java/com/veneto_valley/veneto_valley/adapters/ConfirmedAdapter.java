package com.veneto_valley.veneto_valley.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.util.List;
import java.util.Locale;

public class ConfirmedAdapter extends RecyclerView.Adapter<ConfirmedAdapter.ConfirmedViewHolder> {
	private final List<Ordine> dataList;
	private final Locale locale;
	
	public ConfirmedAdapter(Activity activity, List<Ordine> dataList) {
		this.dataList = dataList;
		locale = activity.getResources().getConfiguration().locale;
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public ConfirmedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
		return new ConfirmedViewHolder(view);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(@NonNull ConfirmedViewHolder holder, int position) {
		Ordine ordine = dataList.get(position);
		// TODO sostituire idordine con idpiatto
		holder.codice.setText(String.format(locale, "%d", ordine.idOrdine));
		String desc = ordine.desc;
		if (desc.length() >= 19) // TODO verificare se scala correttamente
			desc = desc.substring(0, 16) + "...";
		holder.descrizione.setText(desc);
		// TODO fixare quantità
		holder.quantita.setText(String.format(locale, "%d", ordine.quantita));
	}
	
	@Override
	public int getItemCount() {
		return dataList.size();
	}
	
	public void annullaOrdine() {
		// TODO undo ordine
	}
	
	public static class ConfirmedViewHolder extends RecyclerView.ViewHolder {
		private final TextView codice, descrizione, quantita;
		
		public ConfirmedViewHolder(@NonNull View itemView) {
			super(itemView);
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			quantita = itemView.findViewById(R.id.piattoQuantita);
			ImageView handle = itemView.findViewById(R.id.dragHandle);
			handle.setVisibility(View.GONE);
		}
	}
}
