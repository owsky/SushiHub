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
import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class MenuAggiuntaOrdine extends Fragment {
	private List<Piatto> piatti = new ArrayList<>();
	private MenuAdapter menuAdapter;
	private ArrayAdapter<Categoria> categorieAdapter;
	
	public MenuAggiuntaOrdine() {
		super(R.layout.fragment_aggiunta_ordine_menu);
	}
	
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// inizializzazione recyclerview piatti
		RecyclerView menu = view.findViewById(R.id.recyclerViewPiatti);
		menu.setLayoutManager(new LinearLayoutManager(requireContext()));
		menuAdapter = new MenuAdapter();
		menu.setAdapter(menuAdapter);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		MenuViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), MenuViewModel.class, preferences.getString("codice_tavolo", null));
		String idRistorante = preferences.getString("codice_ristorante", null);
		
		// inizializzazione spinner categorie
		Spinner spinner = view.findViewById(R.id.spinnerCategorie);
		categorieAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, viewModel.getCategoria(menuAdapter, idRistorante));
		categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(categorieAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				piatti = categorieAdapter.getItem(position).piatti;
				menuAdapter.submitList(piatti);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			// noop
			}
		});
	}
}