package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

public class ViewModelUtil {
	
	// metodi di costruzione dei viewmodel
	public static <T extends AndroidViewModel> T getViewModel(Activity activity, Class<T> classe) {
		return new ViewModelProvider((ViewModelStoreOwner) activity).get(classe);
	}
	
	public static <T extends AndroidViewModel> T getViewModel(Activity activity, Class<T> classe, String parameter) {
		MyViewModelFactory factory = new MyViewModelFactory(activity.getApplication(), parameter);
		return new ViewModelProvider((ViewModelStoreOwner) activity, factory).get(classe);
	}
	
	public static void clearViewModels(ViewModelStoreOwner viewModelStoreOwner) {
		viewModelStoreOwner.getViewModelStore().clear();
	}
}
