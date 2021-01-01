package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.veneto_valley.veneto_valley.util.RepositoryTavoli;

public class CheckoutViewModel extends OrdiniBaseViewModel {
	private final RepositoryTavoli repositoryTavoli;
	
	public CheckoutViewModel(@NonNull Application application, String tavolo) {
		super(application, tavolo);
		// TODO: inserire codice utente
		ordini = repositoryOrdini.getAllOrders(-1, tavolo);
		repositoryTavoli = new RepositoryTavoli(application);
	}
	
	public float getCostoMenu() {
		return repositoryTavoli.getCostoMenu(tavolo);
	}
	
	public float getCostoExtra() {
		return repositoryTavoli.getCostoExtra(tavolo);
	}
	
	public void checkout() {
		repositoryOrdini.checkout();
	}
}
