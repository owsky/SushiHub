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

    private LinearLayout linLay = null;
    private ArrayList<Categoria> categoria = new ArrayList<>();

    public ValueEventListener MenuFirebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.w("FBTest", snapshot.getKey());
            linLay.removeAllViews();
            for (DataSnapshot d: snapshot.getChildren()){
                String s = d.getKey();
                Log.w("FBTest", s);
                TextView tv = new TextView(linLay.getContext());
                tv.setText(s);
                linLay.addView(tv);
                Categoria tmpCat = new Categoria(s);
                categoria.add(tmpCat);

                for (DataSnapshot p: d.getChildren()){
                    Piatto tmpPiatto = p.getValue(Piatto.class);
                    tmpPiatto.idPiatto = p.getKey();
                    int index = categoria.indexOf(tmpCat);
                    categoria.get(index).piatti.add(tmpPiatto);
                    // Escludo l'id dalla sync quindi devo settarlo a mano
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("FBTest", "loadPost:onCancelled", error.toException());
        }
    };

    public RepositoryMenu(LinearLayout linLay) {

        this.linLay = linLay;
    }
    public ArrayList<Categoria> getCategoria(){
        return categoria;
    }
}
