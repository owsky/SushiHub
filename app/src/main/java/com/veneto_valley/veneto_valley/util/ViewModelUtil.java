package com.veneto_valley.veneto_valley.util;

import android.app.Activity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

public class ViewModelUtil {
	
	public static <T extends AndroidViewModel> T getViewModel(Activity activity, Class<T> tClass) {
		return new ViewModelProvider((ViewModelStoreOwner) activity).get(tClass);
	}
	
	public static <T extends AndroidViewModel> T getViewModel(Activity activity, Class<T> tClass, String parameter) {
		MyViewModelFactory factory = new MyViewModelFactory(activity.getApplication(), parameter);
		return new ViewModelProvider((ViewModelStoreOwner) activity, factory).get(tClass);
	}
	
	public static void clearViewModels(ViewModelStoreOwner viewModelStoreOwner) {
		viewModelStoreOwner.getViewModelStore().clear();
	}
}
