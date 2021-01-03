package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

public class AllOrdersViewModel extends OrdiniBaseViewModel {
	
	public AllOrdersViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		ordini = repositoryOrdini.getAllSynchronized();
	}
}
