package com.veneto_valley.veneto_valley.view;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.Connessione;

public class ListPiattiFragment extends Fragment {
	private SharedPreferences preferences;
	
	public ListPiattiFragment() {
		super(R.layout.fragment_list_piatti);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				ExitDialog dialog = new ExitDialog();
				dialog.show(getParentFragmentManager(), null);
			}
		});
		preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		if (preferences.getBoolean("is_master", false)) {
			String codice_tavolo = preferences.getString("codice_tavolo", null);
			Connessione c = new Connessione(false, this.getActivity(), codice_tavolo);
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
		if (preferences.getBoolean("is_master", false))
			inflater.inflate(R.menu.lista_master_overflow, menu);
		else
			inflater.inflate(R.menu.lista_overflow, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.mostraQR) {
			if (preferences.contains("codice_tavolo")) {
				ListPiattiFragmentDirections.ActionListaPiattiFragmentToGeneraQR action = ListPiattiFragmentDirections.actionListaPiattiFragmentToGeneraQR();
				action.setCodiceTavolo(preferences.getString("codice_tavolo", null));
				NavHostFragment.findNavController(this).navigate(action);
			} else {
				NavHostFragment.findNavController(this).navigate(R.id.homepageFragment);
			}
		} else if (item.getItemId() == R.id.toExtra) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listaPiattiFragment_to_extra);
		} else if (item.getItemId() == R.id.toAllOrders) {
			NavHostFragment.findNavController(this).navigate(R.id.action_listaPiattiFragment_to_allOrders);
		}
		return super.onOptionsItemSelected(item);
	}
}