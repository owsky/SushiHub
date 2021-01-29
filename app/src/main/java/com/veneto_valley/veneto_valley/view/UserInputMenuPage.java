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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Dish;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class UserInputMenuPage extends Fragment {
	
	public UserInputMenuPage() {
		super(R.layout.fragment_user_input_menu);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView nomePiatto = view.findViewById(R.id.dishNameTextview);
		TextView codice = view.findViewById(R.id.code);
		TextView prezzo = view.findViewById(R.id.price);
		EditText qta = view.findViewById(R.id.addQuantity);
		qta.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		UserInputMenuPageArgs args = UserInputMenuPageArgs.fromBundle(requireArguments());
		Dish dish = args.getDish();
		nomePiatto.setText(dish.name);
		codice.setText(dish.id);
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
		df.setMaximumFractionDigits(340);
		String prezzoFormattato = df.format(dish.price) + " €";
		prezzo.setText(prezzoFormattato);
		qta.setText("1");
		
		Button salva = view.findViewById(R.id.save);
		salva.setOnClickListener(v -> {
			if (qta.getText().toString().isEmpty()) {
				Toast.makeText(requireContext(), "Insert the quantity", Toast.LENGTH_SHORT).show();
			} else {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
				String tavolo = preferences.getString("table_code", null);
				int quantita = Integer.parseInt(qta.getText().toString());
				String utente = preferences.getString("username", null);
				OrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdersViewModel.class, tavolo);
				String cod = codice.getText().toString();
				
				Order order = new Order(tavolo, cod, Order.OrderStatus.pending, utente, false);
				order.desc = nomePiatto.getText().toString();
				order.price = dish.price;
				viewModel.insert(order, quantita);
				Snackbar.make(requireActivity().findViewById(android.R.id.content),
						"Undo?", BaseTransientBottomBar.LENGTH_LONG)
						.setAction("Undo", vi -> viewModel.undoInsert())
						.setAnchorView(requireActivity().findViewById(R.id.save))
						.show();
				NavHostFragment.findNavController(this).navigate(R.id.action_userInputMenuNav_to_listsNav);
			}
		});
	}
}