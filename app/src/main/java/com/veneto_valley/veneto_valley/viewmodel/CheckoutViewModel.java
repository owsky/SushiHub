package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

public class CheckoutViewModel extends BaseViewModel{
	private RepositoryTavoli repositoryTavoli;
	
	public CheckoutViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		// TODO: inserire codice utente
		ordini = repositoryOrdini.getAllOrders(-1, tavolo);
		repositoryTavoli = new RepositoryTavoli(application);
	}
	
	public float getCostoMenu(String tavolo) {
		return repositoryTavoli.getCostoMenu(tavolo);
	}
}
