package com.veneto_valley.veneto_valley.db.entities;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Tavolo {
    final static String TAG = "ETavoloLog";
    @PrimaryKey(autoGenerate = false)
    private String idTavolo;

    private String nome;

    private Date dataCreazione;
    private int maxPiatti;
    private float costoMenu;

    private int ristorante;


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

    public String getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(String idTavolo) {
        this.idTavolo = idTavolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getMaxPiatti() {
        return maxPiatti;
    }

    public void setMaxPiatti(int maxPiatti) {
        this.maxPiatti = maxPiatti;
    }

    public float getCostoMenu() {
        return costoMenu;
    }

    public void setCostoMenu(float costoMenu) {
        this.costoMenu = costoMenu;
    }

    public int getRistorante() {
        return ristorante;
    }

    public void setRistorante(int ristorante) {
        this.ristorante = ristorante;
    }
}
