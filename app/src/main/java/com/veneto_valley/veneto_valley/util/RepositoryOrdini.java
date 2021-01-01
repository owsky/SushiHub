package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;
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
	
	public void sendToMaster(Ordine ordine, Activity activity) {
		ordine.status = "confirmed";
		update(ordine);
		
		Connessione connessione = new Connessione(true, activity, tavolo);
		try {
			connessione.invia(ordine.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveFromMaster(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "pending";
		update(ordine);
		Connessione connessione = new Connessione(true, activity, tavolo);
		connessione.invia(ordine.getBytes());
		//TODO: CONTROLLARE
		update(ordine);
		// TODO implementa undo del master
	}
	
	public void markAsDelivered(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "delivered";
		update(ordine);
		Connessione connessione = new Connessione(false, activity, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "confirmed";
		update(ordine);
		Connessione connessione = new Connessione(false, activity, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void checkout(Activity activity) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
		//TODO elimina ordini slave
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("codice_tavolo").apply();
		Connessione connessione = new Connessione(preferences.getBoolean("is_master", false),
				activity, preferences.getString("codice_tavolo", null));
		connessione.closeConnection();
	}
}
