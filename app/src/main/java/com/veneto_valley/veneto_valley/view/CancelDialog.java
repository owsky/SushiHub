package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

public class CancelDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Quit without saving?");
        builder.setPositiveButton("OK", (dialog, which) -> NavHostFragment.findNavController(CancelDialog.this).navigateUp());
        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());
        return builder.create();
    }
}
