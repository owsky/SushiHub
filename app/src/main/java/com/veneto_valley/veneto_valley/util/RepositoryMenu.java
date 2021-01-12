package com.veneto_valley.veneto_valley.util;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.view.MenuAdapter;

import java.util.ArrayList;

public class RepositoryMenu {

    private final ArrayList<Categoria> categoria;

    public RepositoryMenu(MenuAdapter adapter, String idRistorante) {
        categoria = new ArrayList<>();
        categoria.add(new Categoria("Nessun elemento selezionato"));
        
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("menu").child(idRistorante);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("FBTest", snapshot.getKey());
                for (DataSnapshot d: snapshot.getChildren()){
                    String s = d.getKey();
                    Log.w("FBTest", s);
                    Categoria tmpCat = new Categoria(s);
                    categoria.add(tmpCat);
            
                    for (DataSnapshot p: d.getChildren()){
                        Piatto tmpPiatto = p.getValue(Piatto.class);
                        if (tmpPiatto != null && p.getKey() != null) {
                            // Escludo l'id dalla sync quindi devo settarlo a mano
                            tmpPiatto.idPiatto = p.getKey();
                            int index = categoria.indexOf(tmpCat);
                            categoria.get(index).piatti.add(tmpPiatto);
                        }
                    }
                    adapter.notifyItemInserted(categoria.size());
            
                }
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FBTest", "loadPost:onCancelled", error.toException());
            }
        });
    }
    
    public ArrayList<Categoria> getCategoria(){
        return categoria;
    }
}
