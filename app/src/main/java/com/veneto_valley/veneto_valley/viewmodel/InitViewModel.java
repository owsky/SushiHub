package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.util.RepositoryUtenti;

public class InitViewModel extends AndroidViewModel {
	RepositoryUtenti repositoryUtenti;
	
	public InitViewModel(@NonNull Application application) {
		super(application);
		repositoryUtenti = new RepositoryUtenti(application);
	}
	
	public void initUtente(String codice) {
		repositoryUtenti.initUtente(codice);
	}
	
	public String getOwner() {
		return repositoryUtenti.getCurrentUser().idUtente;
	}
}
