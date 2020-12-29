package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

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
	private final Application application;
	
	public Repository(Application application, String tavolo) {
		AppDatabase database = AppDatabase.getInstance(application);
		ordineDao = database.ordineDao();
		this.tavolo = tavolo;
		this.application = application;
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
			new UpdateOrdineAsyncTask(ordineDao).execute(vecchioOrdine);
		} else
			new InsertOrdineAsyncTask(ordineDao).execute(ordine);
	}
	
	public void update(Ordine ordine) {
		new UpdateOrdineAsyncTask(ordineDao).execute(ordine);
	}
	
	public void delete(Ordine ordine) {
		new DeleteOrdineAsyncTask(ordineDao).execute(ordine);
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
		new UpdateOrdineAsyncTask(ordineDao).execute(ordine);
		
		Connessione connessione = new Connessione(true, activity, tavolo);
		try {
			connessione.invia(ordine.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveFromMaster(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "pending";
		new UpdateOrdineAsyncTask(ordineDao).execute(ordine);
		Connessione connessione = new Connessione(true, activity, tavolo);
		connessione.invia(ordine.getBytes());
		//TODO: CONTROLLARE
	}
	
	public void markAsDelivered(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "delivered";
		new UpdateOrdineAsyncTask(ordineDao).execute(ordine);
		Connessione connessione = new Connessione(false, activity, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine, Activity activity) throws IOException {
		ordine.status = "confirmed";
		new UpdateOrdineAsyncTask(ordineDao).execute(ordine);
		Connessione connessione = new Connessione(false, activity, tavolo);
		connessione.invia(ordine.getBytes());
	}
	
	// TODO: sostituire componenti deprecati
	private static class InsertOrdineAsyncTask extends AsyncTask<Ordine, Void, Void> {
		private final OrdineDao ordineDao;
		
		private InsertOrdineAsyncTask(OrdineDao ordineDao) {
			this.ordineDao = ordineDao;
		}
		
		@Override
		protected Void doInBackground(Ordine... ordini) {
			ordineDao.insertAll(ordini[0]);
			return null;
		}
	}
	
	private static class UpdateOrdineAsyncTask extends AsyncTask<Ordine, Void, Void> {
		private final OrdineDao ordineDao;
		
		private UpdateOrdineAsyncTask(OrdineDao ordineDao) {
			this.ordineDao = ordineDao;
		}
		
		@Override
		protected Void doInBackground(Ordine... ordini) {
			ordineDao.update(ordini[0]);
			return null;
		}
	}
	
	private static class DeleteOrdineAsyncTask extends AsyncTask<Ordine, Void, Void> {
		private final OrdineDao ordineDao;
		
		private DeleteOrdineAsyncTask(OrdineDao ordineDao) {
			this.ordineDao = ordineDao;
		}
		
		@Override
		protected Void doInBackground(Ordine... ordini) {
			ordineDao.delete(ordini[0]);
			return null;
		}
	}
}
