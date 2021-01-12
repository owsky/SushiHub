package com.veneto_valley.veneto_valley.util;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;

import java.util.ArrayList;

public class RepositoryRistorante {

    private ArrayList<Ristorante> ristoranti = new ArrayList<>();

    public ValueEventListener RistoranteFirebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot d: snapshot.getChildren()){
                Ristorante tmp = d.getValue(Ristorante.class);
                // Escludo l'id dalla sync quindi devo settarlo a mano
                tmp.idRistorante = d.getKey();
                ristoranti.add(tmp);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("FBTest", "loadPost:onCancelled", error.toException());
        }
    };

    public RepositoryRistorante() {
    }
    public ArrayList<Ristorante> getRistoranti(){
        return ristoranti;
    }
}
