package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
	
	public static class ResumeDialog extends DialogFragment {
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
			builder.setTitle("Vuoi accedere al tavolo in sospeso?");
			builder.setPositiveButton("Sì", (dialog, which) -> {
				NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_homepageFragment_to_listaPiattiFragment);
			});
			builder.setNegativeButton("No", (dialog, which) -> {
				// TODO: cancellazione ordini slave
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
				preferences.edit().remove("codice_tavolo").apply();
				dismiss();
			});
			return builder.create();
		}
	}
}