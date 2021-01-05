package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

public class ViewModelUtil {
	
	// metodi di costruzione dei viewmodel
	public static <T extends AndroidViewModel> T getViewModel(Activity activity, Class<T> classe) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		if (codiceTavolo != null) {
			MyViewModelFactory factory = new MyViewModelFactory(activity.getApplication(), codiceTavolo);
			return new ViewModelProvider((ViewModelStoreOwner) activity, factory).get(classe);
		} else {
			return new ViewModelProvider((ViewModelStoreOwner) activity).get(classe);
		}
	}
	
	public static void clearViewModels(ViewModelStoreOwner viewModelStoreOwner) {
		viewModelStoreOwner.getViewModelStore().clear();
	}
}
