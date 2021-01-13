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
	
	// creazione tavolo master
	public void creaTavolo(String idRistorante, float menu) {
		repository.creaTavolo(idRistorante, menu);
	}
	
	// creazione tavolo slave
	public void creaTavolo(String idRistorante, String codiceTavolo, float menu) {
		repository.creaTavolo(idRistorante, codiceTavolo, menu);
	}
	
	// getter parametri costruzione tavolo
	public List<String> getInfoTavolo() {
		return repository.getInfoTavolo();
	}
}
