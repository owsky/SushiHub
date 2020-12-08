package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.veneto_valley.veneto_valley.adapters.ListeOrdiniAdapter;

import java.util.Objects;

public class ListPiattiFragment extends Fragment {
	
	public ListPiattiFragment() {
		super(R.layout.fragment_list_piatti);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		FloatingActionButton fab = view.findViewById(R.id.fab);
		fab.setOnClickListener(v -> NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_listPiattiFragment_to_aggiungiOrdiniFragment));
		
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
							BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
							badgeDrawable.setBackgroundColor(
									ContextCompat.getColor(requireContext().getApplicationContext(),
											R.color.design_default_color_primary)
							);
							badgeDrawable.setVisible(true);
							break;
						}
						case 1: {
							tab.setText("Confirmed");
							tab.setIcon(R.drawable.ic_confirmed);
							BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
							badgeDrawable.setBackgroundColor(
									ContextCompat.getColor(requireContext().getApplicationContext(),
											R.color.design_default_color_primary)
							);
							badgeDrawable.setVisible(true);
							badgeDrawable.setNumber(8);
							break;
						}
						case 2: {
							tab.setText("Delivered");
							tab.setIcon(R.drawable.ic_delivered);
							BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
							badgeDrawable.setBackgroundColor(
									ContextCompat.getColor(requireContext().getApplicationContext(),
											R.color.design_default_color_primary)
							);
							badgeDrawable.setVisible(true);
							badgeDrawable.setNumber(100);
							badgeDrawable.setMaxCharacterCount(3);
							break;
						}
					}
				}
		);
		tabLayoutMediator.attach();
		
		viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				BadgeDrawable badgeDrawable = Objects.requireNonNull(tabLayout.getTabAt(position)).getOrCreateBadge();
				badgeDrawable.setVisible(false);
			}
		});
	}
}