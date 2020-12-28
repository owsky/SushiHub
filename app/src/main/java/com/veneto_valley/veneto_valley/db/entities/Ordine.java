package com.veneto_valley.veneto_valley.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Entity
public class Ordine {
    @PrimaryKey(autoGenerate = true)
    public long idOrdine;

    public String status;
    public int quantita;
    public String desc;

    //1-N Relations
    public String tavolo;
    public String piatto;
    public long utente;

    //TODO: Implementare test
    public Ordine(String tavolo, String piatto, int quantita, String status) {
        this.tavolo = tavolo;
        this.piatto = piatto;
        this.quantita = quantita;
        this.status = status;
    }

    @Ignore
    public Ordine(String tavolo, String piatto, int quantita) {
        this(tavolo,piatto,quantita,"daOrdinare");
    }

    @Ignore
    public Ordine(String tavolo, String piatto) {
        this(tavolo,piatto,1);
    }
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.flush();
        return bos.toByteArray();
    }
    public static Ordine getFromBytes(byte[] ordine) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(ordine);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Ordine) is.readObject();
    }
}
