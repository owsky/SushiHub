package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Utente {
    @PrimaryKey(autoGenerate = true)
    public int idUtente;

    public String username;
}
