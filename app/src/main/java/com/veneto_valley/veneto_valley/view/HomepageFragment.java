package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;

public class HomepageFragment extends Fragment {

	public HomepageFragment() {
		super(R.layout.fragment_homepage);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.btnUnisciti).setOnClickListener(view1 -> {
			NavHostFragment.findNavController(this).navigate(R.id.action_homepageFragment_to_scanQR);
		});
		view.findViewById(R.id.btnCrea).setOnClickListener(view1 -> {
			NavHostFragment.findNavController(this).navigate(R.id.action_homepageFragment_to_generaQR);
		});
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		if (sharedPreferences.contains("codice_tavolo")) {
			ResumeDialog dialog = new ResumeDialog();
			dialog.show(getParentFragmentManager(), null);
		}
	}
}