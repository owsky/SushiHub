package com.veneto_valley.veneto_valley.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

public class OrdiniAdapter extends ListAdapter<Ordine, OrdiniAdapter.PendingViewHolder> {
//	private final CustomDragListener customDragListener;
	
	public OrdiniAdapter() {
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
		if (currentOrdine.status.equals("pending")) {
//			holder.handle.setOnTouchListener((v, event) -> {
//			if (event.getAction() == MotionEvent.ACTION_DOWN)
//				customDragListener.onDragStarted(holder);
//			return true;
//		});
			ListPiattiFragmentDirections.ActionListaPiattiFragmentToModificaOrdineFragment action = ListPiattiFragmentDirections.actionListaPiattiFragmentToModificaOrdineFragment(currentOrdine);
			holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
		} else {
			holder.handle.setVisibility(View.GONE);
		}
	}
	
	public Ordine getOrdineAt(int position) {
		return getItem(position);
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
	
//	public interface CustomDragListener {
//		void onDragStarted(RecyclerView.ViewHolder viewHolder);
//	}
}
