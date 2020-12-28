package com.veneto_valley.veneto_valley;

import android.graphics.Canvas;
import android.os.Bundle;
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
import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PendingOrdersFragment extends Fragment implements PendingAdapter.CustomDragListener {
	private List<Ordine> dataList = new ArrayList<>();
	private PendingAdapter adapter;
	private ItemTouchHelper itemTouchHelper;
	
	public PendingOrdersFragment() {
		super(R.layout.fragment_pending_orders);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppDatabase database = AppDatabase.getInstance(requireContext());
		dataList = database.ordineDao().getAllbyStatus("pending");
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPending);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		adapter = new PendingAdapter(requireActivity(), dataList, this);
		recyclerView.setAdapter(adapter);

		ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean isLongPressDragEnabled() {
				return true;
			}

			@Override
			public boolean isItemViewSwipeEnabled() {
				return true;
			}
			
			@Override
			public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);
				viewHolder.itemView.setAlpha(1.0f);
			}

			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				int fromPosition = viewHolder.getAdapterPosition();
				int toPosition = target.getAdapterPosition();

				Collections.swap(dataList, fromPosition, toPosition);
				Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT) {
					try {
						adapter.sendItem(viewHolder.getAdapterPosition());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (direction == ItemTouchHelper.LEFT)
					adapter.deleteItem(viewHolder.getAdapterPosition());
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
		
		AdaptersViewModel viewModel = new ViewModelProvider(requireActivity()).get(AdaptersViewModel.class);
		viewModel.addPendingAdapter(adapter);
	}

	@Override
	public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
		itemTouchHelper.startDrag(viewHolder);
	}
}