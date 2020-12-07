package com.veneto_valley.veneto_valley;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PendingOrdersFragment extends Fragment implements OrdiniAdapter.OnDragStartListener, OrdiniAdapter.OnOrderClickListener {
	private final List<Ordine> listaOrdini;
	private OrdiniAdapter adapter;
	
	private ItemTouchHelper itemTouchHelper;
	
	public PendingOrdersFragment() {
		super(R.layout.fragment_pending_orders);
		listaOrdini = new ArrayList<>();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		listaOrdini.add(new Ordine("1", "Ravioli"));
		listaOrdini.add(new Ordine("7", "Cinghiale"));
		listaOrdini.add(new Ordine("55", "Yaki Udon"));
		listaOrdini.add(new Ordine("101", "Sake Nigiri"));
		
		adapter = new OrdiniAdapter(requireActivity(), listaOrdini, this, this);
		
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPending);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
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

				Collections.swap(listaOrdini, fromPosition, toPosition);
				Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT)
					adapter.sendItem(viewHolder.getAdapterPosition());
				else if (direction == ItemTouchHelper.LEFT)
					adapter.deleteItem(viewHolder.getAdapterPosition());
			}

			@Override
			public void onChildDraw (@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
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
	
	private void showDialog() {
		FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
		ModificaPiattoDialog modificaPiattoDialog = new ModificaPiattoDialog(listaOrdini, adapter);
		modificaPiattoDialog.show(fragmentManager, null);
	}
	
	private void showDialog(int position) {
		FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
		ModificaPiattoDialog modificaPiattoDialog = new ModificaPiattoDialog(listaOrdini, adapter, position);
		modificaPiattoDialog.show(fragmentManager, null);
	}
	
	@Override
	public void onOrderClick(int position) {
		showDialog(position);
	}
	
	@Override
	public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
		itemTouchHelper.startDrag(viewHolder);
	}
}