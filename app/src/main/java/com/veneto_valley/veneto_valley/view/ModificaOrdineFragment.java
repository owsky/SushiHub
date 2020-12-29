package com.veneto_valley.veneto_valley.view;

import android.content.Context;
import android.os.Bundle;
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

public class ModificaOrdineFragment extends Fragment {
	private EditText codice, desc, qta;
	
	public ModificaOrdineFragment() {
		super(R.layout.fragment_modifica_ordine);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Ordine ordine = ModificaOrdineFragmentArgs.fromBundle(getArguments()).getOrdine();
		
		codice = view.findViewById(R.id.editCodice);
		codice.setText(ordine.piatto);
		desc = view.findViewById(R.id.editDesc);
		if (ordine.desc != null)
			desc.setText(ordine.desc);
		qta = view.findViewById(R.id.editQuantita);
		qta.setText(String.valueOf(ordine.quantita));
		
		Button salvaModifica = view.findViewById(R.id.salvaModifica);
		salvaModifica.setOnClickListener(v -> {
			ordine.quantita = Integer.parseInt(qta.getText().toString());
			ordine.desc = desc.getText().toString();
			ordine.piatto = codice.getText().toString();
			PendingViewModel viewModel = new ViewModelProvider(requireActivity()).get(PendingViewModel.class);
			viewModel.update(ordine);
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