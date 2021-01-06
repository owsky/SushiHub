package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.RepositoryOrdini;
import com.veneto_valley.veneto_valley.util.RepositoryTavoli;
import com.veneto_valley.veneto_valley.view.ListaOrdiniGenericaPage;

import java.util.List;

public class StoricoViewModel extends AndroidViewModel {
	private final RepositoryTavoli repositoryTavoli;
	private LiveData<List<Tavolo>> tavoli = null;
	
	public StoricoViewModel(@NonNull Application application) {
		super(application);
		repositoryTavoli = new RepositoryTavoli(application);
	}
	
	// lazy initialization della repository
	public LiveData<List<Tavolo>> getTavoli() {
		if (tavoli == null)
			tavoli = repositoryTavoli.getTavoliStorico();
		return tavoli;
	}
	
	// lazy initialization del livedata
	public LiveData<List<Ordine>> getOrdini(Tavolo tavolo) {
		return repositoryTavoli.getOrdiniStorico(tavolo);
	}
	
	// cancellazione tavolo dallo storico
	public void deleteTable(Tavolo tavolo) {
		repositoryTavoli.deleteTable(tavolo);
	}
	
	public void deleteAllTables() {
		repositoryTavoli.deleteAllTables();
	}
}
