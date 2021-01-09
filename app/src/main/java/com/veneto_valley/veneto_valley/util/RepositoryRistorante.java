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

    private LinearLayout linLay = null;
    private ArrayList<String> arrayList = null;

    public ValueEventListener RistoranteFirebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot d: snapshot.getChildren()){
                Ristorante tmp = d.getValue(Ristorante.class);
                // Escludo l'id dalla sync quindi devo settarlo a mano
                tmp.idRistorante = d.getKey();
                TextView tv = new TextView(linLay.getContext());
                String s = tmp.idRistorante + " - " + tmp.indirizzo;
                tv.setText(s);
                linLay.addView(tv);
                if (arrayList != null) {
                    arrayList.add(s);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("FBTest", "loadPost:onCancelled", error.toException());
        }
    };

    public RepositoryRistorante(LinearLayout linLay, ArrayList<String> arrayList) {
        this.linLay = linLay;
        this.arrayList = arrayList;
    }
}
