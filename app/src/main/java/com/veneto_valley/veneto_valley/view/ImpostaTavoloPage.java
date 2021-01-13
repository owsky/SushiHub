package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.util.Misc;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

public class ImpostaTavoloPage extends Fragment {
	private CreaTavoloViewModel viewModel;
	
	public ImpostaTavoloPage() {
		super(R.layout.fragment_imposta_tavolo);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class);
		if (getArguments() != null) {
			ImpostaTavoloPageArgs args = ImpostaTavoloPageArgs.fromBundle(getArguments());
			Ristorante r;
			if ((r = args.getRistorante()) != null) {
				viewModel.creaTavolo(r.idRistorante, r.costoMenu);
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
				preferences.edit().putString("codice_ristorante", r.idRistorante).apply();
				NavHostFragment.findNavController(this).navigate(R.id.action_impostaTavoloNav_to_generaQRNav);
			}
		}
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText nome = view.findViewById(R.id.impostaNome);
		EditText costoMenu = view.findViewById(R.id.costoMenu);
		Button finito = view.findViewById(R.id.impostaFinito);
		
		costoMenu.setImeOptions(EditorInfo.IME_ACTION_DONE);
		finito.setOnClickListener(v -> {
			// verifico che tutte le edittext siano state compilate dell'utente
			if (TextUtils.isEmpty(nome.getText()))
				Toast.makeText(requireContext(), "Inserisci il nome del ristorante", Toast.LENGTH_SHORT).show();
			else if (TextUtils.isEmpty(costoMenu.getText()))
				Toast.makeText(requireContext(), "Inserisci il costo del menu", Toast.LENGTH_SHORT).show();
			else {
				// creo un tavolo tramite lo user input attraverso il viewmodel
				float costo = Float.parseFloat(costoMenu.getText().toString());
				viewModel.creaTavolo(null, costo);
				Misc.hideKeyboard(requireActivity());
				NavHostFragment.findNavController(this).navigate(R.id.action_impostaTavoloNav_to_generaQRNav);
			}
		});
	}
}