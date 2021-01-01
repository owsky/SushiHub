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
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CheckoutViewModel;

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
		
		CheckoutViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), CheckoutViewModel.class);
		final Locale locale = requireActivity().getResources().getConfiguration().locale;
		float menu = viewModel.getCostoMenu();
		costoMenu.setText(String.format(locale, "Costo Menu: %s €", menu));
		float extra = viewModel.getCostoExtra();
		costoExtra.setText(String.format(locale, "Costo Extra: %s €", extra));
		float tot = menu + extra;
		totale.setText(String.format(locale, "Costo totale: %s €", tot));
		finito.setOnClickListener(v -> {
			viewModel.checkout(requireActivity());
			NavHostFragment.findNavController(CheckOutPage.this).navigate(R.id.action_checkOutPage_to_homepageFragment);
		});
	}
	
}