package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.util.Misc;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdiniViewModel;

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
		
		// verifico se l'utente desidera aggiungere un nuovo ordine o modificarne uno esistente
		// e costruisco la view di conseguenza; discrimino inoltre tra ordine normale o fuori menu
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
				if (salvaOrdine())
					NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
			if (ordine.prezzo > 0) {
				desc.setNextFocusDownId(prezzo.getId());
				prezzo.setVisibility(View.VISIBLE);
				prezzoTextView.setVisibility(View.VISIBLE);
				switchMaterial.setChecked(true);
			} else {
				desc.setNextFocusDownId(salvaNuovo.getId());
			}
		} else {
			if (!isExtra)
				desc.setImeOptions(EditorInfo.IME_ACTION_DONE);
			
			salvaEsci.setOnClickListener(v -> {
				if (salvaOrdine())
					NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
			
			salvaNuovo.setOnClickListener(v -> {
				if (salvaOrdine()) {
					codice.setText(null);
					desc.setText(null);
					qta.setText(null);
					prezzo.setText(null);
					codice.requestFocus();
				}
			});
		}
	}
	
	private boolean salvaOrdine() {
		OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class);
		String codiceTavolo = viewModel.getTavolo();
		String codicePiatto = codice.getText().toString();
		Ordine.StatusOrdine status = Ordine.StatusOrdine.pending;
		String descrizione = desc.getText().toString();
		String prezzoExtra = prezzo.getText().toString();
		
		// verifico che i parametri nonnull vengano inseriti
		if (codicePiatto.trim().isEmpty())
			Toast.makeText(requireContext(), "Inserisci il codice del piatto", Toast.LENGTH_SHORT).show();
		else if (qta.getText().toString().isEmpty())
			Toast.makeText(requireContext(), "Inserisci la quantità", Toast.LENGTH_SHORT).show();
		else {
			// istanzio un nuovo ordine tramite lo user input e lo username dell'utente, recuperato
			// attraverso le shared preferences e lo passo al viewmodel
			int quantita = Integer.parseInt(qta.getText().toString());
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
			String username = preferences.getString("username", "username");
			Ordine ordine = new Ordine(codiceTavolo, codicePiatto, quantita, status, username, false);
			if (!descrizione.trim().isEmpty())
				ordine.desc = descrizione;
			
			if (!prezzoExtra.isEmpty())
				ordine.prezzo = Float.parseFloat(prezzoExtra);
			
			if (!isExtra)
				ordine.prezzo = 0;
			
			viewModel.insert(ordine);
			return true;
		}
		return false;
	}
	
	private void flipExtra() {
		Misc.hideKeyboard(requireActivity());
		if (isExtra) {
			prezzoTextView.setVisibility(View.INVISIBLE);
			prezzo.setVisibility(View.INVISIBLE);
			isExtra = false;
			desc.setImeOptions(EditorInfo.IME_ACTION_DONE);
		} else {
			prezzoTextView.setVisibility(View.VISIBLE);
			prezzo.setVisibility(View.VISIBLE);
			isExtra = true;
			desc.setNextFocusDownId(prezzo.getId());
			desc.setImeOptions(EditorInfo.TYPE_NULL);
		}
	}
}