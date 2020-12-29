package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ExitDialog extends DialogFragment {
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
		builder.setTitle("Vuoi uscire dall'applicazione?");
		builder.setPositiveButton("OK", (dialog, which) -> requireActivity().finish());
		builder.setNegativeButton("Annulla", (dialog, which) -> dismiss());
		return builder.create();
	}
}
