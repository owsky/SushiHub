package com.veneto_valley.veneto_valley.util;

import android.app.Application;

import com.veneto_valley.veneto_valley.model.entities.Utente;

public class RepositoryUtenti {
	private final Application application;
	
	public RepositoryUtenti(Application application) {
		this.application = application;
	}
	
	public void initUtente(String codice) {
		if (Utente.ownerId == null) {
			Utente.ownerId = codice;
		}
	}
	
	public String getCurrentUser() {
		return Utente.getCurrentUser(application);
	}
}
