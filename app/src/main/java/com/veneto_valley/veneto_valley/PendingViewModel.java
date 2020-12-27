package com.veneto_valley.veneto_valley;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.veneto_valley.veneto_valley.adapters.PendingAdapter;

public class PendingViewModel extends ViewModel {
	private final MutableLiveData<PendingAdapter> adapter = new MutableLiveData<>();
	
	public LiveData<PendingAdapter> getAdapter() {
		return adapter;
	}
	
	public void addAdapter(PendingAdapter adapter) {
		this.adapter.setValue(adapter);
	}
}
