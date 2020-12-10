package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ordine {
    @PrimaryKey(autoGenerate = true)
    public int idOrdine;

    public String status;

    //1-N Relations
    public int tavolo;
}
