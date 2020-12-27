package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ordine {
    @PrimaryKey(autoGenerate = true)
    private long idOrdine;

    private String status;
    private int quantita;
    private String desc;

    //1-N Relations
    private String tavolo;
    private String piatto;

    public Ordine(String tavolo, String piatto, int quantita) {
        this.tavolo = tavolo;
        this.piatto = piatto;
        this.quantita = quantita;
        this.desc = "";
    }

    public Ordine(String tavolo, String piatto) {
        this(tavolo,piatto,1);
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getTavolo() {
        return tavolo;
    }

    public void setTavolo(String tavolo) {
        this.tavolo = tavolo;
    }

    public String getPiatto() {
        return piatto;
    }

    public void setPiatto(String piatto) {
        this.piatto = piatto;
    }
}
