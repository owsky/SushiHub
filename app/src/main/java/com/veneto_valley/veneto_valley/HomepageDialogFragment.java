package com.veneto_valley.veneto_valley;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class HomepageDialogFragment extends DialogFragment {
	public HomepageDialogFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public static HomepageDialogFragment newInstance(String title) {
		HomepageDialogFragment frag = new HomepageDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		assert getArguments() != null;
		String title = getArguments().getString("title");
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
		builder.setTitle(title);
		builder.setMessage("Are you done?");
		builder.setPositiveButton("Done", (dialog, which) -> requireActivity().finish());
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
