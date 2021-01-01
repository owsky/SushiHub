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
	private final SharedPreferences preferences;
	
	public RepositoryTavoli(Application application) {
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
		String curr = preferences.getString("codice_tavolo", null);
		if (curr != null)
			tavoli = tavoloDao.getAllMinusCurr(curr);
		else
			tavoli = tavoloDao.getAll();
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini(Tavolo tavolo) {
		return ordineDao.getAllByTable(tavolo.idTavolo);
	}
	
	public float getCostoMenu(String tavolo) {
		return tavoloDao.getCostoMenu(tavolo);
	}
	
	public float getCostoExtra(String tavolo) {
		return ordineDao.getTotaleExtra(tavolo);
	}
	
	public void deleteTable(Tavolo tavolo) {
		Executors.newSingleThreadExecutor().execute(() -> {
			ordineDao.deleteByTable(tavolo.idTavolo);
			tavoloDao.delete(tavolo);
		});
	}
	
	public void creaTavolo(String codice) {
		Tavolo tavolo = new Tavolo(codice);
		preferences.edit().putString("codice_tavolo", codice).apply();
		Executors.newSingleThreadExecutor().execute(() -> tavoloDao.insert(tavolo));
	}
	
	public void creaTavolo(String codice, int portate, float menu) {
		Tavolo tavolo = new Tavolo(codice, portate, menu);
		preferences.edit().putString("codice_tavolo", codice).apply();
		Executors.newSingleThreadExecutor().execute(() -> tavoloDao.insert(tavolo));
	}
}
