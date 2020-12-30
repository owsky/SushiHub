package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.dao.TavoloDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryTavoli {
	private final OrdineDao ordineDao;
	private final TavoloDao tavoloDao;
	private final LiveData<List<Tavolo>> tavoli;
	
	public RepositoryTavoli(Application application) {
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
		String curr = preferences.getString("codice_tavolo", null);
		if (curr != null)
			tavoli = tavoloDao.getAllMinusCurr(curr);
		else
			tavoli = tavoloDao.getAll();
	}
	// TODO: implementare Future
	public LiveData<List<Tavolo>> getTavoli() {
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini(Tavolo tavolo) {
		return ordineDao.getAllByTable(tavolo.idTavolo);
	}
	
	public float getCostoMenu(String tavolo) {
		return tavoloDao.getCostoMenu(tavolo);
	}
	
	public void deleteTable(Tavolo tavolo) {
		Executors.newSingleThreadExecutor().execute(() -> {
			ordineDao.deleteByTable(tavolo.idTavolo);
			tavoloDao.delete(tavolo);
		});
	}
}
