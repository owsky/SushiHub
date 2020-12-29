package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

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