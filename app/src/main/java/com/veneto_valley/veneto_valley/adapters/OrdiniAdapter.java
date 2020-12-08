package com.veneto_valley.veneto_valley.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.veneto_valley.veneto_valley.Ordine;
import com.veneto_valley.veneto_valley.R;

import java.util.List;
import java.util.Locale;

public class OrdiniAdapter extends RecyclerView.Adapter<OrdiniAdapter.ItemViewHolder> {
	private final List<Ordine> listaOrdini;
	private final Activity activity;
	private final OnOrderClickListener onOrderClickListener;
	private final OnDragStartListener dragStartListener;
	private Ordine cancellato;
	private int indiceCancellato;
	private Snackbar snackbar;
	
	public OrdiniAdapter(Activity activity, List<Ordine> listaOrdini, OnOrderClickListener onOrderClickListener, OnDragStartListener dragStartListener) {
		this.activity = activity;
		this.listaOrdini = listaOrdini;
		this.onOrderClickListener = onOrderClickListener;
		this.dragStartListener = dragStartListener;
	}
	
	@NonNull
	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
		return new ItemViewHolder(view, onOrderClickListener);
	}
	
	@Override
	public void onBindViewHolder(final ItemViewHolder holder, int position) {
		holder.codice.setText(listaOrdini.get(position).codice);
		String desc = listaOrdini.get(position).descrizione;
		if (desc.length() >= 19)
			desc = desc.substring(0, 16) + "...";
		holder.descrizione.setText(desc);
		holder.quantita.setText(String.format(Locale.ITALY, "%d", listaOrdini.get(position).quantita));
		holder.handleView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
					dragStartListener.onDragStarted(holder);
				}
				return false;
			}
		});
	}
	
	public void deleteItem(int position) {
		cancellato = listaOrdini.get(position);
		indiceCancellato = position;
		listaOrdini.remove(position);
		notifyItemRemoved(position);
		View view = activity.findViewById(R.id.pendingOrders);
		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo delete?", v -> undoDelete());
		snackbar.show();
	}
	
	@Override
	public int getItemCount() {
		return listaOrdini.size();
	}
	
	public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private final OnOrderClickListener onOrderClickListener;
		private final TextView codice, descrizione, quantita;
		public final ImageView handleView;
		
		public ItemViewHolder(View itemView, OnOrderClickListener onOrderClickListener) {
			super(itemView);
			this.onOrderClickListener = onOrderClickListener;
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			quantita = itemView.findViewById(R.id.piattoQuantita);
			handleView = itemView.findViewById(R.id.dragHandle);

			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v) {
			onOrderClickListener.onOrderClick(getAdapterPosition());
		}
	}
	
	public void sendItem(int position) {
		//TODO comunicazione master
		cancellato = listaOrdini.get(position);
		indiceCancellato = position;
		listaOrdini.remove(position);
		notifyItemRemoved(position);
		View view = activity.findViewById(R.id.pendingOrders);
		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo send?", v -> undoSend());
		snackbar.show();
	}
	
	private void undoDelete() {
		listaOrdini.add(indiceCancellato, cancellato);
		notifyItemInserted(indiceCancellato);
	}
	
	private void undoSend() {
		// TODO undo master
		listaOrdini.add(indiceCancellato, cancellato);
		notifyItemInserted(indiceCancellato);
	}
	
	public interface OnOrderClickListener {
		void onOrderClick(int position);
	}
	
	public interface OnDragStartListener {
		void onDragStarted(RecyclerView.ViewHolder viewHolder);
	}
}