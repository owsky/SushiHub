package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;

public class AppModePage extends Fragment {
	
	public AppModePage() {
		super(R.layout.fragment_app_mode_page);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Button conv = view.findViewById(R.id.convBtn);
		conv.setOnClickListener(v -> NavHostFragment.findNavController(AppModePage.this)
				.navigate(R.id.action_appModePage_to_listaRistorantiNav));
		Button nonConv = view.findViewById(R.id.nonConvBtn);
		nonConv.setOnClickListener(v -> NavHostFragment.findNavController(AppModePage.this)
				.navigate(R.id.action_appModeNav_to_impostaTavoloNav));
	}
}