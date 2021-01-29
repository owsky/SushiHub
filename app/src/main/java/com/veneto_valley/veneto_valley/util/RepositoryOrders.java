package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrderDao;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.view.ListOrdersPage;
import com.veneto_valley.veneto_valley.view.OrdersAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RepositoryOrders {
	private final OrderDao orderDao;
	private final Application application;
	private final SharedPreferences preferences;
	private LiveData<List<Order>> pendingOrders = null,
			confirmedOrders = null,
			deliveredOrders = null,
			allSynchronized = null;
	private long[] lastInserted = new long[99];
	
	public RepositoryOrders(Application application) {
		this.application = application;
		orderDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public void insert(Order order, int qta) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Order[] orders = new Order[qta];
			Arrays.fill(orders, order);
			lastInserted = orderDao.insertAll(orders);
		});
	}
	
	public void update(Order order) {
		Executors.newSingleThreadExecutor().execute(() -> orderDao.update(order));
	}
	
	public void delete(Order order) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> orderDao.delete(order));
	}
	
	public void delete() {
		Executors.newSingleThreadExecutor().execute(() -> orderDao.deleteAllById(lastInserted));
	}
	
	public LiveData<List<Order>> getAllSynchronized(String table) {
		if (allSynchronized == null) {
			allSynchronized = orderDao.getAllSynchronized(table, Order.OrderStatus.confirmed);
		}
		return allSynchronized;
	}
	
	public LiveData<List<Order>> getPendingOrders(String table) {
		if (pendingOrders == null)
			pendingOrders = orderDao.getAllbyStatus(Order.OrderStatus.pending, table);
		return pendingOrders;
	}
	
	public LiveData<List<Order>> getConfirmedOrders(String table) {
		if (confirmedOrders == null)
			confirmedOrders = orderDao.getAllbyStatus(Order.OrderStatus.confirmed, table);
		return confirmedOrders;
	}
	
	public LiveData<List<Order>> getDeliveredOrders(String table) {
		if (deliveredOrders == null)
			deliveredOrders = orderDao.getAllbyStatus(Order.OrderStatus.delivered, table);
		return deliveredOrders;
	}
	
	// marks an order as confirmed, sends it to the master if called by a slave
	public void confirmOrder(Order order, String table) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			order.status = Order.OrderStatus.confirmed;
			update(order);
			
			if (!preferences.contains("is_master")) {
				Order orderInvia = new Order(order.tableCode, order.dish, Order.OrderStatus.insertOrder, order.user, order.receivedFromSlave);
				Nearby.getInstance(true, application, table, getPayloadCallback(table))
						.send(orderInvia.getBytes());
			}
		});
		
	}
	
	public void undoConfirmOrder(Order order, String table) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			order.status = Order.OrderStatus.pending;
			update(order);
			
			if (!preferences.contains("is_master")) {
				Order orderInvia = new Order(order.tableCode, order.dish, Order.OrderStatus.deleteOrder, order.user, order.receivedFromSlave);
				Nearby.getInstance(true, application, table, getPayloadCallback(table))
						.send(orderInvia.getBytes());
			}
		});
	}
	
	// marks and order as delivered, sends it to the master if called by a slave
	public void markAsDelivered(Order order, String table) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			order.status = Order.OrderStatus.delivered;
			update(order);
			if (!preferences.contains("is_master")) {
				Order orderInvia = new Order(order.tableCode, order.dish, Order.OrderStatus.deliverOrder, order.user, order.receivedFromSlave);
				Nearby.getInstance(false, application, table, getPayloadCallback(table))
						.send(orderInvia.getBytes());
			}
		});
	}
	
	public void undoMarkAsDelivered(Order order, String table) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			order.status = Order.OrderStatus.confirmed;
			update(order);
			if (!preferences.contains("is_master")) {
				Order orderInvia = new Order(order.tableCode, order.dish, Order.OrderStatus.undeliverOrder, order.user, order.receivedFromSlave);
				Nearby.getInstance(false, application, table, getPayloadCallback(table))
						.send(orderInvia.getBytes());
			}
		});
	}
	
	// removes all synchronized orders from the master's local database
	public void cleanDatabase() {
		Executors.newSingleThreadExecutor().execute(orderDao::deleteSlaves);
	}
	
	public void checkout(String table) {
		if (preferences.getBoolean("is_master", false))
			cleanDatabase();
		preferences.edit().clear().apply();
		Nearby.getInstance(!preferences.getBoolean("is_master", false),
				application, table, getPayloadCallback(table)).closeConnection();
	}
	
	public PayloadCallback getPayloadCallback(String table) {
		return new PayloadCallback() {
			@Override
			public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
				final byte[] receivedBytes = payload.asBytes();
				Executors.newSingleThreadExecutor().execute(() -> {
					Order fromSlave = Order.getFromBytes(receivedBytes);
					Order order;
					
					// it handles the synchronization by recognizing the slave's action through the status
					switch (fromSlave.status){
						case insertOrder:
							order = new Order(table, fromSlave.dish, Order.OrderStatus.confirmed,
							fromSlave.user, true);
							orderDao.insert(order);
							break;
						case deliverOrder:
							order = orderDao.contains(Order.OrderStatus.confirmed, fromSlave.tableCode, fromSlave.dish, fromSlave.user);
							order.status = Order.OrderStatus.delivered;
							orderDao.update(order);
							break;
						case undeliverOrder:
							order = orderDao.contains(Order.OrderStatus.delivered, fromSlave.tableCode, fromSlave.dish, fromSlave.user);
							order.status = Order.OrderStatus.confirmed;
							orderDao.update(order);
							break;
						case deleteOrder:
							order = orderDao.contains(Order.OrderStatus.confirmed, fromSlave.tableCode, fromSlave.dish, fromSlave.user);
							orderDao.delete(order);
							break;
						default:
							Log.w("NearbyOrder","Invalid status");
					}
				});
			}
			
			@Override
			public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
			}
		};
	}
	
	public ItemTouchHelper.SimpleCallback getRecyclerCallback(Context context, OrdersAdapter adapter, ListOrdersPage.ListOrdersType listOrdersType, String table) {
		if (listOrdersType == ListOrdersPage.ListOrdersType.pending) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> confirmOrder(adapter.getOrderAt(integer), table),
					integer -> delete(adapter.getOrderAt(integer)),
					ContextCompat.getColor(context, R.color.colorPrimary),
					ContextCompat.getColor(context, R.color.red),
					R.drawable.ic_send, R.drawable.ic_delete);
		} else if (listOrdersType == ListOrdersPage.ListOrdersType.confirmed) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> markAsDelivered(adapter.getOrderAt(integer), table),
					integer -> undoConfirmOrder(adapter.getOrderAt(integer), table),
					ContextCompat.getColor(context, R.color.colorPrimary),
					ContextCompat.getColor(context, R.color.colorPrimary),
					R.drawable.ic_send, R.drawable.ic_send_rev);
		} else
			return makeCallback(context,
					ItemTouchHelper.LEFT, null,
					integer -> undoMarkAsDelivered(adapter.getOrderAt(integer), table),
					0, ContextCompat.getColor(context, R.color.colorPrimary),
					0, R.drawable.ic_send_rev);
	}
	
	private ItemTouchHelper.SimpleCallback makeCallback(Context context, int dragDir2, Consumer<Integer> consumerRight, Consumer<Integer> consumerLeft, int colorRight, int colorLeft, int drawableRight, int drawableLeft) {
		return new ItemTouchHelper.SimpleCallback(0, dragDir2) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}
			
			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT)
					consumerRight.accept(viewHolder.getAdapterPosition());
				else if (direction == ItemTouchHelper.LEFT)
					consumerLeft.accept(viewHolder.getAdapterPosition());
			}
			
			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if (dX < 0) {
					new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(colorLeft)
							.addSwipeLeftActionIcon(drawableLeft)
							.create()
							.decorate();
				} else if (dX > 0) {
					new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(colorRight)
							.addSwipeRightActionIcon(drawableRight)
							.create()
							.decorate();
				}
				
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		};
	}
}
