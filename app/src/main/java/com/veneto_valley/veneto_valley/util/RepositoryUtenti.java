package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.veneto_valley.veneto_valley.model.entities.Utente;

public class RepositoryUtenti {
	private final SharedPreferences preferences;
	
	public RepositoryUtenti(Application application) {
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public void initUtente(String username) {
		preferences.edit().putString("username", username).apply();
	}
	
	public String getUsername() {
		return preferences.getString("username", "username");
	}
}
