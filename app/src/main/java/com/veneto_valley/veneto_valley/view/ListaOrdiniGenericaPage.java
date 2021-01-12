package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.OrdiniViewModel;
import com.veneto_valley.veneto_valley.viewmodel.StoricoViewModel;

public class ListaOrdiniGenericaPage extends Fragment {
	private TipoLista tipoLista;
	private StoricoViewModel storicoViewModel;
	
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
		// corretto e invocando il relativo metodo che ritorna un livedata, che viene poi osservato dalla view
		if (getArguments() != null)
			tipoLista = ListaOrdiniGenericaPageArgs.fromBundle(getArguments()).getTipoLista();
		
		if (tipoLista == TipoLista.storico) {
			setHasOptionsMenu(true);
			storicoViewModel = ViewModelUtil.getViewModel(requireActivity(), StoricoViewModel.class);
			StoricoAdapter storicoAdapter = new StoricoAdapter();
			recyclerView.setAdapter(storicoAdapter);
			storicoViewModel.getTavoli().observe(getViewLifecycleOwner(), storicoAdapter::submitList);
		} else {
			OrdiniViewModel ordiniViewModel = ViewModelUtil.getViewModel(requireActivity(), OrdiniViewModel.class);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
			OrdiniAdapter ordiniAdapter = new OrdiniAdapter(tipoLista, preferences.contains("codice_ristorante"));
			recyclerView.setAdapter(ordiniAdapter);
			ordiniViewModel.getOrdini(tipoLista).observe(getViewLifecycleOwner(), ordiniAdapter::submitList);
			if (tipoLista == TipoLista.pending || tipoLista == TipoLista.delivered || tipoLista == TipoLista.confirmed) {
				new ItemTouchHelper(ordiniViewModel.getRecyclerCallback(requireContext(), ordiniAdapter, tipoLista)).attachToRecyclerView(recyclerView);
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (getArguments() != null) {
			ListaOrdiniGenericaPageArgs args = ListaOrdiniGenericaPageArgs.fromBundle(getArguments());
			if (args.getTipoLista() == TipoLista.storico)
				inflater.inflate(R.menu.storico_overflow, menu);
			menu.findItem(R.id.eliminaTavolo).setVisible(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.eliminaTuttiTavoli) {
			storicoViewModel.deleteAllTables();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public enum TipoLista {
		allOrders,
		storico,
		pending,
		confirmed,
		delivered
	}
}