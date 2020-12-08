package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.dialogs.ExitDialog;

public class AggiungiOrdiniFragment extends Fragment {
	
	public AggiungiOrdiniFragment() {
		super(R.layout.fragment_aggiungi_ordini);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				ExitDialog dialog = new ExitDialog();
				dialog.show(getParentFragmentManager(), null);
			}
		});
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText codice = view.findViewById(R.id.addCodice);
		EditText desc = view.findViewById(R.id.addDesc);
		EditText qta = view.findViewById(R.id.addQuantita);
		
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		salvaEsci.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO scrive le informazioni sul DB
				NavHostFragment.findNavController(AggiungiOrdiniFragment.this).navigateUp();
			}
		});
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		salvaNuovo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO scrive le informazioni sul DB
				codice.setText(null);
				desc.setText(null);
				qta.setText(null);
				view.requestFocus();
			}
		});
	}
	
	
}