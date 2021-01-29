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
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;

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
		TextView menuPrice = view.findViewById(R.id.checkoutMenu);
		TextView extrasPrice = view.findViewById(R.id.checkoutExtra);
		TextView sum = view.findViewById(R.id.checkoutTotal);
		Button done = view.findViewById(R.id.checkoutDone);
		
		// computes the single user's total price
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		OrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(),
				OrdersViewModel.class, preferences.getString("codice_tavolo", null));
		final Locale locale = requireActivity().getResources().getConfiguration().locale;
		float menu = viewModel.getMenuPrice();
		
		// numbers pretty printing
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
		df.setMaximumFractionDigits(340);
		menuPrice.setText(String.format(locale, "Menu Price: %s €", df.format(menu)));
		float extra = viewModel.getExtrasPrice();
		extrasPrice.setText(String.format(locale, "Extras Price: %s €", df.format(extra)));
		float tot = menu + extra;
		sum.setText(String.format(locale, "Total: %s €", df.format(tot)));
		done.setOnClickListener(v -> {
			viewModel.checkout(requireActivity());
			NavHostFragment.findNavController(CheckOutPage.this).navigate(R.id.action_checkOutNav_to_homepageNav);
		});
	}
	
}