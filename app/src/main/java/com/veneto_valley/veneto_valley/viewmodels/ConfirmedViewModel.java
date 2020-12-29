package com.veneto_valley.veneto_valley.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.Connessione;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

public class ConfirmedViewModel extends BaseViewModel {
	
	public ConfirmedViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repository.getConfirmedOrders();
	}
	
	public void retrieveFromMaster(Ordine ordine, Activity activity) {
		repository.retrieveFromMaster(ordine, activity);
	}
	
	public void markAsDelivered(Ordine ordine, Activity activity) {
		repository.markAsDelivered(ordine, activity);
	}
}