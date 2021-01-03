package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.InitViewModel;
import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;

public class UserInputPage extends Fragment {
	private EditText codice, desc, qta, prezzo;
	private TextView prezzoTextView;
	private boolean isExtra;
	
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
		prezzoTextView = view.findViewById(R.id.prezzoTextView);
		Button salvaEsci = view.findViewById(R.id.salvaEsci);
		Button salvaNuovo = view.findViewById(R.id.salvaNuovo);
		SwitchMaterial switchMaterial = view.findViewById(R.id.switchExtra);
		switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> flipExtra());
		
		UserInputPageArgs args = UserInputPageArgs.fromBundle(requireArguments());
		Ordine ordine;
		if ((ordine = args.getOrdine()) != null) {
			codice.setText(ordine.piatto);
			desc.setText(ordine.desc);
			qta.setText(String.valueOf(ordine.quantita));
			prezzo.setText(String.valueOf(ordine.prezzo));
			salvaEsci.setVisibility(View.GONE);
			salvaNuovo.setText(R.string.salva);
			salvaNuovo.setOnClickListener(v -> {
				salvaOrdine();
				NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
			if (ordine.prezzo > 0) {
				prezzo.setVisibility(View.VISIBLE);
				prezzoTextView.setVisibility(View.VISIBLE);
				switchMaterial.setChecked(true);
			}
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
		
		InitViewModel initViewModel = ViewModelUtil.getViewModel(requireActivity(), InitViewModel.class);
		Ordine ordine = new Ordine(codiceTavolo, codicePiatto, quantita, status, initViewModel.getOwner());
		if (!descrizione.trim().isEmpty())
			ordine.desc = descrizione;
		
		if (!prezzoExtra.isEmpty())
			ordine.prezzo = Float.parseFloat(prezzoExtra);
		
		if (!isExtra)
			ordine.prezzo = 0;
		
		viewModel.insert(ordine);
	}
	
	private void flipExtra() {
		if (isExtra) {
			prezzoTextView.setVisibility(View.INVISIBLE);
			prezzo.setVisibility(View.INVISIBLE);
			isExtra = false;
		} else {
			prezzoTextView.setVisibility(View.VISIBLE);
			prezzo.setVisibility(View.VISIBLE);
			isExtra = true;
		}
	}
}