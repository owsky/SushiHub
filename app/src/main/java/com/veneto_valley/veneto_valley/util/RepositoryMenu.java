package com.veneto_valley.veneto_valley.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.model.entities.Category;
import com.veneto_valley.veneto_valley.model.entities.Dish;
import com.veneto_valley.veneto_valley.view.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class RepositoryMenu {
	
	private final List<Category> category;
	
	public RepositoryMenu(MenuAdapter adapter, String id) {
		category = new ArrayList<>();
		category.add(new Category("No category selected"));
		
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference("menu").child(id);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot d : snapshot.getChildren()) {
					String s = d.getKey();
					Category tmpCat = new Category(s);
					category.add(tmpCat);
					
					for (DataSnapshot p : d.getChildren()) {
						Dish tmpDish = p.getValue(Dish.class);
						if (tmpDish != null && p.getKey() != null) {
							tmpDish.id = p.getKey();
							int index = category.indexOf(tmpCat);
							category.get(index).dishes.add(tmpDish);
						}
					}
					adapter.notifyItemInserted(category.size());
					
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.w("FBTest", "loadPost:onCancelled", error.toException());
			}
		});
	}
	
	public List<Category> getCategory() {
		return category;
	}
}
