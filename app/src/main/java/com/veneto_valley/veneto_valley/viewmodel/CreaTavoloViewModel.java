package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

public class CreaTavoloViewModel extends AndroidViewModel {
	private final RepositoryTavoli repository;
	
	public CreaTavoloViewModel(@NonNull Application application) {
		super(application);
		repository = new RepositoryTavoli(application);
	}
	
	//TODO: se sei slave ricevi tavolo da master
	public void creaTavolo(String codice) {
		repository.creaTavolo(codice);
	}
	
	public void creaTavolo(String codice, String nome, int portate, float menu) {
		repository.creaTavolo(codice, nome, portate, menu);
	}
}
