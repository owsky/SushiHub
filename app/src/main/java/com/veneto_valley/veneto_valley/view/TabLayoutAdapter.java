package com.veneto_valley.veneto_valley.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabLayoutAdapter extends FragmentStateAdapter {
	
	public TabLayoutAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position) {
			case 0:
				return new ListOrdersPage(ListOrdersPage.ListOrdersType.pending);
			case 1:
				return new ListOrdersPage(ListOrdersPage.ListOrdersType.confirmed);
			default:
				return new ListOrdersPage(ListOrdersPage.ListOrdersType.delivered);
		}
	}
	
	@Override
	public int getItemCount() {
		return 3;
	}
}
