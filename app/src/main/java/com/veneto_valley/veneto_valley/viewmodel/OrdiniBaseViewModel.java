package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.util.RepositoryOrdini;

import java.util.List;

public abstract class OrdiniBaseViewModel extends AndroidViewModel {
	protected RepositoryOrdini repositoryOrdini;
	protected LiveData<List<Ordine>> ordini;
	protected String tavolo;
	protected Application application;
	
	public OrdiniBaseViewModel(@NonNull Application application, String tavolo) {
		super(application);
		repositoryOrdini = new RepositoryOrdini(application, tavolo);
		this.tavolo = tavolo;
		this.application = application;
	}
	
	public void insert(Ordine ordine) {
		repositoryOrdini.insert(ordine);
	}
	
	public void update(Ordine ordine) {
		repositoryOrdini.update(ordine);
	}
	
	public void delete(Ordine ordine) {
		repositoryOrdini.delete(ordine);
	}
	
	public String getTavolo() {
		return tavolo;
	}
	
	public LiveData<List<Ordine>> getOrdini() {
		return ordini;
	}
}
