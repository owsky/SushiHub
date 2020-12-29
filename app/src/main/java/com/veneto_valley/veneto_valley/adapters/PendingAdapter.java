package com.veneto_valley.veneto_valley.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.ListPiattiFragmentDirections;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.io.IOException;

public class PendingAdapter extends ListAdapter<Ordine, PendingAdapter.PendingViewHolder> {
//	private final CustomDragListener customDragListener;
//	private Ordine cancellato;
//	private int indiceCancellato;
//	private Snackbar snackbar;
	
	public PendingAdapter() {
		super(DIFF_CALLBACK);
//		this.customDragListener = listener;
	}
	
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
	
	@NonNull
	@Override
	public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
		return new PendingViewHolder(itemView);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
		Ordine currentOrdine = getItem(position);
		holder.codice.setText(String.valueOf(currentOrdine.piatto));
		String desc = currentOrdine.desc;
		if (desc.length() >= 19) // TODO verificare se scala correttamente
			desc = desc.substring(0, 16) + "...";
		holder.descrizione.setText(desc);
		holder.quantita.setText(String.valueOf(currentOrdine.quantita));
//		holder.handle.setOnTouchListener((v, event) -> {
//			if (event.getAction() == MotionEvent.ACTION_DOWN)
//				customDragListener.onDragStarted(holder);
//			return true;
//		});
		ListPiattiFragmentDirections.ActionListaPiattiFragmentToModificaOrdineFragment action = ListPiattiFragmentDirections.actionListaPiattiFragmentToModificaOrdineFragment(currentOrdine);
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public Ordine getOrdineAt(int position) {
		return getItem(position);
	}
	
	public void aggiungiOrdine(Ordine ordine) {

	}
	
	public void modificaOrdine(Ordine ordine) {

	}
	
	public void inviaAlMaster(int position) throws IOException {
//		database.ordineDao().updateOrdineStatusByID(dataList.get(position).idOrdine, "confirmed");
//
//		String codiceTavolo = preferences.getString("codice_tavolo", null);
//		Connessione c = new Connessione(false, activity, codiceTavolo);
//		c.invia(dataList.get(position).getBytes());
	}
	
	public void riceviDaSlave() {
	
	}
	
	public void deleteItem(int position) {
//		cancellato = dataList.get(position);
//		indiceCancellato = position;
//		dataList.remove(position);
//		notifyItemRemoved(position);
//		View view = activity.findViewById(R.id.listaPiattiFragment);
//		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
//		snackbar.setAction("Undo delete?", v -> undoDelete());
//		snackbar.show();
	}
	
	public void retrieveFromConfirmed(int position) {
	}
	
	public void retrieveFromDelivered(int position) {
	}
	
	private void undoSend() {
	}
	
	private void undoDelete() {
	}
	
	private void undoDelivered() {
	}
	
	private void undoConfirmed() {
	}
	
	public static class PendingViewHolder extends RecyclerView.ViewHolder {
		private final TextView codice, descrizione, quantita;
//		private final ImageView handle;
		
		public PendingViewHolder(@NonNull View itemView) {
			super(itemView);
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			quantita = itemView.findViewById(R.id.piattoQuantita);
//			handle = itemView.findViewById(R.id.dragHandle);
		}
	}
	
//	public interface CustomDragListener {
//		void onDragStarted(RecyclerView.ViewHolder viewHolder);
//	}
}
