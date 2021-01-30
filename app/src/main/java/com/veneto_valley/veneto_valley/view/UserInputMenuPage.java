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
        TextView dishName = view.findViewById(R.id.dishNameTextview);
        TextView code = view.findViewById(R.id.code);
        TextView price = view.findViewById(R.id.price);
        EditText qta = view.findViewById(R.id.addQuantity);
        qta.setImeOptions(EditorInfo.IME_ACTION_DONE);

        UserInputMenuPageArgs args = UserInputMenuPageArgs.fromBundle(requireArguments());
        Dish dish = args.getDish();
        dishName.setText(dish.name);
        code.setText(dish.id);
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
        df.setMaximumFractionDigits(340);
        String formattedPrice = df.format(dish.price) + " €";
        price.setText(formattedPrice);
        qta.setText("1");

        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            if (qta.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Insert the quantity", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                String table = preferences.getString("table_code", null);
                int quantity = Integer.parseInt(qta.getText().toString());
                String user = preferences.getString("username", null);
                OrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdersViewModel.class, table);
                String cod = code.getText().toString();

                Order order = new Order(table, cod, Order.OrderStatus.pending, user, false);
                order.desc = dishName.getText().toString();
                order.price = dish.price;
                viewModel.insert(order, quantity);
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