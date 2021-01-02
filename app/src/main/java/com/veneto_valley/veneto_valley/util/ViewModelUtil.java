package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

public class ViewModelUtil {
	
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
}