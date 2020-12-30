package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.viewmodel.CheckoutViewModel;

public class CheckOutPage extends Fragment {
	
	public CheckOutPage() {
		super(R.layout.fragment_check_out);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView costoMenu = view.findViewById(R.id.checkoutMenu);
		TextView costoExtra = view.findViewById(R.id.checkoutExtra);
		TextView totale = view.findViewById(R.id.checkoutTotale);
		Button finito = view.findViewById(R.id.checkoutFinito);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String tavolo = preferences.getString("codice_tavolo", null);
		CheckoutViewModel checkoutViewModel = new CheckoutViewModel(requireActivity().getApplication(), tavolo);
		
		costoMenu.setText(String.format(requireActivity().getResources().getConfiguration().locale,"Costo Menu: %s €", checkoutViewModel.getCostoMenu(tavolo)));
		
		//TODO assegnare costo extra
		costoExtra.setText("Costo Extra: 0 €");
		//TODO sommare menu ed extra e scrivere su totale
		totale.setText(String.format(requireActivity().getResources().getConfiguration().locale,"Costo totale: %s €", checkoutViewModel.getCostoMenu(tavolo)));
		finito.setOnClickListener(v -> {
			preferences.edit().remove("codice_tavolo").apply();
			//TODO eliminare slave
			NavHostFragment.findNavController(CheckOutPage.this).navigate(R.id.action_checkOutPage_to_homepageFragment);
		});
	}
	
}