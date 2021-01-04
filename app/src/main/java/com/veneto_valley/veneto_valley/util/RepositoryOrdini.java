package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.view.ListaOrdiniGenericaPage;
import com.veneto_valley.veneto_valley.view.OrdiniAdapter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RepositoryOrdini {
	private final OrdineDao ordineDao;
	private final String tavolo;
	private final Application application;
	private final SharedPreferences preferences;
	private LiveData<List<Ordine>> pendingOrders = null;
	private LiveData<List<Ordine>> confirmedOrders = null;
	private LiveData<List<Ordine>> deliveredOrders = null;
	private LiveData<List<Ordine>> allSynchronized = null;
	
	public RepositoryOrdini(Application application, String tavolo) {
		this.application = application;
		this.tavolo = tavolo;
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public void insert(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Ordine vecchioOrdine;
			if ((vecchioOrdine = ordineDao.contains(Ordine.StatusOrdine.pending, tavolo, ordine.piatto)) != null) {
				vecchioOrdine.quantita += ordine.quantita;
				if (!(ordine.desc == null))
					vecchioOrdine.desc = ordine.desc;
				vecchioOrdine.prezzo = ordine.prezzo;
				update(vecchioOrdine);
			} else {
				ordineDao.insert(ordine);
			}
		});
	}
	
	public void update(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.update(ordine));
	}
	
	public void delete(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.delete(ordine));
	}
	
	public LiveData<List<Ordine>> getAllSynchronized() {
		if (allSynchronized == null) {
			allSynchronized = ordineDao.getAllSynchronized(tavolo);
		}
		return allSynchronized;
	}
	
	public LiveData<List<Ordine>> getPendingOrders() {
		if (pendingOrders == null)
			pendingOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.pending, tavolo);
		return pendingOrders;
	}
	
	public LiveData<List<Ordine>> getConfirmedOrders() {
		if (confirmedOrders == null)
			confirmedOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.confirmed, tavolo);
		return confirmedOrders;
	}
	
	public LiveData<List<Ordine>> getDeliveredOrders() {
		if (deliveredOrders == null)
			deliveredOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.delivered, tavolo);
		return deliveredOrders;
	}
	
	public void sendToMaster(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.confirmed;
		update(ordine);
		
		if (preferences.contains("is_master")) {
			Connessione.getInstance(true, application, tavolo, getPayloadCallback())
					.invia(ordine.getBytes());
		}
	}
	
	public void retrieveFromMaster(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.pending;
		update(ordine);
		if (!preferences.contains("is_master")) {
			Connessione.getInstance(true, application, tavolo, getPayloadCallback())
					.invia(ordine.getBytes());
		}
	}
	
	public void markAsDelivered(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.delivered;
		update(ordine);
		Connessione.getInstance(false, application, tavolo, getPayloadCallback())
				.invia(ordine.getBytes());
	}
	
	public void markAsNotDelivered(Ordine ordine) {
		ordine.status = Ordine.StatusOrdine.confirmed;
		update(ordine);
		Connessione.getInstance(false, application, tavolo, getPayloadCallback())
				.invia(ordine.getBytes());
	}
	
	public void cleanDatabase() {
		Executors.newSingleThreadExecutor().execute(ordineDao::deleteSlaves);
	}
	
	public void checkout() {
		cleanDatabase();
		preferences.edit().clear().apply();
		new RepositoryTavoli(application).checkoutTavolo(tavolo);
		Connessione.getInstance(preferences.getBoolean("is_master", false),
				application, tavolo, getPayloadCallback()).closeConnection();
	}
	
	public PayloadCallback getPayloadCallback() {
		return new PayloadCallback() {
			@Override
			public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
				final byte[] receivedBytes = payload.asBytes();
				Executors.newSingleThreadExecutor().execute(() -> {
					Ordine ordine = Ordine.getFromBytes(receivedBytes);
					
					if (ordine.status.equals(Ordine.StatusOrdine.confirmed)) {
						ordineDao.insert(ordine);
					} else if (ordine.status.equals(Ordine.StatusOrdine.pending)) {
						ordineDao.delete(ordine);
					} else {
						ordineDao.update(ordine);
					}
				});
			}
			
			@Override
			public void onPayloadTransferUpdate(@NonNull String s,
			                                    @NonNull PayloadTransferUpdate payloadTransferUpdate) {
//			if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
//				// Do something with is here...
//			}
			}
		};
	}
	
	public ItemTouchHelper.SimpleCallback getRecyclerCallback(Context context, OrdiniAdapter adapter, ListaOrdiniGenericaPage.TipoLista tipoLista) {
		if (tipoLista == ListaOrdiniGenericaPage.TipoLista.pending) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> sendToMaster(adapter.getOrdineAt(integer)),
					integer -> delete(adapter.getOrdineAt(integer)),
					ContextCompat.getColor(context, R.color.green),
					ContextCompat.getColor(context, R.color.colorPrimary),
					R.drawable.ic_send, R.drawable.ic_delete);
		} else if (tipoLista == ListaOrdiniGenericaPage.TipoLista.confirmed) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> markAsDelivered(adapter.getOrdineAt(integer)),
					integer -> retrieveFromMaster(adapter.getOrdineAt(integer)),
					ContextCompat.getColor(context, R.color.design_default_color_primary),
					ContextCompat.getColor(context, R.color.design_default_color_primary),
					R.drawable.ic_send, R.drawable.ic_send);
		} else
			return makeCallback(context,
					ItemTouchHelper.LEFT, null,
					integer -> markAsNotDelivered(adapter.getOrdineAt(integer)),
					0, ContextCompat.getColor(context, R.color.green),
					0, R.drawable.ic_send);
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
