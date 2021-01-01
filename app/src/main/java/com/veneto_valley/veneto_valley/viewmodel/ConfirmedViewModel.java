package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;

public class ConfirmedViewModel extends BaseViewModel {
	
	public ConfirmedViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repositoryOrdini.getConfirmedOrders();
	}
	
	public void retrieveFromMaster(Ordine ordine, Activity activity) throws IOException, InterruptedException {
		repositoryOrdini.retrieveFromMaster(ordine, activity);
	}
	
	public void markAsDelivered(Ordine ordine, Activity activity) throws IOException, InterruptedException {
		repositoryOrdini.markAsDelivered(ordine, activity);
	}
}