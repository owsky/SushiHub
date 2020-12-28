package com.veneto_valley.veneto_valley;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.veneto_valley.veneto_valley.adapters.ConfirmedAdapter;
import com.veneto_valley.veneto_valley.adapters.DeliveredAdapter;
import com.veneto_valley.veneto_valley.adapters.PendingAdapter;

public class AdaptersViewModel extends ViewModel {
	private final MutableLiveData<PendingAdapter> pendingAdapter = new MutableLiveData<>();
	private final MutableLiveData<ConfirmedAdapter> confirmedAdapter = new MutableLiveData<>();
	private final MutableLiveData<DeliveredAdapter> deliveredAdapter = new MutableLiveData<>();
	
	public LiveData<PendingAdapter> getPendingAdapter() {
		return pendingAdapter;
	}
	
	public void addPendingAdapter(PendingAdapter adapter) {
		this.pendingAdapter.setValue(adapter);
	}
	
	public LiveData<ConfirmedAdapter> getConfirmedAdapter() {
		return confirmedAdapter;
	}
	
	public void addConfirmedAdapter(ConfirmedAdapter adapter) {
		this.confirmedAdapter.setValue(adapter);
	}
	
	public LiveData<DeliveredAdapter> getDeliveredAdapter() {
		return deliveredAdapter;
	}
	
	public void addDeliveredAdapter(DeliveredAdapter adapter) {
		this.deliveredAdapter.setValue(adapter);
	}
}
