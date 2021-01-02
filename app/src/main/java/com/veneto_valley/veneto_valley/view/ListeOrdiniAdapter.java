package com.veneto_valley.veneto_valley.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;

public class ListeOrdiniAdapter extends FragmentStateAdapter {
	
	public ListeOrdiniAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position) {
			case 0:
				return new ListaPendingPage();
			case 1:
				return new ListaConfirmedOrdersPage();
			default:
				return new ListaDeliveredOrdersPage();
		}
	}
	
	@Override
	public int getItemCount() {
		return 3;
	}
}
