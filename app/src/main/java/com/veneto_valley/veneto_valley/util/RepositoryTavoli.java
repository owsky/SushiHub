package com.veneto_valley.veneto_valley.util;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.dao.TavoloDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.List;

public class RepositoryTavoli {
	private final TavoloDao tavoloDao;
	private final OrdineDao ordineDao;
	private final LiveData<List<Tavolo>> tavoli;
	private final LiveData<List<Ordine>> ordini;
	
	public RepositoryTavoli(Application application) {
		tavoloDao = AppDatabase.getInstance(application).tavoloDao();
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		tavoli = tavoloDao.getAll();
		ordini = ordineDao.getAll();
	}
	
	public LiveData<List<Tavolo>> getTavoli() {
		return tavoli;
	}
	
	public LiveData<List<Ordine>> getOrdini() {
		return ordini;
	}
}
