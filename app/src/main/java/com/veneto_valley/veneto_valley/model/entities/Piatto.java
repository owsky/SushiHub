package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Piatto implements Parcelable {
	@Exclude
	public static final Parcelable.Creator<Piatto> CREATOR = new Parcelable.Creator<Piatto>() {
		
		@Override
		public Piatto createFromParcel(Parcel source) {
			return new Piatto(source);
		}
		
		@Override
		public Piatto[] newArray(int size) {
			return new Piatto[size];
		}
	};
	
	@Exclude
	public String idPiatto;
	public String nome;
	public float prezzo;
	
	public Piatto() {
		// Default constructor required for calls to DataSnapshot.getValue(Piatto.class)
	}
	
	protected Piatto(Parcel in) {
		idPiatto = in.readString();
		nome = in.readString();
		prezzo = in.readFloat();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.idPiatto + " - " + this.nome;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	@Exclude
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(idPiatto);
		dest.writeString(nome);
		dest.writeFloat(prezzo);
	}
	
}