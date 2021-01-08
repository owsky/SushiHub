package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;

public class ListaRistoranti extends Fragment {
	
	public ListaRistoranti() {
		super(R.layout.fragment_lista_ristoranti);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRistoranti);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new ListaRistorantiAdapter());
		// TODO: observe livedata ristoranti
	}
}