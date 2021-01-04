package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

import java.util.List;

public class CreaTavoloViewModel extends AndroidViewModel {
	private final RepositoryTavoli repository;
	
	public CreaTavoloViewModel(@NonNull Application application) {
		super(application);
		repository = new RepositoryTavoli(application);
	}
	
	// metodo di creazione tavolo slave
	public void creaTavolo(String codice, int portate, float menu) {
		repository.creaTavolo(codice, portate, menu);
	}
	
	// metodo di creazione tavolo master
	public void creaTavolo(int portate, float menu) {
		repository.creaTavolo(portate, menu);
	}
	
	// getter parametri costruzione tavolo
	public List<String> getInfoTavolo() {
		return repository.getInfoTavolo();
	}
}
