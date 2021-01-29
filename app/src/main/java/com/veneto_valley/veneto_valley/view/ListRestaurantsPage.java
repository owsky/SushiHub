package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Restaurant;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.MenuViewModel;

import java.util.List;

public class ListRestaurantsPage extends Fragment {
	
	public ListRestaurantsPage() {
		super(R.layout.fragment_restaurants_list);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRistoranti);
		
		MenuViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), MenuViewModel.class);
		ListRestaurantsAdapter adapter = new ListRestaurantsAdapter();
		List<Restaurant> restaurants = viewModel.getRestaurants(adapter);
		adapter.submitList(restaurants);
		
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
	}
}