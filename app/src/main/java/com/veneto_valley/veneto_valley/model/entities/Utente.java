package com.veneto_valley.veneto_valley.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Utente {
    @PrimaryKey(autoGenerate = false)
    public long idUtente;

    public String username;

    public Utente(String username) {
        this.username = username;
    }
}
