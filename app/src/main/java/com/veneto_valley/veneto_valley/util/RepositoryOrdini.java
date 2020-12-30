package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;
import java.util.List;

public class RepositoryOrdini {
	private final OrdineDao ordineDao;
	private final LiveData<List<Ordine>> pendingOrders, confirmedOrders, deliveredOrders;
	private final String tavolo;
	
	public RepositoryOrdini(Application application, String tavolo) {
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
			new OperazioniThread(() -> ordineDao.insertAll(ordine)).start();
		}
	}
	
	public void update(Ordine ordine) {
		new OperazioniThread(() -> ordineDao.update(ordine)).start();
	}
	
	public void delete(Ordine ordine) {
		new OperazioniThread(() -> ordineDao.delete(ordine)).start();
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
	
	public static class OperazioniThread extends Thread {
		private final Runnable runnable;
		
		public OperazioniThread(Runnable runnable) {
			this.runnable = runnable;
		}
		
		@Override
		public void run() {
			runnable.run();
		}
	}
}
