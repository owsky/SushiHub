package com.veneto_valley.veneto_valley.view;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.ConfirmedViewModel;
import com.veneto_valley.veneto_valley.viewmodel.DeliveredViewModel;
import com.veneto_valley.veneto_valley.viewmodel.PendingViewModel;

import java.util.function.Consumer;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ListeTabAdapter extends FragmentStateAdapter {
	@NonNull
	private final Fragment fragment;
	
	public ListeTabAdapter(@NonNull Fragment fragment) {
		super(fragment);
		this.fragment = fragment;
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		ItemTouchHelper.SimpleCallback callback;
		OrdiniAdapter adapter = new OrdiniAdapter();
		switch (position) {
			case 0:
				PendingViewModel pendingViewModel = ViewModelUtil.getViewModel(fragment.requireActivity(), PendingViewModel.class);
				callback = makeCallback(fragment.requireContext(),
						ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
						integer -> pendingViewModel.sendToMaster(adapter.getOrdineAt(integer)),
						integer -> pendingViewModel.delete(adapter.getOrdineAt(integer)),
						ContextCompat.getColor(fragment.requireContext(), R.color.green),
						ContextCompat.getColor(fragment.requireContext(), R.color.colorPrimary),
						R.drawable.ic_send, R.drawable.ic_delete);
				return new ListaOrdiniGenericaPage(adapter, pendingViewModel.getOrdini(), callback);
			case 1:
				ConfirmedViewModel confirmedViewModel = ViewModelUtil.getViewModel(fragment.requireActivity(), ConfirmedViewModel.class);
				callback = makeCallback(fragment.requireContext(),
						ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
						integer -> confirmedViewModel.markAsDelivered(adapter.getOrdineAt(integer)),
						integer -> confirmedViewModel.retrieveFromMaster(adapter.getOrdineAt(integer)),
						ContextCompat.getColor(fragment.requireContext(), R.color.design_default_color_primary),
						ContextCompat.getColor(fragment.requireContext(), R.color.design_default_color_primary),
						R.drawable.ic_send, R.drawable.ic_send);
				return new ListaOrdiniGenericaPage(adapter, confirmedViewModel.getOrdini(), callback);
			default:
				DeliveredViewModel deliveredViewModel = ViewModelUtil.getViewModel(fragment.requireActivity(), DeliveredViewModel.class);
				callback = makeCallback(fragment.requireContext(),
						ItemTouchHelper.LEFT, null,
						integer -> deliveredViewModel.markAsNotDelivered(adapter.getOrdineAt(integer)),
						0, ContextCompat.getColor(fragment.requireContext(), R.color.green),
						0, R.drawable.ic_send);
				return new ListaOrdiniGenericaPage(adapter, deliveredViewModel.getOrdini(), callback);
		}
	}
	
	@Override
	public int getItemCount() {
		return 3;
	}
	
	public static ItemTouchHelper.SimpleCallback makeCallback(Context context, int dragDir2, Consumer<Integer> consumerRight, Consumer<Integer> consumerLeft, int colorRight, int colorLeft, int drawableRight, int drawableLeft) {
		return new ItemTouchHelper.SimpleCallback(0, dragDir2) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}
			
			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT)
					consumerRight.accept(viewHolder.getAdapterPosition());
				else if (direction == ItemTouchHelper.LEFT)
					consumerLeft.accept(viewHolder.getAdapterPosition());
			}
			
			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				if (dX < 0) {
					new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(colorLeft)
							.addSwipeLeftActionIcon(drawableLeft)
							.create()
							.decorate();
				} else if (dX > 0) {
					new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
							.addBackgroundColor(colorRight)
							.addSwipeRightActionIcon(drawableRight)
							.create()
							.decorate();
				}
				
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		};
	}
}
