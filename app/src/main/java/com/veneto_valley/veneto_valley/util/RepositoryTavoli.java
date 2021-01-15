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
				tavolo.checkedOut = true;
				tavoloDao.update(tavolo);
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
	public void creaTavolo(String idRistorante, String nomeRistorante, String codice, float menu) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Tavolo tavolo;
			SharedPreferences.Editor editor = preferences.edit();
			if ((tavolo = tavoloDao.getTavolo(codice)) != null) {
				tavolo.costoMenu = menu;
				tavolo.ristorante = idRistorante;
				tavolo.nome = nomeRistorante;
				tavoloDao.update(tavolo);
			} else {
				tavoloDao.insert(new Tavolo(codice, nomeRistorante, menu));
			}
			if (idRistorante != null && !idRistorante.equals("null"))
				editor.putString("codice_ristorante", idRistorante);
			editor.putString("codice_tavolo", codice);
			editor.apply();
		});
	}
	
	// creazione tavolo per il master
	public void creaTavolo(String idRistorante, String nomeRistorante, float menu) {
		String codice = UUID.randomUUID().toString();
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("codice_tavolo", codice).putBoolean("is_master", true);
		if (idRistorante != null)
			editor.putString("codice_ristorante", idRistorante).putString("nome_ristorante", nomeRistorante);
		editor.apply();
		Tavolo tavolo = new Tavolo(codice, menu, idRistorante);
		tavolo.nome = nomeRistorante;
		tavoloDao.insert(tavolo);
	}
	
	// ritorna i parametri di costruzione del tavolo
	public List<String> getInfoTavolo() {
		Tavolo current = tavoloDao.getTavolo(preferences.getString("codice_tavolo", null));
		String idRistorante = preferences.getString("codice_ristorante", null);
		String nomeRistorante = preferences.getString("nome_ristorante", null);
		List<String> info = new ArrayList<>();
		if (current != null) {
			info.add(current.idTavolo);
			info.add(Float.toString(current.costoMenu));
			info.add(idRistorante);
			info.add(nomeRistorante);
		}
		return info;
	}
	
	public void deleteAllTables() {
		Executors.newSingleThreadExecutor().execute(tavoloDao::deleteAllTables);
	}
}
