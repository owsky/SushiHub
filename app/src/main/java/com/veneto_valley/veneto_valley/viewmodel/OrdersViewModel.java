package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.nearby.connection.PayloadCallback;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.util.RepositoryOrders;
import com.veneto_valley.veneto_valley.util.RepositoryTables;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.view.ListOrdersPage;
import com.veneto_valley.veneto_valley.view.OrdersAdapter;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
	private final RepositoryOrders repositoryOrders;
	private final RepositoryTables repositoryTables;
	private final String table;
	private LiveData<List<Order>> pending = null, confirmed = null, delivered = null, allOrders = null;
	
	public OrdersViewModel(@NonNull Application application, String table) {
		super(application);
		repositoryOrders = new RepositoryOrders(application);
		repositoryTables = new RepositoryTables(application);
		this.table = table;
	}
	
	public LiveData<List<Order>> getOrders(ListOrdersPage.ListOrdersType listOrdersType) {
		if (listOrdersType == ListOrdersPage.ListOrdersType.pending)
			return getPendingOrders();
		else if (listOrdersType == ListOrdersPage.ListOrdersType.confirmed)
			return getConfirmed();
		else if (listOrdersType == ListOrdersPage.ListOrdersType.delivered)
			return getDelivered();
		else
			return getAllSynchronized();
	}
	
	public LiveData<List<Order>> getPendingOrders() {
		if (pending == null)
			pending = repositoryOrders.getPendingOrders(table);
		return pending;
	}
	
	public LiveData<List<Order>> getConfirmed() {
		if (confirmed == null)
			confirmed = repositoryOrders.getConfirmedOrders(table);
		return confirmed;
	}
	
	public LiveData<List<Order>> getDelivered() {
		if (delivered == null)
			delivered = repositoryOrders.getDeliveredOrders(table);
		return delivered;
	}
	
	public LiveData<List<Order>> getAllSynchronized() {
		if (allOrders == null)
			allOrders = repositoryOrders.getAllSynchronized(table);
		return allOrders;
	}
	
	public void insert(Order order, int quantity) {
		repositoryOrders.insert(order, quantity);
	}
	
	public void undoInsert() {
		repositoryOrders.delete();
	}
	
	public String getTable() {
		return table;
	}
	
	// getter nearby callback
	public PayloadCallback getCallback() {
		return repositoryOrders.getPayloadCallback(table);
	}
	
	// getter item touch helper callback
	public ItemTouchHelper.SimpleCallback getRecyclerCallback(
			Context context, OrdersAdapter adapter, ListOrdersPage.ListOrdersType listOrdersType) {
		return repositoryOrders.getRecyclerCallback(context, adapter, listOrdersType, table);
	}
	
	public float getMenuPrice() {
		return repositoryTables.getMenuPrice(table);
	}
	
	public float getExtrasPrice() {
		return repositoryTables.getExtrasPrice(table);
	}
	
	// it calls the various methods required for a checkout
	public void checkout(ViewModelStoreOwner viewModelStoreOwner) {
		repositoryOrders.checkout(table);
		repositoryTables.checkoutTable(table);
		ViewModelUtil.clearViewModels(viewModelStoreOwner);
	}
}
