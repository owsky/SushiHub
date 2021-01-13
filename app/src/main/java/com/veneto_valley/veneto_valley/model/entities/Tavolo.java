package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Tavolo implements Parcelable {
	public static final Parcelable.Creator<Tavolo> CREATOR = new Parcelable.Creator<Tavolo>() {
		@Override
		public Tavolo createFromParcel(Parcel in) {
			return new Tavolo(in);
		}
		
		@Override
		public Tavolo[] newArray(int size) {
			return new Tavolo[size];
		}
	};
	final static String TAG = "ETavoloLog";
	@PrimaryKey
	@NonNull
	public final String idTavolo;
	public String nome;
	public Date dataCreazione;
	public float costoMenu;
	public String ristorante;
	public boolean checkedOut;
	
	public Tavolo(@NonNull String idTavolo) {
		this.idTavolo = idTavolo;
	}
	
	@Ignore
	public Tavolo(@NonNull String idTavolo, float costoMenu) {
		this.idTavolo = idTavolo;
		this.costoMenu = costoMenu;
		this.dataCreazione = Calendar.getInstance().getTime();
		checkedOut = false;
		Log.d(TAG, "Current time => " + this.dataCreazione);
	}
	
	@Ignore
	public Tavolo(String idTavolo, String nomeTavolo, float costoMenu) {
		this(idTavolo, costoMenu);
		this.nome = nomeTavolo;
	}
	
	@Ignore
	public Tavolo(String idTavolo, float costoMenu, String ristorante) {
		this(idTavolo, costoMenu);
		this.ristorante = ristorante;
	}
	
	@Ignore
	protected Tavolo(Parcel in) {
		idTavolo = in.readString();
		nome = in.readString();
		long tmpDataCreazione = in.readLong();
		dataCreazione = tmpDataCreazione != -1 ? new Date(tmpDataCreazione) : null;
		costoMenu = in.readFloat();
		ristorante = in.readString();
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
		return this.idTavolo.equals(t.idTavolo);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(idTavolo);
		dest.writeString(nome);
		dest.writeLong(dataCreazione != null ? dataCreazione.getTime() : -1L);
		dest.writeFloat(costoMenu);
		dest.writeString(ristorante);
	}
}
