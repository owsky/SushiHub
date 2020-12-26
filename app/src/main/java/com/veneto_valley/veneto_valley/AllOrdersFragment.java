package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment {
	
	public AllOrdersFragment() {
		super(R.layout.fragment_all_orders);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAll);
		List<Ordine> listaOrdini = new ArrayList<>();
		listaOrdini.add(new Ordine("1", "Ravioli"));
		listaOrdini.add(new Ordine("7", "Cinghiale"));
		listaOrdini.add(new Ordine("55", "Yaki Udon"));
		listaOrdini.add(new Ordine("101", "Sake Nigiri"));
		OrdiniAdapter adapter = new OrdiniAdapter(requireActivity(), listaOrdini);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	}
}