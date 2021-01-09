package com.veneto_valley.veneto_valley.model.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

// Firebase entity
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

    @NonNull
    @Override
    public String toString() {
        return this.nome + " - " + this.localita;
    }
}


