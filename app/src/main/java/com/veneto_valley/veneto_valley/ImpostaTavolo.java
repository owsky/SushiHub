package com.veneto_valley.veneto_valley;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
		finito.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO scrivi info nel DB
				NavHostFragment.findNavController(ImpostaTavolo.this).navigate(R.id.action_impostaTavolo_to_listaPiattiFragment);
			}
		});
	}
}