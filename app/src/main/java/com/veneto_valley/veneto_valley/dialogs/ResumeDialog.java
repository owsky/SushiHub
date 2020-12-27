package com.veneto_valley.veneto_valley.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.HomepageFragmentDirections;

public class ResumeDialog extends DialogFragment {
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		builder.setTitle("Vuoi accedere al tavolo in sospeso?");
		builder.setPositiveButton("Sì", (dialog, which) -> {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
			HomepageFragmentDirections.ActionHomepageFragmentToListaPiattiFragment action = HomepageFragmentDirections.actionHomepageFragmentToListaPiattiFragment();
			action.setCodiceTavolo(sharedPreferences.getString("codice_tavolo", null));
			NavHostFragment.findNavController(requireParentFragment()).navigate(action);
		});
		builder.setNegativeButton("No", (dialog, which) -> dismiss());
		return builder.create();
	}
}