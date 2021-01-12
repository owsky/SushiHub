package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.veneto_valley.veneto_valley.util.ParcelableUtil;

@Entity
@IgnoreExtraProperties
public class Ordine implements Parcelable {
	@Exclude
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
	@Exclude
	public long idOrdine;
	@Exclude
	public StatusOrdine status;
	// TODO rimuovere quantità
	public int quantita;
	public String desc;
	@Exclude
	public float prezzo;
	@Exclude
	public boolean receivedFromSlave;
	
	//1-N Relations
	@Exclude
	public String tavolo;
	public String piatto;
	@Exclude
	public String utente;
	
	@Ignore
	public Ordine() {
	}
	
	//TODO: Implementare test
	public Ordine(String tavolo, String piatto, int quantita, StatusOrdine status, String utente, boolean receivedFromSlave) {
		this.tavolo = tavolo;
		this.piatto = piatto;
		this.quantita = quantita;
		this.status = status;
		this.utente = utente;
		this.receivedFromSlave = receivedFromSlave;
	}
	
	protected Ordine(Parcel in) {
		status = StatusOrdine.valueOf(in.readString());
		quantita = in.readInt();
		desc = in.readString();
		tavolo = in.readString();
		piatto = in.readString();
		utente = in.readString();
	}
	
	@Exclude
	public static Ordine getFromBytes(byte[] ordine) {
		Parcel parcel = ParcelableUtil.unmarshall(ordine);
		return new Ordine(parcel);
	}
	
	@Exclude
	public byte[] getBytes() {
		return ParcelableUtil.marshall(this);
	}
	
	@Override
	@Exclude
	public int describeContents() {
		return 0;
	}
	
	@Override
	@Exclude
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(status.toString());
		dest.writeInt(quantita);
		dest.writeString(desc);
		dest.writeString(tavolo);
		dest.writeString(piatto);
		dest.writeString(utente);
	}
	
	public enum StatusOrdine {
		pending,
		confirmed,
		delivered
	}
}