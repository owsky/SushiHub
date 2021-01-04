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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class RepositoryTavoli {
	private final OrdineDao ordineDao;
	private final TavoloDao tavoloDao;
	private final SharedPreferences preferences;
	private LiveData<List<Tavolo>> tavoli = null;
	
	public RepositoryTavoli(Application application) {
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		if (tavoli == null)
			//TODO fix query
//			tavoli = tavoloDao.getAllButCurrent();
			tavoli = tavoloDao.getAll();
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
	
	public void checkoutTavolo(String idTavolo) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Tavolo tavolo = tavoloDao.getTavolo(idTavolo);
			if (tavolo != null) {
				if (ordineDao.getAllByTable(tavolo.idTavolo).getValue() == null) {
					tavoloDao.delete(tavolo);
				}else {
					tavolo.checkedOut = true;
					tavoloDao.update(tavolo);
				}
			}
		});
	}
	
	public void deleteTable(Tavolo tavolo) {
		Executors.newSingleThreadExecutor().execute(() -> {
			ordineDao.deleteByTable(tavolo.idTavolo);
			tavoloDao.delete(tavolo);
		});
	}
	
	public void creaTavolo(String codice, int portate, float menu) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Tavolo tavolo;
			if ((tavolo = tavoloDao.getTavolo(codice)) != null) {
				tavolo.costoMenu = menu;
				tavolo.maxPiatti = portate;
				tavoloDao.update(tavolo);
			} else {
				tavolo = new Tavolo(codice, portate, menu);
				preferences.edit().putString("codice_tavolo", codice).apply();
				tavoloDao.insert(tavolo);
			}
		});
	}
	
	public void creaTavolo(int portate, float menu) {
		// operazioni sincrone perché necessarie alla view successiva
		String codice = UUID.randomUUID().toString();
		preferences.edit().putString("codice_tavolo", codice).putBoolean("is_master", true).apply();
		Tavolo tavolo = new Tavolo(codice, portate, menu);
		tavoloDao.insert(tavolo);
	}
	
	public List<String> getInfoTavolo() {
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		Tavolo curr = tavoloDao.getTavolo(codiceTavolo);
		List<String> info = new ArrayList<>();
		info.add(curr.idTavolo);
		info.add(Integer.toString(curr.maxPiatti));
		info.add(Float.toString(curr.costoMenu));
		return info;
	}
}
