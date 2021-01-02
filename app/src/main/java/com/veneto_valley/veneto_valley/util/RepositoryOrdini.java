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
import com.veneto_valley.veneto_valley.viewmodel.ConfirmedViewModel;

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
	
	public void insert(Ordine ordine) {
		Ordine vecchioOrdine;
		if ((vecchioOrdine = ordineDao.getOrdineByPiatto("pending", tavolo, ordine.piatto)) != null) {
			vecchioOrdine.quantita += ordine.quantita;
			if (!(ordine.desc == null))
				vecchioOrdine.desc = ordine.desc;
			vecchioOrdine.prezzo = ordine.prezzo;
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
	
	public LiveData<List<Ordine>> getAllOrders() {
		return ordineDao.getAllByTable(tavolo);
	}
	
	public LiveData<List<Ordine>> getAllOrdersByUser(long utente) {
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
		
		Connessione connessione = Connessione.getInstance(true, application, tavolo, getCallback());
		connessione.invia(ordine.getBytes());
	}
	
	public void retrieveFromMaster(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.pending;
		update(ordine);
		Connessione connessione = Connessione.getInstance(true, application, tavolo, getCallback());
		connessione.invia(ordine.getBytes());
		//TODO: CONTROLLARE
		update(ordine);
		// TODO implementa undo del master
	}
	
	public void markAsDelivered(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.delivered;
		update(ordine);
		Connessione connessione = Connessione.getInstance(false, application, tavolo, getCallback());
		connessione.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine) {
		ordine.status = Ordine.statusOrdine.confirmed;
		update(ordine);
		Connessione connessione = Connessione.getInstance(false, application, tavolo, getCallback());
		connessione.invia(ordine.getBytes());
	}
	
	public void checkout() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
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
					
					if (ordine.status.equals(Ordine.statusOrdine.confirmed)) {
						ordineDao.insert(ordine);
					} else if (ordine.status.equals(Ordine.statusOrdine.pending)) {
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
