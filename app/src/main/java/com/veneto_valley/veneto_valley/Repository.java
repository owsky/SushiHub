package com.veneto_valley.veneto_valley;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.util.List;

public class Repository {
	private final OrdineDao ordineDao;
	private final LiveData<List<Ordine>> pendingOrders, confirmedOrders, deliveredOrders;
	
	public Repository(Application application, String tavolo) {
		AppDatabase database = AppDatabase.getInstance(application);
		ordineDao = database.ordineDao();
		// TODO: filtro per tavolo
		pendingOrders = ordineDao.getAllbyStatus("pending", tavolo);
		confirmedOrders = ordineDao.getAllbyStatus("confirmed", tavolo);
		deliveredOrders = ordineDao.getAllbyStatus("delivered", tavolo);
	}
	
	public void insert(Ordine ordine) {
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
