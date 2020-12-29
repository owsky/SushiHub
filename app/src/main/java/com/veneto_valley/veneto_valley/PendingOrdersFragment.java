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

import com.veneto_valley.veneto_valley.adapters.PendingAdapter;
import com.veneto_valley.veneto_valley.viewmodels.MyViewModelFactory;
import com.veneto_valley.veneto_valley.viewmodels.PendingViewModel;

import java.io.IOException;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PendingOrdersFragment extends Fragment {
	private PendingViewModel pendingViewModel;
	private ItemTouchHelper itemTouchHelper;
	
	public PendingOrdersFragment() {
		super(R.layout.fragment_pending_orders);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPending);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		PendingAdapter adapter = new PendingAdapter();
		recyclerView.setAdapter(adapter);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		String codiceTavolo = preferences.getString("codice_tavolo", null);
		if (codiceTavolo != null) {
			MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication(), codiceTavolo);
			pendingViewModel = new ViewModelProvider(requireActivity(), factory).get(PendingViewModel.class);
		} else {
			pendingViewModel = new ViewModelProvider(requireActivity()).get(PendingViewModel.class);
		}
		pendingViewModel.getOrdini().observe(getViewLifecycleOwner(), adapter::submitList);
		
		
		ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				// TODO: investigare se si può supportare facilmente il reorder
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT) {
					try {
						adapter.inviaAlMaster(viewHolder.getAdapterPosition());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (direction == ItemTouchHelper.LEFT)
					pendingViewModel.delete(adapter.getOrdineAt(viewHolder.getAdapterPosition()));
			}

			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if (dX < 0) {
					new RecyclerViewSwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
							.addSwipeLeftActionIcon(R.drawable.ic_delete)
							.create()
							.decorate();
				} else if (dX > 0) {
					new RecyclerViewSwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
							.addSwipeRightActionIcon(R.drawable.ic_send)
							.create()
							.decorate();
				}

				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		};
		itemTouchHelper = new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}

//	@Override
//	public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
//		itemTouchHelper.startDrag(viewHolder);
//	}
}