package com.veneto_valley.veneto_valley.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.AllOrdersViewModel;
import com.veneto_valley.veneto_valley.viewmodel.StoricoViewModel;

import java.util.List;

public class ListaOrdiniGenericaPage extends Fragment {
	private OrdiniAdapter ordiniAdapter;
	private ItemTouchHelper.SimpleCallback callback;
	private LiveData<List<Ordine>> liveDataOrdini;
	
	public ListaOrdiniGenericaPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	public ListaOrdiniGenericaPage(OrdiniAdapter ordiniAdapter, LiveData<List<Ordine>> liveDataOrdini, ItemTouchHelper.SimpleCallback callback) {
		super(R.layout.fragment_recyclerview);
		this.ordiniAdapter = ordiniAdapter;
		this.callback = callback;
		this.liveDataOrdini = liveDataOrdini;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		
		if (getArguments() != null) {
			ListaOrdiniGenericaPageArgs args = ListaOrdiniGenericaPageArgs.fromBundle(getArguments());
			if (args.getTipoLista() == TipoLista.allOrders) {
				AllOrdersViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), AllOrdersViewModel.class);
				ordiniAdapter = new OrdiniAdapter();
				recyclerView.setAdapter(ordiniAdapter);
				liveDataOrdini = viewModel.getOrdini();
				liveDataOrdini.observe(getViewLifecycleOwner(), ordiniAdapter::submitList);
			} else {
				StoricoViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), StoricoViewModel.class);
				StoricoAdapter storicoAdapter = new StoricoAdapter();
				recyclerView.setAdapter(storicoAdapter);
				LiveData<List<Tavolo>> liveDataTavoli = viewModel.getTavoli();
				liveDataTavoli.observe(getViewLifecycleOwner(), storicoAdapter::submitList);
			}
		} else {
			recyclerView.setAdapter(ordiniAdapter);
			ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
			itemTouchHelper.attachToRecyclerView(recyclerView);
			liveDataOrdini.observe(getViewLifecycleOwner(), ordiniAdapter::submitList);
		}
	}
	
	public enum TipoLista {
		allOrders,
		storico
	}
}