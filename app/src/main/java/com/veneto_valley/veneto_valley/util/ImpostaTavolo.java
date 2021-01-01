package com.veneto_valley.veneto_valley.util;

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
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

public class ImpostaTavolo extends Fragment {
	//TODO inserire nome ristorante
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
				Toast.makeText(requireContext(), "Inserisci il costo del menu", Toast.LENGTH_SHORT).show();
			else {
				ImpostaTavoloArgs args = ImpostaTavoloArgs.fromBundle(requireArguments());
				CreaTavoloViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class);
				viewModel.creaTavolo(args.getCodiceTavolo(), Integer.parseInt(portate.getText().toString()), Float.parseFloat(costoMenu.getText().toString()));
				NavHostFragment.findNavController(this).navigate(R.id.action_impostaTavolo_to_listaPiattiFragment);
			}
		});
	}
}