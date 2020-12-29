package com.veneto_valley.veneto_valley.db.entities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Tavolo {
    final static String TAG = "ETavoloLog";
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String idTavolo;

    public String nome;

    public Date dataCreazione;
    public int maxPiatti;
    public float costoMenu;

    public int ristorante;


    @Ignore
    public Tavolo(String idTavolo, int maxPiatti, float costoMenu) {
        this.idTavolo = idTavolo;
        this.maxPiatti = maxPiatti;
        this.costoMenu = costoMenu;
        this.dataCreazione = Calendar.getInstance().getTime();
        Log.d(TAG,"Current time => " + this.dataCreazione);
    }

    public Tavolo(String idTavolo, String nomeTavolo, int maxPiatti, float costoMenu) {
       this(idTavolo, maxPiatti, costoMenu);
       this.nome = nomeTavolo;
    }

    public Tavolo(String idTavolo, int maxPiatti, float costoMenu, int ristorante) {
        this(idTavolo, maxPiatti, costoMenu);
        this.ristorante = ristorante;
    }
}
