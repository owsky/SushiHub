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
import com.veneto_valley.veneto_valley.model.entities.Utente;

import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryTavoli {
	private final Application application;
	private final OrdineDao ordineDao;
	private final TavoloDao tavoloDao;
	private final SharedPreferences preferences;
	private LiveData<List<Tavolo>> tavoli = null;
	
	public RepositoryTavoli(Application application) {
		this.application = application;
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		if (tavoli == null)
			tavoli = tavoloDao.getAllButCurrent();
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini(Tavolo tavolo) {
		return ordineDao.getAllByTable(tavolo.idTavolo);
	}
	
	public float getCostoMenu(String tavolo) {
		return tavoloDao.getCostoMenu(tavolo);
	}
	
	public float getCostoExtra(String tavolo) {
		return ordineDao.getTotaleExtra(tavolo, Utente.getCurrentUser(application));
	}
	
	public void checkoutTavolo(String idTavolo) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Tavolo tavolo = tavoloDao.getTavolo(idTavolo);
			tavolo.checkedOut = true;
			tavoloDao.update(tavolo);
		});
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
	
	public void creaTavolo(String codice, String nome, int portate, float menu) {
		Tavolo tavolo = new Tavolo(codice, nome, portate, menu);
		preferences.edit().putString("codice_tavolo", codice).apply();
		preferences.edit().putBoolean("is_master", true).apply();
		Executors.newSingleThreadExecutor().execute(() -> tavoloDao.insert(tavolo));
	}
}
