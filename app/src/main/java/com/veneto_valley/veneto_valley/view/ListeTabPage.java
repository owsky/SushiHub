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
import com.veneto_valley.veneto_valley.util.Connessione;

public class ListeTabPage extends Fragment {
	private SharedPreferences preferences;
	
	public ListeTabPage() {
		super(R.layout.fragment_lista_piatti);
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
		if (preferences.getBoolean("is_master", false)) {
			String codice_tavolo = preferences.getString("codice_tavolo", null);
			Connessione c = Connessione.getItance(false, this.getActivity(), codice_tavolo);
		}
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		view.findViewById(R.id.fab).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_listPiattiFragment_to_aggiungiOrdiniFragment));
		
		ViewPager2 viewPager2 = view.findViewById(R.id.viewPager);
		viewPager2.setAdapter(new ListeOrdiniAdapter(this));
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
		MenuItem item = menu.findItem(R.id.toAllOrders);
		if (!preferences.getBoolean("is_master", false))
			item.setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.mostraQR) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listaPiattiFragment_to_generaQR);
		} else if (item.getItemId() == R.id.toAllOrders) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listaPiattiFragment_to_allOrders);
		} else if (item.getItemId() == R.id.toCheckout) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listaPiattiFragment_to_checkOutPage2);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class ExitDialog extends DialogFragment {
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
			builder.setTitle("Vuoi uscire dall'applicazione?");
			builder.setPositiveButton("OK", (dialog, which) -> requireActivity().finish());
			builder.setNegativeButton("Annulla", (dialog, which) -> dismiss());
			return builder.create();
		}
	}
}