package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.veneto_valley.veneto_valley.util.ParcelableUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Entity
public class Ordine implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long idOrdine;
    
    public statusOrdine status;
    public int quantita;
    public String desc;
    public float prezzo;

    public enum statusOrdine {
        pending,
        confirmed,
        delivered
    }

    //1-N Relations
    public String tavolo;
    public String piatto;
    public String utente;
    
    //TODO: Implementare test
    public Ordine(String tavolo, String piatto, int quantita, statusOrdine status) {
        this.tavolo = tavolo;
        this.piatto = piatto;
        this.quantita = quantita;
        this.status = status;
    }
    
    @Ignore
    public Ordine(String tavolo, String piatto, int quantita) {
        this(tavolo,piatto,quantita,statusOrdine.pending);
    }
    
    @Ignore
    public Ordine(String tavolo, String piatto) {
        this(tavolo,piatto,1);
    }
    public byte[] getBytes() throws IOException {
        byte[] oggetto = ParcelableUtil.marshall(this);
        return oggetto;
    }
    public static Ordine getFromBytes(byte[] ordine) throws IOException, ClassNotFoundException {
        Parcel parcel = ParcelableUtil.unmarshall(ordine);
        return new Ordine(parcel);
    }
    
    protected Ordine(Parcel in) {
        status = statusOrdine.valueOf(in.readString());
        quantita = in.readInt();
        desc = in.readString();
        tavolo = in.readString();
        piatto = in.readString();
        utente = in.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status.toString());
        dest.writeInt(quantita);
        dest.writeString(desc);
        dest.writeString(tavolo);
        dest.writeString(piatto);
        dest.writeString(utente);
    }
    
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ordine> CREATOR = new Parcelable.Creator<Ordine>() {
        @Override
        public Ordine createFromParcel(Parcel in) {
            return new Ordine(in);
        }
        
        @Override
        public Ordine[] newArray(int size) {
            return new Ordine[size];
        }
    };
}