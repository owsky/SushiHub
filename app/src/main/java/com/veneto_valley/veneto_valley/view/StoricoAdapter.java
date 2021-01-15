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
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

public class StoricoAdapter extends ListAdapter<Tavolo, StoricoAdapter.TavoloViewHolder> {
	
	// callback che consente al listadapter di confrontare gli elementi della lista
	private static final DiffUtil.ItemCallback<Tavolo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tavolo>() {
		@Override
		public boolean areItemsTheSame(@NonNull Tavolo oldItem, @NonNull Tavolo newItem) {
			return oldItem.idTavolo.equals(newItem.idTavolo);
		}
		
		@Override
		public boolean areContentsTheSame(@NonNull Tavolo oldItem, @NonNull Tavolo newItem) {
			return oldItem.dataCreazione.equals(newItem.dataCreazione) &&
					oldItem.ristorante.equals(newItem.ristorante) &&
					oldItem.costoMenu == newItem.costoMenu;
		}
	};
	
	protected StoricoAdapter() {
		super(DIFF_CALLBACK);
	}
	
	@NonNull
	@Override
	public TavoloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_storico, parent, false);
		return new TavoloViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull TavoloViewHolder holder, int position) {
		Tavolo tavolo = getItem(position);
		holder.ristorante.setText(tavolo.nome);
		holder.data.setText(tavolo.dataCreazione.toString());
		
		// crea un click listener che naviga verso la view con i dettagli delle ordinazioni, passando
		// un safearg con il tavolo selezionato
		ListaOrdiniGenericaPageDirections.ActionStoricoOrdiniPage2ToStoricoDettagliPage action =
				ListaOrdiniGenericaPageDirections.actionStoricoOrdiniPage2ToStoricoDettagliPage(tavolo);
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public static class TavoloViewHolder extends RecyclerView.ViewHolder {
		private final TextView ristorante, data;
		
		public TavoloViewHolder(@NonNull View itemView) {
			super(itemView);
			ristorante = itemView.findViewById(R.id.nomeRistorante);
			data = itemView.findViewById(R.id.dataStorico);
		}
	}
}
