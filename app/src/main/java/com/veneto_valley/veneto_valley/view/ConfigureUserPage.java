package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.Misc;

public class ConfigureUserPage extends Fragment {

    public ConfigureUserPage() {
        super(R.layout.fragment_configure_user);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText setUsername = view.findViewById(R.id.configureUserTextView);
        Button button = view.findViewById(R.id.configureUserBtn);

        setUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);

        button.setOnClickListener(v -> {
            String username = setUsername.getText().toString();
            if (username.trim().isEmpty())
                Toast.makeText(requireContext(), "Insert a username", Toast.LENGTH_SHORT).show();
            else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                preferences.edit().putString("username", username).apply();
                Misc.hideKeyboard(requireActivity());
                NavHostFragment.findNavController(this).navigate(R.id.action_configureUserNav_to_tabLayoutNav);
            }
        });
    }
}