package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

public class ImpostaTavoloPage extends Fragment {
	public ImpostaTavoloPage() {
		super(R.layout.fragment_imposta_tavolo);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText nome = view.findViewById(R.id.impostaNome);
		EditText costoMenu = view.findViewById(R.id.costoMenu);
		EditText portate = view.findViewById(R.id.portatePersona);
		Button finito = view.findViewById(R.id.impostaFinito);
		
		finito.setOnClickListener(v -> {
			// verifico che tutte le edittext siano state compilato dell'utente
			if (TextUtils.isEmpty(nome.getText()))
				Toast.makeText(requireContext(), "Inserisci il nome del ristorante", Toast.LENGTH_SHORT).show();
			else if (TextUtils.isEmpty(costoMenu.getText()))
				Toast.makeText(requireContext(), "Inserisci il costo del menu", Toast.LENGTH_SHORT).show();
			else if (TextUtils.isEmpty(portate.getText()))
				Toast.makeText(requireContext(), "Inserisci il numero di portate massime a persona", Toast.LENGTH_SHORT).show();
			else {
				// creo un tavolo tramite lo user input attraverso il viewmodel
				CreaTavoloViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class);
				viewModel.creaTavolo(Integer.parseInt(portate.getText().toString()), Float.parseFloat(costoMenu.getText().toString()));
				NavHostFragment.findNavController(this).navigate(R.id.action_impostaTavoloNav_to_generaQRNav);
			}
		});
	}
}