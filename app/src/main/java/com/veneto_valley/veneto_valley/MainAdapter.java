package com.veneto_valley.veneto_valley;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
	private List<Ordine> dataList;
	private Activity context;
	private AppDatabase database;
	private CustomDragListener customDragListener;
	private Ordine cancellato;
	private int indiceCancellato;
	private Snackbar snackbar;
	
	public MainAdapter(Activity context, List<Ordine> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.customDragListener = null;
	}
	
	public MainAdapter(Activity context, List<Ordine> dataList, CustomDragListener customDragListener) {
		this.context = context;
		this.dataList = dataList;
		notifyDataSetChanged();
		
		this.customDragListener = customDragListener;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
		return new ViewHolder(view);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Ordine ordine = dataList.get(position);
		database = AppDatabase.getInstance(context);
		holder.codice.setText(String.format(context.getResources().getConfiguration().locale, "%d", ordine.getIdOrdine()));
		String desc = ordine.getDesc(); // TODO Done manca descrizione ordine
		if (desc.length() >= 19) //TODO verificare se scala correttamente
			desc = desc.substring(0, 16) + "...";
		holder.descrizione.setText(desc);
		holder.quantita.setText(String.format(context.getResources().getConfiguration().locale, "%d", ordine.getQuantita())); //TODO Done MANCA QUANTITA
		if (customDragListener != null) {
			holder.handleView.setOnTouchListener((v, event) -> {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					customDragListener.onDragStarted(holder);
				return true;
			});
		} else {
			holder.handleView.setVisibility(View.GONE);
		}
		ListPiattiFragmentDirections.ActionListaPiattiFragmentToModificaOrdineFragment action = ListPiattiFragmentDirections.actionListaPiattiFragmentToModificaOrdineFragment(Integer.parseInt(holder.codice.getText().toString()));
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	@Override
	public int getItemCount() {
		return dataList.size();
	}
	
	public void sendItem(int position) {
		//TODO comunicazione master
		cancellato = dataList.get(position);
		indiceCancellato = position;
		dataList.remove(position);
		notifyItemRemoved(position);
		View view = context.findViewById(R.id.pendingOrders);
		snackbar = Snackbar.make(view, "Snackbar text", Snackbar.LENGTH_SHORT);
		snackbar.setAction("Undo send?", v -> undoSend());
		snackbar.show();
	}
	
	public void deleteItem(int position) {
		cancellato = dataList.get(position);
		indiceCancellato = position;
		dataList.remove(position);
		notifyItemRemoved(position);
		View view = context.findViewById(R.id.listaPiattiFragment);
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
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView codice, descrizione, quantita;
		public final ImageView handleView;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			codice = itemView.findViewById(R.id.piattoCodice);
			descrizione = itemView.findViewById(R.id.piattoDesc);
			quantita = itemView.findViewById(R.id.piattoQuantita);
			handleView = itemView.findViewById(R.id.dragHandle);
		}
	}
	
	public interface CustomDragListener {
		void onDragStarted(RecyclerView.ViewHolder viewHolder);
	}
}
