package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

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
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.view.ListaOrdiniGenericaPage;
import com.veneto_valley.veneto_valley.view.OrdiniAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RepositoryOrdini {
	private final OrdineDao ordineDao;
	private final Application application;
	private final SharedPreferences preferences;
	private LiveData<List<Ordine>> pendingOrders = null,
			confirmedOrders = null,
			deliveredOrders = null,
			allSynchronized = null;
	private long[] lastInserted = new long[99];
	
	public RepositoryOrdini(Application application) {
		this.application = application;
		ordineDao = AppDatabase.getInstance(application).ordineDao();
		preferences = PreferenceManager.getDefaultSharedPreferences(application);
	}
	
	public void insert(Ordine ordine, int qta) {
		Executors.newSingleThreadExecutor().execute(() -> {
			Ordine[] ordini = new Ordine[qta];
			Arrays.fill(ordini, ordine);
			lastInserted = ordineDao.insertAll(ordini);
		});
	}
	
	public void update(Ordine ordine) {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.update(ordine));
	}
	
	public void delete(Ordine ordine) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> ordineDao.delete(ordine));
	}
	
	public void delete() {
		Executors.newSingleThreadExecutor().execute(() -> ordineDao.deleteAllById(lastInserted));
	}
	
	// lazy initialization dei livedata
	public LiveData<List<Ordine>> getAllSynchronized(String tavolo) {
		if (allSynchronized == null) {
			allSynchronized = ordineDao.getAllSynchronized(tavolo, Ordine.StatusOrdine.confirmed);
		}
		return allSynchronized;
	}
	
	public LiveData<List<Ordine>> getPendingOrders(String tavolo) {
		if (pendingOrders == null)
			pendingOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.pending, tavolo);
		return pendingOrders;
	}
	
	public LiveData<List<Ordine>> getConfirmedOrders(String tavolo) {
		if (confirmedOrders == null)
			confirmedOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.confirmed, tavolo);
		return confirmedOrders;
	}
	
	public LiveData<List<Ordine>> getDeliveredOrders(String tavolo) {
		if (deliveredOrders == null)
			deliveredOrders = ordineDao.getAllbyStatus(Ordine.StatusOrdine.delivered, tavolo);
		return deliveredOrders;
	}
	
	// conferma un ordine pending come da ordinare e lo invia al master se slave
	public void confermaOrdine(Ordine ordine, String tavolo) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			ordine.status = Ordine.StatusOrdine.confirmed;
			update(ordine);
			
			if (!preferences.contains("is_master"))
				Connessione.getInstance(true, application, tavolo, getPayloadCallback(tavolo))
						.invia(ordine.getBytes());
		});
		
	}
	
	// annulla la conferma dell'ordine e notifica il master se slave
	public void annullaConfermaOrdine(Ordine ordine, String tavolo) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			ordine.status = Ordine.StatusOrdine.pending;
			update(ordine);
			
			if (!preferences.contains("is_master"))
				Connessione.getInstance(true, application, tavolo, getPayloadCallback(tavolo))
						.invia(ordine.getBytes());
		});
	}
	
	// segna un ordine come arrivato e notifica il master se slave
	public void notificaArrivato(Ordine ordine, String tavolo) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			ordine.status = Ordine.StatusOrdine.delivered;
			update(ordine);
			if (!preferences.contains("is_master"))
				Connessione.getInstance(false, application, tavolo, getPayloadCallback(tavolo))
						.invia(ordine.getBytes());
		});
	}
	
	// segna un ordine come non arrivato e notifica il master se slave
	public void annullaArrivato(Ordine ordine, String tavolo) {
		Executors.newSingleThreadScheduledExecutor().execute(() -> {
			ordine.status = Ordine.StatusOrdine.confirmed;
			update(ordine);
			if (!preferences.contains("is_master"))
				Connessione.getInstance(false, application, tavolo, getPayloadCallback(tavolo))
						.invia(ordine.getBytes());
		});
	}
	
	// metodo che usa il master per rimuovere i dati degli slave dal database durante il checkout
	public void cleanDatabase() {
		Executors.newSingleThreadExecutor().execute(ordineDao::deleteSlaves);
	}
	
	// svuota le shared preference, rimuove gli ordini degli slave se master e chiude la connessione
	public void checkout(String tavolo) {
		if (preferences.contains("is_master"))
			cleanDatabase();
		preferences.edit().clear().apply();
		Connessione.getInstance(preferences.getBoolean("is_master", false),
				application, tavolo, getPayloadCallback(tavolo)).closeConnection();
	}
	
	// costruisce la callback nearby
	public PayloadCallback getPayloadCallback(String tavolo) {
		return new PayloadCallback() {
			@Override
			public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
				final byte[] receivedBytes = payload.asBytes();
				Executors.newSingleThreadExecutor().execute(() -> {
					Ordine fromSlave = Ordine.getFromBytes(receivedBytes);
					Ordine ordine;
					// se l'ordine ricevuto dallo slave corrisponde ad un ordine già presente nel
					// db del master, riconosce l'input dello slave tramite lo status
					if ((ordine = ordineDao.contains(fromSlave.status, fromSlave.tavolo, fromSlave.piatto, fromSlave.utente)) != null) {
						if (fromSlave.status == Ordine.StatusOrdine.pending)
							ordineDao.delete(ordine);
						else if (fromSlave.status == Ordine.StatusOrdine.confirmed) {
							ordineDao.insert(ordine);
						} else
							ordineDao.update(ordine);
					} else {
						// altrimenti crea un nuovo ordine con i dati ricevuti così da rispettare
						// i vincoli di unicità del database e lo aggiunge
						ordine = new Ordine(tavolo, fromSlave.piatto, fromSlave.status,
								fromSlave.utente, true);
						ordineDao.insert(ordine);
					}
				});
			}
			
			@Override
			public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
			}
		};
	}
	
	// ritorna la callback itemtouch helper
	public ItemTouchHelper.SimpleCallback getRecyclerCallback(Context context, OrdiniAdapter adapter, ListaOrdiniGenericaPage.TipoLista tipoLista, String tavolo) {
		if (tipoLista == ListaOrdiniGenericaPage.TipoLista.pending) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> confermaOrdine(adapter.getOrdineAt(integer), tavolo),
					integer -> delete(adapter.getOrdineAt(integer)),
					ContextCompat.getColor(context, R.color.colorPrimary),
					ContextCompat.getColor(context, R.color.red),
					R.drawable.ic_send, R.drawable.ic_delete);
		} else if (tipoLista == ListaOrdiniGenericaPage.TipoLista.confirmed) {
			return makeCallback(context,
					ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
					integer -> notificaArrivato(adapter.getOrdineAt(integer), tavolo),
					integer -> annullaConfermaOrdine(adapter.getOrdineAt(integer), tavolo),
					ContextCompat.getColor(context, R.color.colorPrimary),
					ContextCompat.getColor(context, R.color.colorPrimary),
					R.drawable.ic_send, R.drawable.ic_send_rev);
		} else
			return makeCallback(context,
					ItemTouchHelper.LEFT, null,
					integer -> annullaArrivato(adapter.getOrdineAt(integer), tavolo),
					0, ContextCompat.getColor(context, R.color.colorPrimary),
					0, R.drawable.ic_send_rev);
	}
	
	// costruisce la callback itemtouch helper
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
