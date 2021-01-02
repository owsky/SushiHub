package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;

public class ConfirmedViewModel extends OrdiniBaseViewModel {
	
	public ConfirmedViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repositoryOrdini.getConfirmedOrders();
	}
	
	public void retrieveFromMaster(Ordine ordine) {
		repositoryOrdini.retrieveFromMaster(ordine);
	}
	
	public void markAsDelivered(Ordine ordine) {
		repositoryOrdini.markAsDelivered(ordine);
	}
}