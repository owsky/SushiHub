package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.Nearby;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;

public class TabLayoutPage extends Fragment {
	private SharedPreferences preferences;
	
	public TabLayoutPage() {
		super(R.layout.fragment_orders_list);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				ExitDialog dialog = new ExitDialog();
				dialog.show(getParentFragmentManager(), getTag());
			}
		});

		preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String tableCode = preferences.getString("table_code", null);
		OrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdersViewModel.class, tableCode);
		Nearby.getInstance(!preferences.getBoolean("is_master", false), requireActivity().getApplication(), tableCode, viewModel.getCallback());
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		view.findViewById(R.id.fab).setOnClickListener(v -> {
			if (preferences.getString("rest_code", null) != null)
				Navigation.findNavController(v).navigate(R.id.action_listsNav_to_affiliatedUserInputNav);
			else
				Navigation.findNavController(v).navigate(R.id.action_listsNav_to_userInputNav);
		});
		
		// tabs config
		ViewPager2 viewPager2 = view.findViewById(R.id.viewPager);
		viewPager2.setAdapter(new TabLayoutAdapter(this));
		viewPager2.setUserInputEnabled(false);
		TabLayout tabLayout = view.findViewById(R.id.tabLayout);
		TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
				tabLayout, viewPager2, (tab, position) -> {
			switch (position) {
				case 0: {
					tab.setText("Pending");
					tab.setIcon(R.drawable.ic_pending);
					break;
				}
				case 1: {
					tab.setText("Confirmed");
					tab.setIcon(R.drawable.ic_confirmed);
					break;
				}
				case 2: {
					tab.setText("Delivered");
					tab.setIcon(R.drawable.ic_delivered);
					break;
				}
			}
		}
		);
		tabLayoutMediator.attach();
	}
	
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.lista_overflow, menu);
		// if the user isn't the master, it hides the synchronized orders option
		MenuItem item = menu.findItem(R.id.toAllOrders);
		if (!preferences.getBoolean("is_master", false))
			item.setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.showQR) {
			// it sets the safearg so that the QRGenerator won't show the button to procede and
			// the user is forced to use the navigation componento to go back
			TabLayoutPageDirections.ActionListsNavToQRGeneratorNav action =
					TabLayoutPageDirections.actionListsNavToQRGeneratorNav();
			action.setShare(true);
			NavHostFragment.findNavController(this).navigate(action);
		} else if (item.getItemId() == R.id.toAllOrders) {
			TabLayoutPageDirections.ActionListsNavToAllOrders action =
					TabLayoutPageDirections.actionListsNavToAllOrders(ListOrdersPage.ListOrdersType.allOrders);
			NavHostFragment.findNavController(this).navigate(action);
		} else if (item.getItemId() == R.id.toCheckout) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listsNav_to_checkOutNav);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class ExitDialog extends DialogFragment {
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
			builder.setTitle("Do you want to quit the app?");
			builder.setPositiveButton("OK", (dialog, which) -> requireActivity().finish());
			builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());
			return builder.create();
		}
	}
}