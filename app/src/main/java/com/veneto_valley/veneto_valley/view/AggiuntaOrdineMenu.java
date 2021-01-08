package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AggiuntaOrdineMenu extends Fragment {
	private AdapterView.OnItemSelectedListener listener;
	
	public AggiuntaOrdineMenu() {
		super(R.layout.fragment_aggiunta_ordine_menu);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listener = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			
			}
		};
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Spinner spinner = view.findViewById(R.id.spinnerCategorie);
		// TODO: get lista categorie da viewmodel
		List<String> categorie = new ArrayList<>();
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorie);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(listener);
		
		// TODO: get lista piatti da viewmodel
		List<String> piatti = new ArrayList<>();
		RecyclerView menu = view.findViewById(R.id.recyclerViewPiatti);
		menu.setLayoutManager(new LinearLayoutManager(requireContext()));
		MenuAdapter menuAdapter = new MenuAdapter();
		menuAdapter.submitList(piatti);
		menu.setAdapter(menuAdapter);
	}
}