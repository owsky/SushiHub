package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;

public class AppModePage extends Fragment {
	
	public AppModePage() {
		super(R.layout.fragment_homepage);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView greeting = view.findViewById(R.id.greeting);
		greeting.setText(R.string.conv_greeting);
		
		Button conv = view.findViewById(R.id.btnCrea);
		conv.setText("Ristorante\nConvenzionato");
		conv.setOnClickListener(v -> NavHostFragment.findNavController(AppModePage.this)
				.navigate(R.id.action_appModePage_to_listaRistorantiNav));
		
		Button nonConv = view.findViewById(R.id.btnUnisciti);
		nonConv.setText("Ristorante non\nConvenzionato");
		nonConv.setOnClickListener(v -> NavHostFragment.findNavController(AppModePage.this)
				.navigate(R.id.action_appModeNav_to_impostaTavoloNav));
	}
}