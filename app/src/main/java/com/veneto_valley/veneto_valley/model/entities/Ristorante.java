package com.veneto_valley.veneto_valley.model.entities;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.veneto_valley.veneto_valley.util.RepositoryRistorante;

import java.util.ArrayList;


@IgnoreExtraProperties
public class Ristorante {
    @Exclude
    public String idRistorante;
    public String nome;
    public String indirizzo;
    public String localita;

    public Ristorante() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    //TODO: Si può eliminare?
    public Ristorante(String nome) {
        this.nome = nome;
    }

    public Ristorante(String idRistorante, String nome, String indirizzo, String localita) {
        this.idRistorante = idRistorante;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.localita = localita;
    }

    public static ArrayList<Ristorante> getRistoranti() {
        ArrayList<Ristorante> ristoranteArrayList = new ArrayList<>();
        ristoranteArrayList.add(new Ristorante("GiappoTV", "SanShi Treviso", "Via dalle palle 12", "Treviso (TV)"));
        ristoranteArrayList.add(new Ristorante("GiappoPD", "Sushiko Padova", "Via dalle palle 11", "Padova (PD)"));
        return ristoranteArrayList;
    }
}


