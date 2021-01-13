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
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdiniViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
		
		// calcolo il totale che l'utente dovrà pagare
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class, preferences.getString("codice_tavolo", null));
		final Locale locale = requireActivity().getResources().getConfiguration().locale;
		float menu = viewModel.getCostoMenu();
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
		df.setMaximumFractionDigits(340);
		costoMenu.setText(String.format(locale, "Costo Menu: %s €", df.format(menu)));
		float extra = viewModel.getCostoExtra();
		costoExtra.setText(String.format(locale, "Costo Extra: %s €", df.format(extra)));
		float tot = menu + extra;
		totale.setText(String.format(locale, "Costo totale: %s €", df.format(tot)));
		finito.setOnClickListener(v -> {
			// invoco il checkout tramite il viewmodel
			viewModel.checkout(requireActivity());
			NavHostFragment.findNavController(CheckOutPage.this).navigate(R.id.action_checkOutPage_to_homepageFragment);
		});
	}
	
}