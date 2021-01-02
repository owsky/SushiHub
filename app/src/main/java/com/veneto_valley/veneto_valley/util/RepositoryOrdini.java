package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryOrdini {
	private final OrdineDao ordineDao;
	private final LiveData<List<Ordine>> pendingOrders, confirmedOrders, deliveredOrders, extraOrders;
	private final String tavolo;
	private final Application application;
	
	public RepositoryOrdini(Application application, String tavolo) {
		AppDatabase database = AppDatabase.getInstance(application);
		ordineDao = database.ordineDao();
		this.tavolo = tavolo;
		pendingOrders = ordineDao.getAllbyStatus("pending", tavolo);
		confirmedOrders = ordineDao.getAllbyStatus("confirmed", tavolo);
		deliveredOrders = ordineDao.getAllbyStatus("delivered", tavolo);
		extraOrders = ordineDao.getAllExtra(tavolo);
		this.application = application;
	}
	
	// TODO: fix overwrite extra
	public void insert(Ordine ordine) {
		Ordine vecchioOrdine;
		if ((vecchioOrdine = ordineDao.getOrdineByPiatto("pending", tavolo, ordine.piatto)) != null) {
			vecchioOrdine.quantita += ordine.quantita;
			if (!(ordine.desc == null))
				vecchioOrdine.desc = ordine.desc;
			update(vecchioOrdine);
		} else {
			Executors.newSingleThreadExecutor().execute(() -> ordineDao.insert(ordine));
		}
	}
	
	public void update(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.update(ordine));
	}
	
	public void delete(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.delete(ordine));
	}
	
	public LiveData<List<Ordine>> getAllOrders(long utente, String tavolo) {
		return ordineDao.getAllByUser(utente, tavolo);
	}
	
	public LiveData<List<Ordine>> getPendingOrders() {
		return pendingOrders;
	}
	
	public LiveData<List<Ordine>> getConfirmedOrders() {
		return confirmedOrders;
	}
	
	public LiveData<List<Ordine>> getDeliveredOrders() {
		return deliveredOrders;
	}
	
	public LiveData<List<Ordine>> getAllExtra() {
		return extraOrders;
	}
	
	public void sendToMaster(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.confirmed;
		update(ordine);
		
		Connessione connessione = Connessione.getInstance(true, application, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void retrieveFromMaster(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.pending;
		update(ordine);
		Connessione connessione = Connessione.getInstance(true, application, tavolo);
		connessione.invia(ordine.getBytes());
		//TODO: CONTROLLARE
		update(ordine);
		// TODO implementa undo del master
	}
	
	public void markAsDelivered(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.delivered;
		update(ordine);
		Connessione connessione = Connessione.getInstance(false, application, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.confirmed;
		update(ordine);
		Connessione connessione = Connessione.getInstance(false, application, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void checkout() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
		//TODO elimina ordini slave
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("codice_tavolo").apply();
		Connessione connessione = Connessione.getInstance(preferences.getBoolean("is_master", false),
				application, preferences.getString("codice_tavolo", null));
		connessione.closeConnection();
	}
}
