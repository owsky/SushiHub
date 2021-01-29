package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.util.Misc;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;

public class UserInputPage extends Fragment {
	private EditText code, desc, quantity, price;
	private TextView priceTextView;
	private boolean isExtra;
	
	public UserInputPage() {
		super(R.layout.fragment_user_input);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				CancelDialog dialog = new CancelDialog();
				dialog.show(getParentFragmentManager(), getTag());
			}
		});
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		code = view.findViewById(R.id.addCode);
		desc = view.findViewById(R.id.addDesc);
		quantity = view.findViewById(R.id.addQuantity);
		price = view.findViewById(R.id.addPrice);
		priceTextView = view.findViewById(R.id.priceTextView);
		Button saveAndQuit = view.findViewById(R.id.saveAndQuit);
		Button saveAndNew = view.findViewById(R.id.saveAndNew);
		SwitchMaterial switchMaterial = view.findViewById(R.id.switchExtra);
		switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> flipExtra());
		
		// it checks whether the user wants to insert a new order or edit an existing one, then it builds a view accordingly
		if (!isExtra)
			desc.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		saveAndQuit.setOnClickListener(v -> {
			if (saveOrder())
				NavHostFragment.findNavController(UserInputPage.this).navigateUp();
		});
		
		saveAndNew.setOnClickListener(v -> {
			if (saveOrder()) {
				code.setText(null);
				desc.setText(null);
				quantity.setText(null);
				price.setText(null);
				code.requestFocus();
			}
		});
	}
	
	private boolean saveOrder() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		OrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(),
				OrdersViewModel.class, preferences.getString("table_code", null));
		String tableCode = viewModel.getTable();
		String dishCode = code.getText().toString();
		Order.OrderStatus status = Order.OrderStatus.pending;
		String description = desc.getText().toString();
		String extraPrice = price.getText().toString();
		
		if (dishCode.trim().isEmpty())
			Toast.makeText(requireContext(), "Insert the dish code", Toast.LENGTH_SHORT).show();
		else if (quantity.getText().toString().isEmpty())
			Toast.makeText(requireContext(), "Insert the quantity", Toast.LENGTH_SHORT).show();
		else {
			// it uses the shared prefs to get the username and then it calls on the viewmodel to create a new order
			String username = preferences.getString("username", "username");
			Order newOrder = new Order(tableCode, dishCode, status, username, false);
			if (!description.trim().isEmpty())
				newOrder.desc = description;
			if (!extraPrice.trim().isEmpty())
				newOrder.price = Float.parseFloat(extraPrice);
			viewModel.insert(newOrder, Integer.parseInt(quantity.getText().toString()));
			Snackbar.make(requireActivity().findViewById(android.R.id.content),
					"Undo?", BaseTransientBottomBar.LENGTH_LONG)
					.setAction("Undo", v -> viewModel.undoInsert())
					.setAnchorView(requireActivity().findViewById(R.id.saveAndQuit))
					.show();
			return true;
		}
		return false;
	}
	
	private void flipExtra() {
		Misc.hideKeyboard(requireActivity());
		if (isExtra) {
			priceTextView.setVisibility(View.INVISIBLE);
			price.setVisibility(View.INVISIBLE);
			isExtra = false;
			desc.setImeOptions(EditorInfo.IME_ACTION_DONE);
		} else {
			priceTextView.setVisibility(View.VISIBLE);
			price.setVisibility(View.VISIBLE);
			isExtra = true;
			desc.setNextFocusDownId(price.getId());
			desc.setImeOptions(EditorInfo.TYPE_NULL);
		}
	}
}