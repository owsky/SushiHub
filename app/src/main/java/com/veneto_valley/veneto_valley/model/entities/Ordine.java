package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Entity
public class Ordine implements Parcelable {
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
	@PrimaryKey(autoGenerate = true)
	public long idOrdine;
	public String status;
	public int quantita;
	public String desc;
	public float prezzo;
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
		this.prezzo = 0;
	}
	
	@Ignore
	public Ordine(String tavolo, String piatto, int quantita, String status, Float prezzo) {
		this(tavolo, piatto, quantita, status);
		this.prezzo = prezzo;
	}
	
	@Ignore
	public Ordine(String tavolo, String piatto, int quantita) {
		this(tavolo, piatto, quantita, "daOrdinare");
	}
	
	@Ignore
	public Ordine(String tavolo, String piatto) {
		this(tavolo, piatto, 1);
	}
	
	protected Ordine(Parcel in) {
		status = in.readString();
		quantita = in.readInt();
		desc = in.readString();
		tavolo = in.readString();
		piatto = in.readString();
		utente = in.readLong();
	}
	
	public static Ordine getFromBytes(byte[] ordine) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(ordine);
		ObjectInputStream is = new ObjectInputStream(in);
		return (Ordine) is.readObject();
	}
	
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.flush();
		return bos.toByteArray();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(status);
		dest.writeInt(quantita);
		dest.writeString(desc);
		dest.writeString(tavolo);
		dest.writeString(piatto);
		dest.writeLong(utente);
	}
}