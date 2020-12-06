package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersFragment extends Fragment implements OrdiniAdapter.OnOrderClickListener {
	OrdiniAdapter ordiniAdapter;
	List<Ordine> listaOrdini = new ArrayList<>();
	
	public PendingOrdersFragment() {
		super(R.layout.fragment_pending_orders);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPending);
		
		listaOrdini.add(new Ordine("1", "Ravioli"));
		listaOrdini.add(new Ordine("7", "Cinghiale"));
		listaOrdini.add(new Ordine("55", "Yaki Udon"));
		listaOrdini.add(new Ordine("101", "Sake Nigiri"));
		
		ordiniAdapter = new OrdiniAdapter(requireContext(), listaOrdini, this);
		recyclerView.setAdapter(ordiniAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.addItemDecoration(new MyDividerItemDecoration(requireContext()));
	}
	
	@Override
	public void onOrderClick(int position) {
		openDialog(position);
	}
	
	public void openDialog(int position) {
		FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
		ModificaPiattoDialog modificaPiattoDialog = new ModificaPiattoDialog(listaOrdini, ordiniAdapter, position);
		modificaPiattoDialog.show(fragmentManager, null);
	}
	
	public void applica(int position, String codice, String descrizione) {
		listaOrdini.get(position).codice = codice;
		listaOrdini.get(position).descrizione = descrizione;
		ordiniAdapter.notifyItemChanged(position);
	}
}