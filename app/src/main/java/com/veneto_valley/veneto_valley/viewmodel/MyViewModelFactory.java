package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class MyViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
	private final Application application;
	private final String tavolo;
	
	public MyViewModelFactory(@NonNull Application application, String tavolo) {
		super(application);
		this.application = application;
		this.tavolo = tavolo;
	}
	
	// costruisco il viewmodel con parametro tavolo
	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
			try {
				return modelClass.getConstructor(application.getClass(), String.class)
						.newInstance(application, tavolo);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return super.create(modelClass);
	}
}
