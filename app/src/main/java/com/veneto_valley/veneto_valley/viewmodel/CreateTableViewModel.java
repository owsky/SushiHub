package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.util.RepositoryTables;

import java.util.List;

public class CreateTableViewModel extends AndroidViewModel {
	private final RepositoryTables repository;
	
	public CreateTableViewModel(@NonNull Application application) {
		super(application);
		repository = new RepositoryTables(application);
	}
	
	// createTable for the master
	public void createTable(String id, String name, float menu) {
		repository.createTable(id, name, menu);
	}
	
	// createTable for the slaves
	public void createTable(String id, String name, String tableCode, float menu) {
		repository.createTable(id, name, tableCode, menu);
	}
	
	// it returns the necessary parameters to create a new table
	public List<String> getInfoTavolo() {
		return repository.getTableInfo();
	}
}
