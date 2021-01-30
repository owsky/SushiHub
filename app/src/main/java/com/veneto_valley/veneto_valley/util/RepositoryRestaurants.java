package com.veneto_valley.veneto_valley.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.model.entities.Restaurant;
import com.veneto_valley.veneto_valley.view.ListRestaurantsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RepositoryRestaurants {
    public final List<Restaurant> restaurants = new ArrayList<>();

    public RepositoryRestaurants(ListRestaurantsAdapter adapter) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("restaurants");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Restaurant tmp = d.getValue(Restaurant.class);
                    if (tmp != null) {
                        tmp.id = d.getKey();
                        restaurants.add(tmp);
                        adapter.notifyItemInserted(restaurants.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FBTest", "loadPost:onCancelled", error.toException());
            }
        });
    }
}
