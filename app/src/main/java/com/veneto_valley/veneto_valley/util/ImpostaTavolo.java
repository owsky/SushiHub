package com.veneto_valley.veneto_valley.util;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.veneto_valley.veneto_valley.ImpostaTavoloArgs;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

public class ImpostaTavolo extends Fragment {
	
	public ImpostaTavolo() {
		super(R.layout.fragment_imposta_tavolo);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText costoMenu = view.findViewById(R.id.costoMenu);
		EditText portate = view.findViewById(R.id.portatePersona);
		Button finito = view.findViewById(R.id.impostaFinito);
		
		finito.setOnClickListener(v -> {
			if (TextUtils.isEmpty(costoMenu.getText()))
				costoMenu.setError("Inserisci il costo del menu");
			else if (TextUtils.isEmpty(portate.getText()))
				portate.setError("Inserisci il numero massimo di portate");
			else {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
				SharedPreferences.Editor editor = sharedPreferences.edit();
				
				ImpostaTavoloArgs args;
				if (getArguments() != null && (args = ImpostaTavoloArgs.fromBundle(getArguments())).getCodiceTavolo() != null) {
					editor.putString("codice_tavolo", args.getCodiceTavolo());
					editor.putBoolean("is_master", true);
					editor.apply();
					Tavolo tavolo = new Tavolo(args.getCodiceTavolo(), Integer.parseInt(portate.getText().toString()), Float.parseFloat(costoMenu.getText().toString()));
					AppDatabase.getInstance(requireContext()).tavoloDao().insertAll(tavolo);
				}
				NavHostFragment.findNavController(ImpostaTavolo.this).navigate(R.id.action_impostaTavolo_to_listaPiattiFragment);
			}
		});
	}
}