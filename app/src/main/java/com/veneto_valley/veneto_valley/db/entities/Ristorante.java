package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ristorante {
    @PrimaryKey
    public long idRistorante;

    public String nome;

    public Ristorante(String nome) {
        this.nome = nome;
    }
}
