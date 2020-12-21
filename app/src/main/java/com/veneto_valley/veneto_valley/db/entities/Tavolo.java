package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Tavolo {
    @PrimaryKey(autoGenerate = false)
    private int idTavolo;

    private String nome;

    private int dataCreazione;
    private int maxPiatti;
    private float costoMenu;

    private int ristorante;

    public Tavolo(int idTavolo, int maxPiatti, float costoMenu) {
        this.idTavolo = idTavolo;
        this.maxPiatti = maxPiatti;
        this.costoMenu = costoMenu;
        //TODO: dataCreazione auto generata
    }

    public Tavolo(int idTavolo, int maxPiatti, float costoMenu, int ristorante) {
        this(idTavolo, maxPiatti, costoMenu);
        this.ristorante = ristorante;
    }

    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(int dataCreazione) {
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
