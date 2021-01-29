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
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;

public class HomePage extends Fragment {
	
	public HomePage() {
		super(R.layout.fragment_homepage);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.btnJoin)
				.setOnClickListener(view1 -> NavHostFragment.findNavController(this)
						.navigate(R.id.action_homepageNav_to_scanQRNav));
		view.findViewById(R.id.btnCreate)
				.setOnClickListener(view1 -> NavHostFragment.findNavController(this)
						.navigate(R.id.action_homepageNav_to_appModeNav));
		
		// Asks the users whether they want to resume an unfinished session
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		if (sharedPreferences.contains("codice_tavolo")) {
			ResumeDialog dialog = new ResumeDialog(sharedPreferences.getString("codice_tavolo", null));
			dialog.show(getParentFragmentManager(), null);
		}
	}
	
	public static class ResumeDialog extends DialogFragment {
		private final String tableCode;
		
		public ResumeDialog(String tableCode) {
			this.tableCode = tableCode;
		}
		
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
			builder.setTitle("Do you want to access the last unfinished session?");
			builder.setPositiveButton("Yes", (dialog, which) -> NavHostFragment.
					findNavController(requireParentFragment()).navigate(R.id.listsNav));
			builder.setNegativeButton("No", (dialog, which) -> {
				ViewModelUtil.getViewModel(requireActivity(), OrdersViewModel.class, tableCode)
						.checkout(requireActivity());
				dismiss();
			});
			return builder.create();
		}
	}
}