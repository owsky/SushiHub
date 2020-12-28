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
import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.dialogs.CancelDialog;

public class ModificaOrdineFragment extends Fragment {
	private EditText codice, desc, qta;
	private Ordine vecchioOrdine;
	private String codiceTavolo;
	
	public ModificaOrdineFragment() {
		super(R.layout.fragment_modifica_ordine);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		AppDatabase database = AppDatabase.getInstance(requireContext());
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		 codiceTavolo = preferences.getString("codice_tavolo", null);
		
		long codiceOrdine = ModificaOrdineFragmentArgs.fromBundle(getArguments()).getCodiceOrdine();
		vecchioOrdine = database.ordineDao().getOrdineById(codiceOrdine);
		
		codice = view.findViewById(R.id.editCodice);
		codice.setText(vecchioOrdine.piatto);
		desc = view.findViewById(R.id.editDesc);
		if (vecchioOrdine.desc != null)
			desc.setText(vecchioOrdine.desc);
		qta = view.findViewById(R.id.editQuantita);
		qta.setText(String.format(getResources().getConfiguration().locale, "%d", vecchioOrdine.quantita));
		
		Button salvaModifica = view.findViewById(R.id.salvaModifica);
		salvaModifica.setOnClickListener(v -> {
			Ordine nuovoOrdine = new Ordine(codiceTavolo, codice.getText().toString(), Integer.parseInt(qta.getText().toString()));
			nuovoOrdine.desc = desc.getText().toString();
			nuovoOrdine.status = vecchioOrdine.status;
			AdaptersViewModel viewModel = new ViewModelProvider(requireActivity()).get(AdaptersViewModel.class);
			PendingAdapter adapter = viewModel.getPendingAdapter().getValue();
			adapter.modificaOrdine(nuovoOrdine);
			NavHostFragment.findNavController(ModificaOrdineFragment.this).navigateUp();
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