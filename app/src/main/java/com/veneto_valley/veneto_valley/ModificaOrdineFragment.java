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
import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.dialogs.CancelDialog;

public class ModificaOrdineFragment extends Fragment {
	
	public ModificaOrdineFragment() {
		super(R.layout.fragment_modifica_ordine);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		AppDatabase database = AppDatabase.getInstance(requireContext());
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		assert getArguments() != null;
		int cod = ModificaOrdineFragmentArgs.fromBundle(getArguments()).getPosition();
		//Ordine ordine = database.ordineDao().getOrdineById(cod);// TODO Done ritorna ordine
		
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		
		EditText codice = view.findViewById(R.id.addCodice);
		codice.setText(cod);
		EditText desc = view.findViewById(R.id.addDesc);
		//desc.setText(ordine.getDesc()); //TODO Done get descrizione
		EditText qta = view.findViewById(R.id.addQuantita);
		//qta.setText(ordine.getQuantita()); //TODO Done get quantita
		
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		salvaEsci.setOnClickListener(v -> {
			// TODO scrive le informazioni sul DB
			// TODO Done modifica tipo Ordine.codice, Ordine.tavolo
			Ordine ordine = new Ordine(codiceTavolo, codice.getText().toString());
			// TODO Done descrizione
			ordine.desc = "...";
			
			database.ordineDao().insertAll(ordine);
			NavHostFragment.findNavController(ModificaOrdineFragment.this).navigateUp();
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