package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.AllOrdersViewModel;

public class ListaAllOrdersPage extends Fragment {
	
	public ListaAllOrdersPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		OrdiniAdapter adapter = new OrdiniAdapter();
		recyclerView.setAdapter(adapter);
		
		final AllOrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), AllOrdersViewModel.class);
		viewModel.getOrdini().observe(getViewLifecycleOwner(), adapter::submitList);
	}
}