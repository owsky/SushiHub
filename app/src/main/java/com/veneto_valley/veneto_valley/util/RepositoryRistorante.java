package com.veneto_valley.veneto_valley.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.view.ListaRistorantiAdapter;

import java.util.ArrayList;
import java.util.List;

public class RepositoryRistorante {
	private final List<Ristorante> ristoranti = new ArrayList<>();
	
	public RepositoryRistorante(ListaRistorantiAdapter adapter) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference("ristoranti");
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot d : snapshot.getChildren()) {
					Ristorante tmp = d.getValue(Ristorante.class);
					// Escludo l'id dalla sync quindi devo settarlo a mano
					if (tmp != null) {
						tmp.idRistorante = d.getKey();
						ristoranti.add(tmp);
						adapter.notifyItemInserted(ristoranti.size());
					}
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.w("FBTest", "loadPost:onCancelled", error.toException());
			}
		});
	}
	
	public List<Ristorante> getRistoranti() {
		return ristoranti;
	}
}
