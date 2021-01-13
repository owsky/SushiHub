package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RepositoryUtenti {
	private final SharedPreferences preferences;
	
	public RepositoryUtenti(Application application) {
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	// salva un record dello username dell'utente locale nelle shared preference
	public void initUtente(String username) {
		preferences.edit().putString("username", username).apply();
	}
}
