package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.MenuViewModel;

import java.util.List;

public class ListaRistoranti extends Fragment {
	
	public ListaRistoranti() {
		super(R.layout.fragment_lista_ristoranti);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRistoranti);
		
		MenuViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), MenuViewModel.class);
		ListaRistorantiAdapter adapter = new ListaRistorantiAdapter();
		List<Ristorante> ristoranti = viewModel.getRistoranti(adapter);
		adapter.submitList(ristoranti);
		
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
	}
}