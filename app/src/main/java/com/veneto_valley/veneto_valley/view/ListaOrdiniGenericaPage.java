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
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdiniViewModel;
import com.veneto_valley.veneto_valley.viewmodel.StoricoViewModel;

import java.util.List;

public class ListaOrdiniGenericaPage extends Fragment {
	private TipoLista tipoLista;
	
	public ListaOrdiniGenericaPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	public ListaOrdiniGenericaPage(TipoLista tipoLista) {
		super(R.layout.fragment_recyclerview);
		this.tipoLista = tipoLista;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		
		// utilizzo il safearg/parametro tipolista per costruire la view di riferimento estraendo il viewmodel
		// corretto e invocando il relativo metodo che ritorna un livedata, che viene poi osservato
		// dalla view
		if (getArguments() != null) {
			ListaOrdiniGenericaPageArgs args = ListaOrdiniGenericaPageArgs.fromBundle(getArguments());
			if (args.getTipoLista() == TipoLista.allOrders) {
				OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class);
				OrdiniAdapter ordiniAdapter = new OrdiniAdapter(true);
				recyclerView.setAdapter(ordiniAdapter);
				viewModel.getAllSynchronized().observe(getViewLifecycleOwner(), ordiniAdapter::submitList);
			} else if (args.getTipoLista() == TipoLista.storico) {
				StoricoViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), StoricoViewModel.class);
				StoricoAdapter storicoAdapter = new StoricoAdapter();
				recyclerView.setAdapter(storicoAdapter);
				viewModel.getTavoli().observe(getViewLifecycleOwner(), storicoAdapter::submitList);
			}
		} else {
			OrdiniAdapter ordiniAdapter = new OrdiniAdapter(false);
			LiveData<List<Ordine>> ordini;
			OrdiniViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class);
			if (tipoLista == TipoLista.pending) {
				ordini = viewModel.getPendingOrders();
			} else if (tipoLista == TipoLista.confirmed) {
				ordini = viewModel.getConfirmed();
			} else {
				ordini = viewModel.getDelivered();
			}
			recyclerView.setAdapter(ordiniAdapter);
			// questa view necessita di user input quindi richiede al viewmodel la creazione della
			// callback necessaria
			ItemTouchHelper.SimpleCallback callback = viewModel.getRecyclerCallback(requireContext(), ordiniAdapter, tipoLista);
			ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
			itemTouchHelper.attachToRecyclerView(recyclerView);
			ordini.observe(getViewLifecycleOwner(), ordiniAdapter::submitList);
		}
	}
	
	public enum TipoLista {
		allOrders,
		storico,
		pending,
		confirmed,
		delivered
	}
}