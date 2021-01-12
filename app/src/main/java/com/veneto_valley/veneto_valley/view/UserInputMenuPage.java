package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdiniViewModel;

import org.w3c.dom.Text;

import static android.view.View.GONE;

public class UserInputMenuPage extends Fragment {
	
	public UserInputMenuPage() {
		super(R.layout.fragment_user_input_menu);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView nomePiatto = view.findViewById(R.id.nomePiattoDyn);
		TextView codice = view.findViewById(R.id.codice);
		EditText qta = view.findViewById(R.id.addQuantita);
		qta.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		if (getArguments() != null) {
			UserInputMenuPageArgs args = UserInputMenuPageArgs.fromBundle(getArguments());
			if (args.getOrdine() != null) {
				Ordine ordine = args.getOrdine();
				nomePiatto.setText(ordine.desc);
				codice.setText(ordine.piatto);
				qta.setText(ordine.quantita);
				
			} else if (args.getPiatto() != null) {
				Piatto piatto = args.getPiatto();
				nomePiatto.setText(piatto.nome);
				codice.setText(piatto.idPiatto);
				qta.setText("1");
			}
		}
		
		Button salva = view.findViewById(R.id.salva);
		salva.setOnClickListener(v -> {
			if (qta.getText().toString().isEmpty()) {
				Toast.makeText(requireContext(), "Inserisci la quantità", Toast.LENGTH_SHORT).show();
			} else {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
				String tavolo = preferences.getString("codice_tavolo", null);
				int quantita = Integer.parseInt(qta.getText().toString());
				String utente = preferences.getString("username", null);
				OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class);
				String cod = codice.getText().toString();
				Ordine ordine = new Ordine(tavolo, cod, quantita, Ordine.StatusOrdine.pending, utente, false);
				ordine.desc = nomePiatto.getText().toString();
				viewModel.insert(ordine);
				NavHostFragment.findNavController(this).navigate(R.id.action_userInputMenu_to_listeTabNav);
			}
		});
	}
}