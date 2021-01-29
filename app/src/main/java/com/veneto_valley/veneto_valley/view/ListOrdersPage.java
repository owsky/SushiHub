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
import com.veneto_valley.veneto_valley.viewmodel.OrdersViewModel;
import com.veneto_valley.veneto_valley.viewmodel.HistoryViewModel;

public class ListOrdersPage extends Fragment {
	private ListOrdersType listOrdersType;
	private HistoryViewModel historyViewModel;
	
	public ListOrdersPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	public ListOrdersPage(ListOrdersType listOrdersType) {
		super(R.layout.fragment_recyclerview);
		this.listOrdersType = listOrdersType;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		
		// it builds a dynamic recyclerview page using listOrdersType
		if (getArguments() != null)
			listOrdersType = ListOrdersPageArgs.fromBundle(getArguments()).getListOrdersType();
		
		if (listOrdersType == ListOrdersType.history) {
			setHasOptionsMenu(true);
			historyViewModel = ViewModelUtil.getViewModel(requireActivity(), HistoryViewModel.class);
			HistoryAdapter historyAdapter = new HistoryAdapter();
			recyclerView.setAdapter(historyAdapter);
			historyViewModel.getTables().observe(getViewLifecycleOwner(), historyAdapter::submitList);
		} else {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
			OrdersViewModel ordersViewModel = ViewModelUtil.getViewModel(requireActivity(), OrdersViewModel.class, preferences.getString("codice_tavolo", null));
			OrdersAdapter ordersAdapter = new OrdersAdapter(listOrdersType);
			recyclerView.setAdapter(ordersAdapter);
			ordersViewModel.getOrders(listOrdersType).observe(getViewLifecycleOwner(), ordersAdapter::submitList);
			if (listOrdersType == ListOrdersType.pending || listOrdersType == ListOrdersType.delivered || listOrdersType == ListOrdersType.confirmed) {
				new ItemTouchHelper(ordersViewModel.getRecyclerCallback(requireContext(), ordersAdapter, listOrdersType)).attachToRecyclerView(recyclerView);
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (getArguments() != null) {
			ListOrdersPageArgs args = ListOrdersPageArgs.fromBundle(getArguments());
			if (args.getListOrdersType() == ListOrdersType.history)
				inflater.inflate(R.menu.storico_overflow, menu);
			menu.findItem(R.id.deleteTable).setVisible(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.deleteAllTables) {
			historyViewModel.deleteAllTables();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public enum ListOrdersType {
		allOrders,
		history,
		pending,
		confirmed,
		delivered
	}
}