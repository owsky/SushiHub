package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.util.RepositoryUtenti;

public class InitViewModel extends AndroidViewModel {
	final RepositoryUtenti repositoryUtenti;
	
	public InitViewModel(@NonNull Application application) {
		super(application);
		repositoryUtenti = new RepositoryUtenti(application);
	}
	
	// salvo lo username dell'utente
	public void initUtente(String codice) {
		repositoryUtenti.initUtente(codice);
	}
}
