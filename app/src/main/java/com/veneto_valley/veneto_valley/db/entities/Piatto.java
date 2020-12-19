package com.veneto_valley.veneto_valley.db.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO: Rendere tutto privato
@Entity
public class Piatto {
    @PrimaryKey(autoGenerate = true)
    public int idPiatto;

    public String nomePiatto;

    @Nullable
    public float prezzoPiatto;

    public Piatto(String nomePiatto) {
        this.nomePiatto = nomePiatto;
    }
}