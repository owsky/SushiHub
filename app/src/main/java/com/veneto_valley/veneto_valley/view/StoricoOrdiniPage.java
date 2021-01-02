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
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.StoricoViewModel;

public class StoricoOrdiniPage extends Fragment {
	
	public StoricoOrdiniPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		StoricoAdapter adapter = new StoricoAdapter();
		recyclerView.setAdapter(adapter);
		
		StoricoViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), StoricoViewModel.class);
		viewModel.getTavoli().observe(getViewLifecycleOwner(), adapter::submitList);
	}
}