package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.util.Repository;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.util.List;

public abstract class BaseViewModel extends AndroidViewModel {
	protected Repository repository;
	protected LiveData<List<Ordine>> ordini;
	
	public BaseViewModel(@NonNull Application application, String tavolo) {
		super(application);
		repository = new Repository(application, tavolo);
	}
	
	public void insert(Ordine ordine) {
		repository.insert(ordine);
	}
	
	public void update(Ordine ordine) {
		repository.update(ordine);
	}
	
	public void delete(Ordine ordine) {
		repository.delete(ordine);
	}
	
	public LiveData<List<Ordine>> getOrdini() {
		return ordini;
	}
}
