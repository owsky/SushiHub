package com.veneto_valley.veneto_valley.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.veneto_valley.veneto_valley.Ordine;
import com.veneto_valley.veneto_valley.R;

import java.util.List;

public class OrdiniAdapter extends RecyclerView.Adapter<OrdiniAdapter.OrdiniViewHolder> {
	private final List<Ordine> listaOrdini;
	private final Activity activity;
	private final OnOrderClickListener onOrderClickListener;
	private Ordine cancellato;
	private int indiceCancellato;
	
	public OrdiniAdapter(Activity activity, List<Ordine> listaOrdini, OnOrderClickListener onOrderClickListener) {
		this.activity = activity;
		this.listaOrdini = listaOrdini;
		this.onOrderClickListener = onOrderClickListener;
	}
	
	@NonNull
	@Override
	public OrdiniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(activity);
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
	
	public void deleteItem(int position) {
		cancellato = listaOrdini.get(position);
		indiceCancellato = position;
		listaOrdini.remove(position);
		notifyItemRemoved(position);
		showUndoSnackbar();
	}
	
	private void showUndoSnackbar() {
		View view = activity.findViewById(R.id.pendingOrders);
		Snackbar snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo delete?", v -> undoDelete());
		snackbar.show();
	}
	
	private void undoDelete() {
		listaOrdini.add(indiceCancellato, cancellato);
		notifyItemInserted(indiceCancellato);
	}
	
	public interface OnOrderClickListener {
		void onOrderClick(int position);
	}
}
