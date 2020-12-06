package com.veneto_valley.veneto_valley;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
	private final Drawable divider;
	
	public MyDividerItemDecoration(Context context) {
		divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
	}
	
	@Override
	public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		int dividerLeft = parent.getPaddingLeft();
		int dividerRight = parent.getWidth() - parent.getPaddingRight();
		
		int childCount = parent.getChildCount();
		for (int i = 0; i <= childCount - 2; i++) {
			View child = parent.getChildAt(i);
			
			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			
			int dividerTop = child.getBottom() + params.bottomMargin;
			int dividerBottom = dividerTop + divider.getIntrinsicHeight();
			
			divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
			divider.draw(c);
		}
	}
}
