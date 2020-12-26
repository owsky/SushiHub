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
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.dialogs.CancelDialog;

public class AggiungiOrdiniFragment extends Fragment {
	
	public AggiungiOrdiniFragment() {
		super(R.layout.fragment_aggiungi_ordini);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText codice = view.findViewById(R.id.addCodice);
		EditText desc = view.findViewById(R.id.addDesc);
		EditText qta = view.findViewById(R.id.addQuantita);
		
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		salvaEsci.setOnClickListener(v -> {
			// TODO scrive le informazioni sul DB
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
			String codiceTavolo = preferences.getString("codice_tavolo", null);
			// TODO modifica tipo Ordine.codice, Ordine.tavolo
			Ordine ordine = new Ordine(Integer.parseInt(codiceTavolo), Integer.parseInt(codice.getText().toString()));
			// TODO descrizione
			AppDatabase database = AppDatabase.getInstance(requireContext());
			database.ordineDao().insertAll(ordine);
			NavHostFragment.findNavController(AggiungiOrdiniFragment.this).navigateUp();
		});
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		salvaNuovo.setOnClickListener(v -> {
			// TODO scrive le informazioni sul DB
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
}