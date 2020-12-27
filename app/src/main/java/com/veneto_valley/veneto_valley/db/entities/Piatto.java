package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Piatto {
    @NotNull
    @PrimaryKey(autoGenerate = false)
    public String idPiatto;

    public String nomePiatto;

    public float prezzoPiatto;

    @Ignore
    public Piatto(){}

    @Ignore
    public Piatto(String idPiatto) {
        this(idPiatto, "Piatto " + idPiatto);
    }

    public Piatto(String idPiatto, String nomePiatto) {
        this.idPiatto = idPiatto;
        this.nomePiatto = nomePiatto;
    }
}