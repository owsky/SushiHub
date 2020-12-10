package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Tavolo {
    @PrimaryKey(autoGenerate = true)
    public int idTavolo;

    public String nome;
    public Date data;
    public int maxPiatti;
    public float costoMenu;

    public int ristorante;
}
