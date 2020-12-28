package com.veneto_valley.veneto_valley;

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

import com.veneto_valley.veneto_valley.adapters.PendingAdapter;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.dialogs.CancelDialog;

public class AggiungiOrdiniFragment extends Fragment {
	private EditText codice, desc, qta;
	private PendingAdapter adapter;
	
	public AggiungiOrdiniFragment() {
		super(R.layout.fragment_aggiungi_ordini);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		codice= view.findViewById(R.id.addCodice);
		desc = view.findViewById(R.id.addDesc);
		qta = view.findViewById(R.id.addQuantita);
		
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		salvaEsci.setOnClickListener(v -> {
			salva();
			NavHostFragment.findNavController(AggiungiOrdiniFragment.this).navigateUp();
		});
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		salvaNuovo.setOnClickListener(v -> {
			salva();
			codice.setText(null);
			desc.setText(null);
			qta.setText(null);
			view.requestFocus();
		});
		
		AdaptersViewModel viewModel = new ViewModelProvider(requireActivity()).get(AdaptersViewModel.class);
		adapter = viewModel.getPendingAdapter().getValue();
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
	
	private void salva() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		Ordine ordine = new Ordine(codiceTavolo, codice.getText().toString());
		ordine.desc = desc.getText().toString();
		ordine.quantita = Integer.parseInt(qta.getText().toString());
		ordine.status = "pending";
		adapter.aggiungiOrdine(ordine);
	}
}