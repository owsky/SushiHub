package com.veneto_valley.veneto_valley;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.viewmodels.ConfirmedViewModel;
import com.veneto_valley.veneto_valley.viewmodels.MyViewModelFactory;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ConfirmedOrdersFragment extends Fragment {
	private ConfirmedViewModel viewModel;
	
	public ConfirmedOrdersFragment() {
		super(R.layout.fragment_confirmed_orders);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewConfirmed);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
		OrdiniAdapter adapter = new OrdiniAdapter();
		recyclerView.setAdapter(adapter);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		if (codiceTavolo != null) {
			MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication(), codiceTavolo);
			viewModel = new ViewModelProvider(requireActivity(), factory).get(ConfirmedViewModel.class);
		} else {
			viewModel = new ViewModelProvider(requireActivity()).get(ConfirmedViewModel.class);
		}
		viewModel.getOrdini().observe(getViewLifecycleOwner(), adapter::submitList);

		ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				Ordine ordine = adapter.getOrdineAt(viewHolder.getAdapterPosition());
				if (direction == ItemTouchHelper.LEFT)
					viewModel.retrieveFromMaster(ordine, requireActivity());
				else if (direction == ItemTouchHelper.RIGHT)
					viewModel.markAsDelivered(ordine, requireActivity());
			}

			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if (dX < 0) {
					new RecyclerViewSwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_primary))
							.addSwipeLeftActionIcon(R.drawable.ic_send)
							.create()
							.decorate();
				} else if (dX > 0) {
					new RecyclerViewSwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_primary))
							.addSwipeRightActionIcon(R.drawable.ic_send)
							.create()
							.decorate();
				}
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
}