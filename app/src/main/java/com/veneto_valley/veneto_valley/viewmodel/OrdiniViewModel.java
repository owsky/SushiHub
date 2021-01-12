package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.nearby.connection.PayloadCallback;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.util.RepositoryOrdini;
import com.veneto_valley.veneto_valley.util.RepositoryTavoli;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.view.ListaOrdiniGenericaPage;
import com.veneto_valley.veneto_valley.view.OrdiniAdapter;

import java.util.List;

public class OrdiniViewModel extends AndroidViewModel {
	private final RepositoryOrdini repositoryOrdini;
	private final RepositoryTavoli repositoryTavoli;
	private LiveData<List<Ordine>> pending = null, confirmed = null, delivered = null, allOrders = null;
	private final String tavolo;
	
	public OrdiniViewModel(@NonNull Application application, String tavolo) {
		super(application);
		repositoryOrdini = new RepositoryOrdini(application);
		repositoryTavoli = new RepositoryTavoli(application);
		this.tavolo = tavolo;
	}
	
	public LiveData<List<Ordine>> getOrdini(ListaOrdiniGenericaPage.TipoLista tipoLista) {
		if (tipoLista == ListaOrdiniGenericaPage.TipoLista.pending)
			return getPendingOrders();
		else if (tipoLista == ListaOrdiniGenericaPage.TipoLista.confirmed)
			return getConfirmed();
		else if (tipoLista == ListaOrdiniGenericaPage.TipoLista.delivered)
			return getDelivered();
		else
			return getAllSynchronized();
	}
	
	// lazy initialization dei livedata
	public LiveData<List<Ordine>> getPendingOrders() {
		if (pending == null)
			pending = repositoryOrdini.getPendingOrders(tavolo);
		return pending;
	}
	
	public LiveData<List<Ordine>> getConfirmed() {
		if (confirmed == null)
			confirmed = repositoryOrdini.getConfirmedOrders(tavolo);
		return confirmed;
	}
	
	public LiveData<List<Ordine>> getDelivered() {
		if (delivered == null)
			delivered = repositoryOrdini.getDeliveredOrders(tavolo);
		return delivered;
	}
	
	public LiveData<List<Ordine>> getAllSynchronized() {
		if (allOrders == null)
			allOrders = repositoryOrdini.getAllSynchronized(tavolo);
		return allOrders;
	}
	
	public void insert(Ordine ordine) {
		repositoryOrdini.insert(ordine);
	}
	
	public void delete(Ordine ordine) {
		repositoryOrdini.delete(ordine);
	}
	
	public String getTavolo() {
		return tavolo;
	}
	
	// getter callback nearby
	public PayloadCallback getCallback() {
		return repositoryOrdini.getPayloadCallback(tavolo);
	}
	
	// getter callback touch helper
	public ItemTouchHelper.SimpleCallback getRecyclerCallback(Context context, OrdiniAdapter adapter, ListaOrdiniGenericaPage.TipoLista tipoLista) {
		return repositoryOrdini.getRecyclerCallback(context, adapter, tipoLista, tavolo);
	}
	
	public float getCostoMenu() {
		return repositoryTavoli.getCostoMenu(tavolo);
	}
	
	public float getCostoExtra() {
		return repositoryTavoli.getCostoExtra(tavolo);
	}
	
	// propago la richiesta di checkout alla repository
	public void checkout(ViewModelStoreOwner viewModelStoreOwner) {
		repositoryOrdini.checkout(tavolo);
		repositoryTavoli.checkoutTavolo(tavolo);
		ViewModelUtil.clearViewModels(viewModelStoreOwner);
	}
}
