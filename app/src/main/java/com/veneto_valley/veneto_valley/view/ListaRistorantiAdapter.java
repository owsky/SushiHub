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
import com.veneto_valley.veneto_valley.model.entities.Ristorante;

public class ListaRistorantiAdapter extends ListAdapter<Ristorante, ListaRistorantiAdapter.RistoranteViewHolder> {
	
	public ListaRistorantiAdapter() {
		super(new DiffUtil.ItemCallback<Ristorante>() {
			@Override
			public boolean areItemsTheSame(@NonNull Ristorante oldItem, @NonNull Ristorante newItem) {
				return oldItem.idRistorante.equals(newItem.idRistorante);
			}
			
			@Override
			public boolean areContentsTheSame(@NonNull Ristorante oldItem, @NonNull Ristorante newItem) {
				return oldItem.nome.equals(newItem.nome) &&
						oldItem.indirizzo.equals(newItem.indirizzo) &&
						oldItem.localita.equals(newItem.localita);
			}
		});
	}
	
	@NonNull
	@Override
	public RistoranteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.elemento_ristorante, parent, false);
		return new RistoranteViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull final RistoranteViewHolder holder, int position) {
		Ristorante curr = getItem(position);
		holder.ristorante.setText(curr.nome);
		holder.indirizzo.setText(curr.indirizzo);
		
		ListaRistorantiDirections.ActionListaRistorantiNavToImpostaTavoloNav action =
				ListaRistorantiDirections.actionListaRistorantiNavToImpostaTavoloNav();
		action.setRistorante(curr);
		holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(action));
	}
	
	public static class RistoranteViewHolder extends RecyclerView.ViewHolder {
		public final TextView ristorante, indirizzo;
		
		public RistoranteViewHolder(View view) {
			super(view);
			ristorante = view.findViewById(R.id.nomeRistoranteItem);
			indirizzo = view.findViewById(R.id.indirizzoRistoranteItem);
		}
	}
}