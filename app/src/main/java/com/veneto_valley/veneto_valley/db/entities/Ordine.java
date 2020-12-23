package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ordine {
    @PrimaryKey(autoGenerate = true)
    private long idOrdine;

    private String status;

    //1-N Relations
    private int tavolo;
    private int piatto;

    public Ordine(int tavolo, int piatto) {
        this.tavolo = tavolo;
        this.piatto = piatto;
    }

    public long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(long idOrdine) {
        this.idOrdine = idOrdine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTavolo() {
        return tavolo;
    }

    public void setTavolo(int tavolo) {
        this.tavolo = tavolo;
    }

    public int getPiatto() {
        return piatto;
    }

    public void setPiatto(int piatto) {
        this.piatto = piatto;
    }
}
