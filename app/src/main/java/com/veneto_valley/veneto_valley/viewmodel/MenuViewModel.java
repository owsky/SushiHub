package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.model.entities.Category;
import com.veneto_valley.veneto_valley.model.entities.Restaurant;
import com.veneto_valley.veneto_valley.util.RepositoryMenu;
import com.veneto_valley.veneto_valley.util.RepositoryRestaurants;
import com.veneto_valley.veneto_valley.view.ListRestaurantsAdapter;
import com.veneto_valley.veneto_valley.view.MenuAdapter;

import java.util.List;

public class MenuViewModel extends AndroidViewModel {
	private RepositoryRestaurants repositoryRestaurants = null;
	private RepositoryMenu repositoryMenu = null;
	
	public MenuViewModel(@NonNull Application application) {
		super(application);
	}
	
	public List<Restaurant> getRestaurants(ListRestaurantsAdapter adapter) {
		if (repositoryRestaurants == null)
			repositoryRestaurants = new RepositoryRestaurants(adapter);
		return repositoryRestaurants.restaurants;
	}
	
	public List<Category> getCategories(MenuAdapter adapter, String id) {
		if (repositoryMenu == null)
			repositoryMenu = new RepositoryMenu(adapter, id);
		return repositoryMenu.getCategory();
	}
}
