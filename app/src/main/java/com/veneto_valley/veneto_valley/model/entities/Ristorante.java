package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

// Firebase entity
@IgnoreExtraProperties
public class Ristorante implements Parcelable {
	@Exclude
	public static final Parcelable.Creator<Ristorante> CREATOR = new Parcelable.Creator<Ristorante>() {
		@Override
		public Ristorante createFromParcel(Parcel in) {
			return new Ristorante(in);
		}
		
		@Override
		public Ristorante[] newArray(int size) {
			return new Ristorante[size];
		}
	};
	@Exclude
	public String idRistorante;
	public String nome;
	public String indirizzo;
	public String localita;
	public float costoMenu;
	
	public Ristorante() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}
	
	protected Ristorante(Parcel in) {
		idRistorante = in.readString();
		nome = in.readString();
		indirizzo = in.readString();
		localita = in.readString();
		costoMenu = in.readFloat();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.nome + " - " + this.localita;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(idRistorante);
		dest.writeString(nome);
		dest.writeString(indirizzo);
		dest.writeString(localita);
		dest.writeFloat(costoMenu);
	}
}


