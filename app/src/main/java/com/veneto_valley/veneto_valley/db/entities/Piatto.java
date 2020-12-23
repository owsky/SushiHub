package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Piatto {
    @PrimaryKey(autoGenerate = true)
    private long idPiatto;

    private String nomePiatto;

    private float prezzoPiatto;

    public Piatto(){}
    public Piatto(String nomePiatto) {
        this.nomePiatto = nomePiatto;
    }

    public float getPrezzoPiatto() {
        return prezzoPiatto;
    }

    public void setPrezzoPiatto(float prezzoPiatto) {
        this.prezzoPiatto = prezzoPiatto;
    }

    public String getNomePiatto() {
        return nomePiatto;
    }

    public void setNomePiatto(String nomePiatto) {
        this.nomePiatto = nomePiatto;
    }

    public long getIdPiatto() {
        return idPiatto;
    }

    public void setIdPiatto(long idPiatto) {
        this.idPiatto = idPiatto;
    }
}