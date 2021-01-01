package com.veneto_valley.veneto_valley.db.entities;

import android.content.Context;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.dao.UtenteDao;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity
public class Utente {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    public String idUtente;

    public String username;

    public Utente(String idUtente) {
        this.idUtente = idUtente;
    }

    public static Utente getUser(String idUtente, Context ctx){
        UtenteDao utenteDao = AppDatabase.getInstance(ctx).utenteDao();
        Utente u = utenteDao.loadById(idUtente);
        if (u == null) {
            Utente tmp = new Utente(idUtente);
            utenteDao.insertAll(tmp);
            u = utenteDao.loadById(idUtente);
        }
        return u;
    }

    public static Utente getCurrentUser(Context ctx){
        String myId = "LaRRRRRRRetta"; // TODO: Inserire funzione per ottenere id nearby
        return getUser(myId,ctx);
    }

    public static void deleteAllButCurrentUser(Context ctx){
        String myId = "LaRRRRRRRetta"; // TODO: Inserire funzione per ottenere id nearby
        UtenteDao utenteDao = AppDatabase.getInstance(ctx).utenteDao();
        OrdineDao ordineDao = AppDatabase.getInstance(ctx).ordineDao();

        ordineDao.deleteNotUser(myId);
        utenteDao.deleteNotUser(myId);
    }
}
