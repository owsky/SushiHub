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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
		Ordine ordine = UserInputPageArgs.fromBundle(requireArguments()).getOrdine();
		if (ordine != null) {
			codice.setText(ordine.piatto);
			desc.setText(ordine.desc);
			qta.setVisibility(View.GONE);
			TextView qtaText = view.findViewById(R.id.qta);
			qtaText.setVisibility(View.GONE);
			prezzo.setText(String.valueOf(ordine.prezzo));
			salvaEsci.setVisibility(View.GONE);
			salvaNuovo.setText(R.string.salva);
			salvaNuovo.setOnClickListener(v -> {
				if (salvaOrdine(ordine))
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
				if (salvaOrdine(null))
					NavHostFragment.findNavController(UserInputPage.this).navigateUp();
			});
			
			salvaNuovo.setOnClickListener(v -> {
				if (salvaOrdine(null)) {
					codice.setText(null);
					desc.setText(null);
					qta.setText(null);
					prezzo.setText(null);
					codice.requestFocus();
				}
			});
		}
	}
	
	private boolean salvaOrdine(Ordine o) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(),
				OrdiniViewModel.class, preferences.getString("codice_tavolo", null));
		String codiceTavolo = viewModel.getTavolo();
		String codicePiatto = codice.getText().toString();
		Ordine.StatusOrdine status = Ordine.StatusOrdine.pending;
		String descrizione = desc.getText().toString();
		String prezzoExtra = prezzo.getText().toString();
		
		// verifico che i parametri nonnull vengano inseriti
		if (codicePiatto.trim().isEmpty())
			Toast.makeText(requireContext(), "Inserisci il codice del piatto", Toast.LENGTH_SHORT).show();
		else if (o == null && qta.getText().toString().isEmpty())
			Toast.makeText(requireContext(), "Inserisci la quantità", Toast.LENGTH_SHORT).show();
		else {
			// istanzio un nuovo ordine tramite lo user input e lo username dell'utente, recuperato
			// attraverso le shared preferences e lo passo al viewmodel
			String username = preferences.getString("username", "username");
			Ordine ordine = new Ordine(codiceTavolo, codicePiatto, status, username, false);
			if (!descrizione.trim().isEmpty())
				ordine.desc = descrizione;
			if (!prezzoExtra.trim().isEmpty())
				ordine.prezzo = Float.parseFloat(prezzoExtra);
			viewModel.insert(ordine, Integer.parseInt(qta.getText().toString()));
			Snackbar.make(requireActivity().findViewById(android.R.id.content),
					"Annullare l'operazione?", BaseTransientBottomBar.LENGTH_LONG)
					.setAction("Undo", v -> viewModel.undoInsert())
					.setAnchorView(requireActivity().findViewById(R.id.salvaEsci))
					.show();
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