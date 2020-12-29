package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

import java.util.List;

public class TavoloViewModel extends AndroidViewModel {
	protected RepositoryTavoli repository;
	protected LiveData<List<Tavolo>> tavoli;
	protected LiveData<List<Ordine>> ordini;
	
	public TavoloViewModel(@NonNull Application application) {
		super(application);
		repository = new RepositoryTavoli(application);
		tavoli = repository.getTavoli();
		ordini = repository.getOrdini();
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini() {
		return ordini;
	}
}
