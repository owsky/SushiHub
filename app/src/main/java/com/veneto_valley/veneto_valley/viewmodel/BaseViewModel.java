package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.RepositoryOrdini;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.util.List;

public abstract class BaseViewModel extends AndroidViewModel {
	protected RepositoryOrdini repositoryOrdini;
	protected LiveData<List<Ordine>> ordini;
	
	public BaseViewModel(@NonNull Application application, String tavolo) {
		super(application);
		repositoryOrdini = new RepositoryOrdini(application, tavolo);
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
	
	public LiveData<List<Ordine>> getOrdini() {
		return ordini;
	}
}
