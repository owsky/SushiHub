package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ristorante {
    @PrimaryKey
    public int idRistorante;

    public String nome;
}
