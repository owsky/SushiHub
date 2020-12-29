package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
	private Application application;
	private String tavolo;
	

	public MyViewModelFactory(@NonNull Application application, String tavolo) {
		super(application);
		this.application = application;
		this.tavolo = tavolo;
	}
	
	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass == PendingViewModel.class)
			return (T) new PendingViewModel(application, tavolo);
		else if (modelClass == DeliveredViewModel.class)
			return (T) new DeliveredViewModel(application, tavolo);
		else
			return (T) new ConfirmedViewModel(application, tavolo);
	}
}
