package com.veneto_valley.veneto_valley.db.entities;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Tavolo {
    final static String TAG = "ETavoloLog";
    @NotNull
    @PrimaryKey(autoGenerate = false)
    public String idTavolo;

    public String nome;

    public Date dataCreazione;
    public int maxPiatti;
    public float costoMenu;

    public int ristorante;

    @Ignore
    public Tavolo(String idTavolo, int maxPiatti, float costoMenu) {
        this.idTavolo = idTavolo;
        this.maxPiatti = maxPiatti;
        this.costoMenu = costoMenu;
        this.dataCreazione = Calendar.getInstance().getTime();
        Log.d(TAG,"Current time => " + this.dataCreazione);
    }

    @Ignore
    public Tavolo(String idTavolo, String nomeTavolo, int maxPiatti, float costoMenu) {
       this(idTavolo, maxPiatti, costoMenu);
       this.nome = nomeTavolo;
    }

    public Tavolo(String idTavolo, int maxPiatti, float costoMenu, int ristorante) {
        this(idTavolo, maxPiatti, costoMenu);
        this.ristorante = ristorante;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Tavolo)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Tavolo t = (Tavolo) obj;

        // Compare the data members and return accordingly
        return this.idTavolo.equals(t.idTavolo); //TODO: Ampliare Confronto
    }
}
