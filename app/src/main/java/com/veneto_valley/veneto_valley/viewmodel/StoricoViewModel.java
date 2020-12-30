package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

import java.util.List;

public class StoricoViewModel extends AndroidViewModel {
	private final RepositoryTavoli repository;
	private final LiveData<List<Tavolo>> tavoli;
	
	public StoricoViewModel(@NonNull Application application) {
		super(application);
		repository = new RepositoryTavoli(application);
		tavoli = repository.getTavoli();
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini(Tavolo tavolo) {
		return repository.getOrdini(tavolo);
	}
	
	public void deleteTable(Tavolo tavolo) {
		repository.deleteTable(tavolo);
	}
}
