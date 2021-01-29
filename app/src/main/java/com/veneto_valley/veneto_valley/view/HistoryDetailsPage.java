package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.model.entities.Table;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.HistoryViewModel;

import java.util.List;

public class HistoryDetailsPage extends Fragment {
	private HistoryViewModel viewModel;
	private Table table;
	
	public HistoryDetailsPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		OrdersAdapter adapter = new OrdersAdapter(ListOrdersPage.ListOrdersType.history);
		recyclerView.setAdapter(adapter);
		
		table = HistoryDetailsPageArgs.fromBundle(requireArguments()).getTable();
		viewModel = ViewModelUtil.getViewModel(requireActivity(), HistoryViewModel.class);
		LiveData<List<Order>> orders = viewModel.getOrders(table);
		orders.observe(getViewLifecycleOwner(), adapter::submitList);
	}
	
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.storico_overflow, menu);
		menu.findItem(R.id.deleteAllTables).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.deleteTable) {
			ConfirmDialog dialog = new ConfirmDialog(() -> {
				viewModel.deleteTable(table);
				NavHostFragment.findNavController(HistoryDetailsPage.this).navigateUp();
			});
			dialog.show(getParentFragmentManager(), null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class ConfirmDialog extends DialogFragment {
		private final Runnable runnable;
		
		public ConfirmDialog(Runnable runnable) {
			this.runnable = runnable;
		}
		
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
			builder.setTitle("Confirm table delete?");
			builder.setPositiveButton("OK", (dialog, which) -> runnable.run());
			builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());
			return builder.create();
		}
	}
}