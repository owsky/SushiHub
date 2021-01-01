package com.veneto_valley.veneto_valley.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;

public class AggiungiOrdiniFragment extends Fragment {
	private EditText codice, desc, qta;
	
	public AggiungiOrdiniFragment() {
		super(R.layout.fragment_aggiungi_ordini);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		codice = view.findViewById(R.id.addCodice);
		desc = view.findViewById(R.id.addDesc);
		qta = view.findViewById(R.id.addQuantita);
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		
		salvaEsci.setOnClickListener(v -> {
			salvaOrdine();
			NavHostFragment.findNavController(AggiungiOrdiniFragment.this).navigateUp();
		});
		
		salvaNuovo.setOnClickListener(v -> {
			salvaOrdine();
			codice.setText(null);
			desc.setText(null);
			qta.setText(null);
			view.requestFocus();
		});
	}
	
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		setHasOptionsMenu(true);
		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				CancelDialog dialog = new CancelDialog();
				dialog.show(getParentFragmentManager(), null);
			}
		});
	}
	
	private void salvaOrdine() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		String codicePiatto = codice.getText().toString();
		int quantita = Integer.parseInt(qta.getText().toString());
		String status = "pending";
		String descrizione = desc.getText().toString();
		
		Ordine ordine = new Ordine(codiceTavolo, codicePiatto, quantita, status);
		if (!descrizione.trim().isEmpty())
			ordine.desc = descrizione;
		
		PendingViewModel viewModel = new ViewModelProvider(requireActivity()).get(PendingViewModel.class);
		viewModel.insert(ordine);
	}
}