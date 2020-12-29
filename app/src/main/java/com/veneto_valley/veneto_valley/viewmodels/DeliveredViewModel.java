package com.veneto_valley.veneto_valley.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

public class DeliveredViewModel extends BaseViewModel {
	
	public DeliveredViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repository.getDeliveredOrders();
	}
}
