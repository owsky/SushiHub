package com.veneto_valley.veneto_valley.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.veneto_valley.veneto_valley.Connessione;
import com.veneto_valley.veneto_valley.ListPiattiFragmentDirections;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder> {
	private final List<Ordine> dataList;
	private final CustomDragListener customDragListener;
	private Ordine cancellato;
	private int indiceCancellato;
	private final Activity activity;
	private Snackbar snackbar;
	private final Locale locale;
	
	public PendingAdapter(Activity activity, List<Ordine> dataList, CustomDragListener listener) {
		this.activity = activity;
		this.dataList = dataList;
		this.customDragListener = listener;
		this.locale = activity.getResources().getConfiguration().locale;
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
		return new PendingViewHolder(view);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
		Ordine ordine = dataList.get(position);
		// TODO sostituire idordine con idpiatto
		holder.codice.setText(String.format(locale, "%d", ordine.idOrdine));
		String desc = ordine.desc;
		if (desc.length() >= 19) // TODO verificare se scala correttamente
			desc = desc.substring(0, 16) + "...";
		holder.descrizione.setText(desc);
		// TODO fixare quantità
		holder.quantita.setText(String.format(locale, "%d", ordine.quantita));
		holder.handle.setOnTouchListener((v, event) -> {
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				customDragListener.onDragStarted(holder);
			return true;
		});
		ListPiattiFragmentDirections.ActionListaPiattiFragmentToModificaOrdineFragment action = ListPiattiFragmentDirections.actionListaPiattiFragmentToModificaOrdineFragment(Integer.parseInt(holder.codice.getText().toString()));
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	@Override
	public int getItemCount() {
		return dataList.size();
	}
	
	public void aggiungiOrdine(Ordine ordine) {
		AppDatabase database = AppDatabase.getInstance(activity);
		database.ordineDao().insertAll(ordine);
		// TODO ottimizzare, resetta l'ordine ad ogni modifica
		dataList.clear();
		// TODO sostituire getAll con getAllByStatus
		dataList.addAll(database.ordineDao().getAll());
		notifyDataSetChanged();
	}
	
	public void sendItem(int position) throws IOException {
		//TODO Done comunicazione master
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		Connessione c = new Connessione(false, activity, codiceTavolo);
		c.invia(dataList.get(position).getBytes());

		cancellato = dataList.get(position);
		indiceCancellato = position;
		dataList.remove(position);
		notifyItemRemoved(position);
		View view = activity.findViewById(R.id.pendingOrders);
		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo send?", v -> undoSend());
		snackbar.show();
	}
	
	public void deleteItem(int position) {
		cancellato = dataList.get(position);
		indiceCancellato = position;
		dataList.remove(position);
		notifyItemRemoved(position);
		View view = activity.findViewById(R.id.listaPiattiFragment);
		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo delete?", v -> undoDelete());
		snackbar.show();
	}
	
	public void retrieveFromConfirmed(int position) {
	}
	
	public void retrieveFromDelivered(int position) {
	}
	
	private void undoSend() {
		// TODO undo master
		dataList.add(indiceCancellato, cancellato);
		notifyItemInserted(indiceCancellato);
	}
	
	private void undoDelete() {
		dataList.add(indiceCancellato, cancellato);
		notifyItemInserted(indiceCancellato);
	}
	
	private void undoDelivered() {
	}
	
	private void undoConfirmed() {
	}
	
	public static class PendingViewHolder extends RecyclerView.ViewHolder {
		private final TextView codice, descrizione, quantita;
		private final ImageView handle;
		
		public PendingViewHolder(@NonNull View itemView) {
			super(itemView);
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			quantita = itemView.findViewById(R.id.piattoQuantita);
			handle = itemView.findViewById(R.id.dragHandle);
		}
	}
	
	public interface CustomDragListener {
		void onDragStarted(RecyclerView.ViewHolder viewHolder);
	}
}
