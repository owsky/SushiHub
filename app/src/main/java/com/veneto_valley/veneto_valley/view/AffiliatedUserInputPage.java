package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Category;
import com.veneto_valley.veneto_valley.model.entities.Dish;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class AffiliatedUserInputPage extends Fragment {
	private List<Dish> dishes = new ArrayList<>();
	private MenuAdapter menuAdapter;
	private ArrayAdapter<Category> categoryArrayAdapter;
	
	public AffiliatedUserInputPage() {
		super(R.layout.fragment_insert_order_affiliated);
	}
	
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// recyclerview init
		RecyclerView menu = view.findViewById(R.id.recyclerViewDishes);
		menu.setLayoutManager(new LinearLayoutManager(requireContext()));
		menuAdapter = new MenuAdapter();
		menu.setAdapter(menuAdapter);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		MenuViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), MenuViewModel.class,
				preferences.getString("table_code", null));
		String restaurantId = preferences.getString("rest_code", null);
		
		// spinner init
		Spinner spinner = view.findViewById(R.id.spinnerCategories);
		categoryArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
				viewModel.getCategories(menuAdapter, restaurantId));
		categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(categoryArrayAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				dishes = categoryArrayAdapter.getItem(position).dishes;
				menuAdapter.submitList(dishes);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// noop
			}
		});
	}
}