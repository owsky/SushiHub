package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;
import java.util.List;

public class Repository {
	private final OrdineDao ordineDao;
	private final LiveData<List<Ordine>> pendingOrders, confirmedOrders, deliveredOrders;
	private final String tavolo;
	
	public Repository(Application application, String tavolo) {
		AppDatabase database = AppDatabase.getInstance(application);
		ordineDao = database.ordineDao();
		this.tavolo = tavolo;
		pendingOrders = ordineDao.getAllbyStatus("pending", tavolo);
		confirmedOrders = ordineDao.getAllbyStatus("confirmed", tavolo);
		deliveredOrders = ordineDao.getAllbyStatus("delivered", tavolo);
	}
	
	public void insert(Ordine ordine) {
		Ordine vecchioOrdine;
		if ((vecchioOrdine = ordineDao.getOrdineByPiatto("pending", tavolo, ordine.piatto)) != null) {
			vecchioOrdine.quantita += ordine.quantita;
			if (!(ordine.desc == null))
				vecchioOrdine.desc = ordine.desc;
			update(vecchioOrdine);
		} else {
			new InsertOrdineThread(ordine, ordineDao).start();
		}
	}
	
	public void update(Ordine ordine) {
		new UpdateOrdineThread(ordine, ordineDao).start();
	}
	
	public void delete(Ordine ordine) {
		new DeleteOrdineThread(ordine, ordineDao).start();
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
	
	public void retrieveFromMaster(Ordine ordine, Activity activity) {
		ordine.status = "pending";
		update(ordine);
		
		// TODO implementa undo del master
	}
	
	public Ordine receiveFromSlave(Activity activity) {
		Connessione connessione = new Connessione(false, activity, tavolo);
		final long[] semaforo = {0};
		final Ordine[] risposta = {null};
		connessione.mPayloadCallback = new PayloadCallback() {
			@Override
			public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
				final   byte[] receivedBytes = payload.asBytes();
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						semaforo[0] =1;
						try {
							risposta[0] =Ordine.getFromBytes(receivedBytes);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						semaforo[0]=0;
					}
				});
			}

			@Override
			public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

			}
		};
		return risposta[0];
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
	
	public static class InsertOrdineThread extends Thread {
		private final Ordine ordine;
		private final OrdineDao dao;
		
		public InsertOrdineThread(Ordine ordine, OrdineDao dao) {
			this.ordine = ordine;
			this.dao = dao;
		}
		
		@Override
		public void run() {
			dao.insertAll(ordine);
		}
	}
	
	public static class UpdateOrdineThread extends Thread {
		private final Ordine ordine;
		private final OrdineDao dao;
		
		public UpdateOrdineThread(Ordine ordine, OrdineDao dao) {
			this.ordine = ordine;
			this.dao = dao;
		}
		
		@Override
		public void run() {
			dao.update(ordine);
		}
	}
	
	public static class DeleteOrdineThread extends Thread {
		private final Ordine ordine;
		private final OrdineDao dao;
		
		public DeleteOrdineThread(Ordine ordine, OrdineDao dao) {
			this.ordine = ordine;
			this.dao = dao;
		}
		
		@Override
		public void run() {
			dao.delete(ordine);
		}
	}
}
