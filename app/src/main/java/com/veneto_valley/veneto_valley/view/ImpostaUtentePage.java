package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
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
import com.veneto_valley.veneto_valley.viewmodel.InitViewModel;

public class ImpostaUtentePage extends Fragment {
	
	public ImpostaUtentePage() {
		super(R.layout.fragment_imposta_utente_page);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText impostaUsername = view.findViewById(R.id.impostaUtenteTextview);
		Button button = view.findViewById(R.id.impostaUtenteBtn);
		
		button.setOnClickListener(v -> {
			String username = impostaUsername.getText().toString();
			// verifico che l'utente abbia compilato l'edittext
			if (username.trim().isEmpty())
				Toast.makeText(requireContext(), "Inserisci uno username", Toast.LENGTH_SHORT).show();
			else {
				ViewModelUtil.getViewModel(requireActivity(), InitViewModel.class).initUtente(username);
				NavHostFragment.findNavController(this).navigate(R.id.action_impostaUtentePage_to_listeTabNav);
			}
		});
	}
}