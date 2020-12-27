package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Ordine {
    @PrimaryKey(autoGenerate = true)
    public long idOrdine;

    public String status;
    public int quantita;
    public String desc;

    //1-N Relations
    public String tavolo;
    public String piatto;
    public long utente;

    public Ordine(String tavolo, String piatto, int quantita) {
        this.tavolo = tavolo;
        this.piatto = piatto;
        this.quantita = quantita;
        this.desc = "";
    }

    @Ignore
    public Ordine(String tavolo, String piatto) {
        this(tavolo,piatto,1);
    }
}
