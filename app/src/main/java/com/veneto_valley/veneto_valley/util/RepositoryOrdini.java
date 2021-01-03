package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Utente;

import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryOrdini {
	private final OrdineDao ordineDao;
	private final String tavolo;
	private final Application application;
	private final SharedPreferences preferences;
	private LiveData<List<Ordine>> pendingOrders = null;
	private LiveData<List<Ordine>> confirmedOrders = null;
	private LiveData<List<Ordine>> deliveredOrders = null;
	private LiveData<List<Ordine>> allSynchronized = null;
	
	public RepositoryOrdini(Application application, String tavolo) {
		this.application = application;
		this.tavolo = tavolo;
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public void insert(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Ordine vecchioOrdine;
			if ((vecchioOrdine = ordineDao.contains(Ordine.StatusOrdine.pending, tavolo, ordine.piatto, Utente.getCurrentUser(application))) != null) {
				vecchioOrdine.quantita += ordine.quantita;
				if (!(ordine.desc == null))
					vecchioOrdine.desc = ordine.desc;
				vecchioOrdine.prezzo = ordine.prezzo;
				update(vecchioOrdine);
			} else {
				ordineDao.insert(ordine);
			}
		});
	}
	
	public void update(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.update(ordine));
	}
	
	public void delete(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.delete(ordine));
	}
	
	public LiveData<List<Ordine>> getAllSynchronized() {
		if (allSynchronized == null)
			allSynchronized = ordineDao.getAllSynchronized(tavolo, Utente.getCurrentUser(application));
		return allSynchronized;
	}
	
	public LiveData<List<Ordine>> getPendingOrders() {
		if (pendingOrders == null)
			pendingOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.pending, tavolo, Utente.getCurrentUser(application));
		return pendingOrders;
	}
	
	public LiveData<List<Ordine>> getConfirmedOrders() {
		if (confirmedOrders == null)
			confirmedOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.confirmed, tavolo, Utente.getCurrentUser(application));
		return confirmedOrders;
	}
	
	public LiveData<List<Ordine>> getDeliveredOrders() {
		if (deliveredOrders == null)
			deliveredOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.delivered, tavolo, Utente.getCurrentUser(application));
		return deliveredOrders;
	}
	
	public void sendToMaster(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.confirmed;
		update(ordine);
		
		if (preferences.contains("is_master")) {
			Connessione.getInstance(true, application, tavolo, getCallback())
					.invia(ordine.getBytes());
		}
	}
	
	public void retrieveFromMaster(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.pending;
		update(ordine);
		if (!preferences.contains("is_master")) {
			Connessione.getInstance(true, application, tavolo, getCallback())
					.invia(ordine.getBytes());
		}
	}
	
	public void markAsDelivered(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.delivered;
		update(ordine);
		Connessione.getInstance(false, application, tavolo, getCallback())
				.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.confirmed;
		update(ordine);
		Connessione.getInstance(false, application, tavolo, getCallback())
				.invia(ordine.getBytes());
	}
	
	}
	
	public void checkout() {
		//TODO elimina ordini slave
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("codice_tavolo").apply();
		Connessione connessione = Connessione.getInstance(preferences.getBoolean("is_master", false),
				application, preferences.getString("codice_tavolo", null), getCallback());
		connessione.closeConnection();
	}
	
	public PayloadCallback getCallback() {
		return new PayloadCallback() {
			@Override
			public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
				final byte[] receivedBytes = payload.asBytes();
				Executors.newSingleThreadExecutor().execute(() -> {
					Ordine ordine = Ordine.getFromBytes(receivedBytes);
					
					if (ordine.status.equals(Ordine.StatusOrdine.confirmed)) {
						ordineDao.insert(ordine);
					} else if (ordine.status.equals(Ordine.StatusOrdine.pending)) {
						ordineDao.delete(ordine);
					} else {
						ordineDao.update(ordine);
					}
				});
			}
			
			@Override
			public void onPayloadTransferUpdate(@NonNull String s,
			                                    @NonNull PayloadTransferUpdate payloadTransferUpdate) {
//			if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
//				// Do something with is here...
//			}
			}
		};
	}
}
