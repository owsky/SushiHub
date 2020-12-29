package com.veneto_valley.veneto_valley.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.db.entities.Ordine;

public class PendingViewModel extends BaseViewModel {
	
	public PendingViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repository.getPendingOrders();
	}
	
	public void sendToMaster(Ordine ordine, Activity activity) {
		repository.sendToMaster(ordine, activity);
	}
}
