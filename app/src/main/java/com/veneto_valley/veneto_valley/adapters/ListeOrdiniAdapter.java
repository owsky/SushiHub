package com.veneto_valley.veneto_valley.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.veneto_valley.veneto_valley.view.ConfirmedOrdersFragment;
import com.veneto_valley.veneto_valley.view.DeliveredOrdersFragment;
import com.veneto_valley.veneto_valley.view.PendingOrdersFragment;

public class ListeOrdiniAdapter extends FragmentStateAdapter {
	
	public ListeOrdiniAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position) {
			case 0:
				return new PendingOrdersFragment();
			case 1:
				return new ConfirmedOrdersFragment();
			default:
				return new DeliveredOrdersFragment();
		}
	}
	
	@Override
	public int getItemCount() {
		return 3;
	}
}
