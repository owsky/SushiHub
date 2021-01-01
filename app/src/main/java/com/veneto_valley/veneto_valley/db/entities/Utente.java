package com.veneto_valley.veneto_valley.db.entities;

import android.content.Context;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.UtenteDao;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity
public class Utente {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    public String idUtente;

    public String username;

    public Utente(String idUtente, String username) {
        this.idUtente = idUtente;
        this.username = username;
    }

    public static Utente getCurrentUser(Context ctx){
        String myId = "LaRRRRRRRetta"; // TODO: Inserire funzione per ottenere id nearby
        UtenteDao utenteDao = AppDatabase.getInstance(ctx).utenteDao();
        Utente u = utenteDao.loadById(myId);
        if (u == null) {
            Utente tmp = new Utente(myId,"Test");
            utenteDao.insertAll(tmp);
            u = utenteDao.loadById(myId);
        }
        return u;
    }
}
