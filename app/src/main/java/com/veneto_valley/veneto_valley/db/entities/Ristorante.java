package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ristorante {
    @PrimaryKey
    private long idRistorante;

    private String nome;

    public Ristorante(String nome) {
        this.nome = nome;
    }

    public long getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(long idRistorante) {
        this.idRistorante = idRistorante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
