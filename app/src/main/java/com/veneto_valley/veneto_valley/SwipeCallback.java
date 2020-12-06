package com.veneto_valley.veneto_valley;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {
	private final OrdiniAdapter adapter;
	private Drawable icon;
	private final ColorDrawable background;

	public SwipeCallback(OrdiniAdapter adapter, Context context) {
		super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
		this.adapter = adapter;
		icon = ContextCompat.getDrawable(context,
				R.drawable.ic_delete);
		background = new ColorDrawable(Color.RED);
	}
	
	@Override
	public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
		return false;
	}
	
	@Override
	public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
		int position = viewHolder.getAdapterPosition();
		adapter.deleteItem(position);
	}
	
	@Override
	public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		View itemView = viewHolder.itemView;
		int backgroundCornerOffset = 20;
		int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight() / 2);
		int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight() / 2);
		int iconBottom = iconTop + icon.getIntrinsicHeight();
		
		if (dX > 0) {
			int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
			int iconRight = itemView.getLeft() + iconMargin;
			icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
			
			background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
		} else if (dX < 0) {
			int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
			int iconRight = itemView.getRight() - iconMargin;
			icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
			
			background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
		} else {
			background.setBounds(0, 0, 0, 0);
		}
		background.draw(c);
		icon.draw(c);
	}
}
