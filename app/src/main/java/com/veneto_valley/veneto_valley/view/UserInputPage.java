package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;

public class UserInputPage extends Fragment {
	private EditText codice, desc, qta, prezzo;
	
	public UserInputPage() {
		super(R.layout.fragment_user_input);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				CancelDialog dialog = new CancelDialog();
				dialog.show(getParentFragmentManager(), getTag());
			}
		});
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		codice = view.findViewById(R.id.addCodice);
		desc = view.findViewById(R.id.addDesc);
		qta = view.findViewById(R.id.addQuantita);
		prezzo = view.findViewById(R.id.addPrezzo);
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		
		UserInputPageArgs args = UserInputPageArgs.fromBundle(requireArguments());
		Ordine ordine;
		if ((ordine = args.getOrdine()) != null) {
			codice.setText(ordine.piatto);
			desc.setText(ordine.desc);
			qta.setText(String.valueOf(ordine.quantita));
			prezzo.setText(String.valueOf(ordine.prezzo));
			salvaEsci.setVisibility(View.GONE);
			salvaNuovo.setText("Salva");
			salvaNuovo.setOnClickListener(v -> {
				salvaOrdine();
				NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
		} else {
			salvaEsci.setOnClickListener(v -> {
				salvaOrdine();
				NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
			
			salvaNuovo.setOnClickListener(v -> {
				salvaOrdine();
				codice.setText(null);
				desc.setText(null);
				qta.setText(null);
				prezzo.setText(null);
				view.requestFocus();
			});
		}
	}
	
	private void salvaOrdine() {
		PendingViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), PendingViewModel.class);
		String codiceTavolo = viewModel.getTavolo();
		String codicePiatto = codice.getText().toString();
		int quantita = Integer.parseInt(qta.getText().toString());
		Ordine.statusOrdine status = Ordine.statusOrdine.pending;
		String descrizione = desc.getText().toString();
		String prezzoExtra = prezzo.getText().toString();
		
		Ordine ordine = new Ordine(codiceTavolo, codicePiatto, quantita, status);
		if (!descrizione.trim().isEmpty())
			ordine.desc = descrizione;
		
		if (!prezzoExtra.isEmpty())
			ordine.prezzo = Float.parseFloat(prezzoExtra);
		
		viewModel.insert(ordine);
	}
}