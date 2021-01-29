package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Restaurant;
import com.veneto_valley.veneto_valley.util.Misc;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreateTableViewModel;

public class ConfigureTablePage extends Fragment {
	private CreateTableViewModel viewModel;
	
	public ConfigureTablePage() {
		super(R.layout.fragment_configure_table);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = ViewModelUtil.getViewModel(requireActivity(), CreateTableViewModel.class);
		if (getArguments() != null) {
			ConfigureTablePageArgs args = ConfigureTablePageArgs.fromBundle(getArguments());
			Restaurant r;
			if ((r = args.getRestaurant()) != null) {
				viewModel.createTable(r.id, r.name, r.menuPrice);
				NavHostFragment.findNavController(this).navigate(R.id.action_configureTableNav_to_QRGeneratorNav);
			}
		}
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EditText name = view.findViewById(R.id.configureName);
		EditText menuPrice = view.findViewById(R.id.menuPrice);
		Button done = view.findViewById(R.id.configureDone);
		
		menuPrice.setImeOptions(EditorInfo.IME_ACTION_DONE);
		done.setOnClickListener(v -> {
			if (TextUtils.isEmpty(name.getText()))
				Toast.makeText(requireContext(), "Insert the restaurant's name", Toast.LENGTH_SHORT).show();
			else if (TextUtils.isEmpty(menuPrice.getText()))
				Toast.makeText(requireContext(), "Insert the Menu's price", Toast.LENGTH_SHORT).show();
			else {
				float price = Float.parseFloat(menuPrice.getText().toString());
				String restaurantName = name.getText().toString();
				viewModel.createTable(null, restaurantName, price);
				Misc.hideKeyboard(requireActivity());
				NavHostFragment.findNavController(this).navigate(R.id.action_configureTableNav_to_QRGeneratorNav);
			}
		});
	}
}