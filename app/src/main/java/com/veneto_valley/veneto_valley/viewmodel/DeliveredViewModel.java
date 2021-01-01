package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.io.IOException;

public class DeliveredViewModel extends OrdiniBaseViewModel {
	
	public DeliveredViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repositoryOrdini.getDeliveredOrders();
	}
	
	public void markAsNotDelivered(Ordine ordine, Activity activity) throws IOException, InterruptedException {
		repositoryOrdini.markAsNotDelivered(ordine, activity);
	}
}
