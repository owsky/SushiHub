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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class RepositoryTavoli {
	private final OrdineDao ordineDao;
	private final TavoloDao tavoloDao;
	private final SharedPreferences preferences;
	private LiveData<List<Tavolo>> tavoliStorico = null;
	
	public RepositoryTavoli(Application application) {
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public LiveData<List<Tavolo>> getTavoliStorico() {
		if (tavoliStorico == null)
			tavoliStorico = tavoloDao.getAllButCurrent();
		return tavoliStorico;
	}
	
	// costruisce al volo un livedata per lo storico
	public LiveData<List<Ordine>> getOrdiniStorico(Tavolo tavolo) {
		return ordineDao.getAllByTable(tavolo.idTavolo);
	}
	
	// metodi per checkout
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
				if (ordineDao.getAllByTable(tavolo.idTavolo) == null) {
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
	
	// creazione tavolo per gli slave
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
	
	// creazione tavolo per il master
	public void creaTavolo(int portate, float menu) {
		// operazioni sincrone perché necessarie alla view successiva
		// TODO: implementare future
		String codice = UUID.randomUUID().toString();
		preferences.edit().putString("codice_tavolo", codice).putBoolean("is_master", true).apply();
		Tavolo tavolo = new Tavolo(codice, portate, menu);
		tavoloDao.insert(tavolo);
	}
	
	// ritorna i parametri di costruzione del tavolo
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
