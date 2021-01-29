package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrderDao;
import com.veneto_valley.veneto_valley.model.dao.TableDao;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.model.entities.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class RepositoryTables {
	private final OrderDao orderDao;
	private final TableDao tableDao;
	private final SharedPreferences preferences;
	private LiveData<List<Table>> tablesHistory = null;
	
	public RepositoryTables(Application application) {
		tableDao = AppDatabase.getInstance(application).tavoloDao();
		orderDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public LiveData<List<Table>> getTablesHistory() {
		if (tablesHistory == null)
			tablesHistory = tableDao.getAllButCurrent();
		return tablesHistory;
	}
	
	public LiveData<List<Order>> getOrdersHistory(Table table) {
		return orderDao.getAllByTable(table.id);
	}
	
	public float getMenuPrice(String table) {
		return tableDao.getMenuPrice(table);
	}
	
	public float getExtrasPrice(String table) {
		return orderDao.getTotaleExtra(table);
	}
	
	public void checkoutTable(String id) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Table table = tableDao.getTable(id);
			if (table != null) {
				table.checkedOut = true;
				tableDao.update(table);
			}
		});
	}
	
	public void deleteTable(Table table) {
		Executors.newSingleThreadExecutor().execute(() -> {
			orderDao.deleteByTable(table.id);
			tableDao.delete(table);
		});
	}
	
	// createTable for slaves
	public void createTable(String restaurantId, String restaurantName, String tableCode, float menu) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Table table;
			SharedPreferences.Editor editor = preferences.edit();
			if ((table = tableDao.getTable(tableCode)) != null) {
				table.menuPrice = menu;
				table.restaurant = restaurantId;
				table.name = restaurantName;
				tableDao.update(table);
			} else {
				tableDao.insert(new Table(tableCode, restaurantName, menu));
			}
			if (restaurantId != null && !restaurantId.equals("null"))
				editor.putString("rest_code", restaurantId);
			editor.putString("table_code", tableCode);
			editor.apply();
		});
	}
	
	// createTable for the master
	public void createTable(String restaurantId, String restaurantName, float menu) {
		String codice = UUID.randomUUID().toString();
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("table_code", codice).putBoolean("is_master", true);
		if (restaurantId != null)
			editor.putString("rest_code", restaurantId);
		editor.putString("rest_name", restaurantName).apply();
		Table table = new Table(codice, menu, restaurantId);
		table.name = restaurantName;
		tableDao.insert(table);
	}
	
	// returns the necessary parameters to create a table
	public List<String> getTableInfo() {
		Table current = tableDao.getTable(preferences.getString("table_code", null));
		String idRistorante = preferences.getString("rest_code", null);
		String nomeRistorante = preferences.getString("rest_name", null);
		List<String> info = new ArrayList<>();
		if (current != null) {
			info.add(current.id);
			info.add(Float.toString(current.menuPrice));
			info.add(idRistorante);
			info.add(nomeRistorante);
		}
		return info;
	}
	
	public void deleteAllTables() {
		Executors.newSingleThreadExecutor().execute(tableDao::deleteAllTables);
	}
}
