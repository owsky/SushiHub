package com.veneto_valley.veneto_valley.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.Ordine;
import com.veneto_valley.veneto_valley.R;

import java.util.List;

public class OrdiniAdapter extends RecyclerView.Adapter<OrdiniAdapter.OrdiniViewHolder> {
	private final List<Ordine> listaOrdini;
	private final Context context;
	private final OnOrderClickListener onOrderClickListener;
	
	public OrdiniAdapter(Context context, List<Ordine> listaOrdini, OnOrderClickListener onOrderClickListener) {
		this.context = context;
		this.listaOrdini = listaOrdini;
		this.onOrderClickListener = onOrderClickListener;
	}
	
	@NonNull
	@Override
	public OrdiniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.elemento_lista, parent, false);
		return new OrdiniViewHolder(view, onOrderClickListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull OrdiniViewHolder holder, int position) {
		holder.codice.setText(listaOrdini.get(position).codice);
		holder.descrizione.setText(listaOrdini.get(position).descrizione);
	}
	
	@Override
	public int getItemCount() {
		return listaOrdini.size();
	}
	
	public static class OrdiniViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView codice, descrizione;
		OnOrderClickListener onOrderClickListener;
		
		public OrdiniViewHolder(@NonNull View itemView, OnOrderClickListener onOrderClickListener) {
			super(itemView);
			this.onOrderClickListener = onOrderClickListener;
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v) {
			onOrderClickListener.onOrderClick(getAdapterPosition());
		}
	}
	
	public interface OnOrderClickListener {
		void onOrderClick(int position);
	}
}
