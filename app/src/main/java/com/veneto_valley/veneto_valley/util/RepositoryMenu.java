package com.veneto_valley.veneto_valley.util;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;

import java.util.ArrayList;

public class RepositoryMenu {

    private ArrayList<Categoria> categorieArrayList;
    private LinearLayout linLay = null;

    public ValueEventListener MenuFirebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.w("FBTest", snapshot.getKey());
            for (DataSnapshot d: snapshot.getChildren()){
                String s = d.getKey();
                Log.w("FBTest", s);
                TextView tv = new TextView(linLay.getContext());
                tv.setText(s);
                linLay.addView(tv);
                Categoria tmpCat = new Categoria(s);

                for (DataSnapshot p: d.getChildren()){
                    Piatto tmpPiatto = p.getValue(Piatto.class);
                    // Escludo l'id dalla sync quindi devo settarlo a mano
                    tmpPiatto.idPiatto = p.getKey();
                    tmpCat.piatti.add(tmpPiatto);
                }

                categorieArrayList.add(tmpCat);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("FBTest", "loadPost:onCancelled", error.toException());
        }
    };

    public RepositoryMenu(LinearLayout linLay, ArrayList<Categoria> categorieArrayList) {
        linLay.removeAllViews();
        this.categorieArrayList = categorieArrayList;
        this.linLay = linLay;
    }
}
