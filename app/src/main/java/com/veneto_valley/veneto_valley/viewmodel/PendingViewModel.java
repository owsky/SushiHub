package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

public class PendingViewModel extends OrdiniBaseViewModel {
	
	public PendingViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repositoryOrdini.getPendingOrders();
	}
	
	public void sendToMaster(Ordine ordine) {
		repositoryOrdini.sendToMaster(ordine);
	}
}
