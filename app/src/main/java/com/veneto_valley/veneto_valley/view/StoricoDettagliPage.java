package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.viewmodel.BaseViewModel;
import com.veneto_valley.veneto_valley.viewmodel.DeliveredViewModel;
import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;
import com.veneto_valley.veneto_valley.viewmodel.TavoloViewModel;

public class StoricoDettagliPage extends Fragment {
	
	public StoricoDettagliPage() {
		super(R.layout.fragment_storico_dettagli_page);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewStoricoPage);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		// TODO creare un nuovo adapter
		OrdiniAdapter adapter = new OrdiniAdapter();
		recyclerView.setAdapter(adapter);
		
		TavoloViewModel viewModel = new ViewModelProvider(requireActivity()).get(TavoloViewModel.class);
		viewModel.getOrdini().observe(getViewLifecycleOwner(), adapter::submitList);
	}
}