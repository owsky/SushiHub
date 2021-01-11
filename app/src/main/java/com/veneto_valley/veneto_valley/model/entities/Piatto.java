package com.veneto_valley.veneto_valley.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@Entity
@IgnoreExtraProperties
public class Piatto {
	@PrimaryKey
	@NonNull
	@Exclude
	public String idPiatto;
	
	public String nome;
	public String descrizione;
	public float prezzo;
	
	public Piatto(@NonNull String idPiatto, String nome, String descrizione) {
		this.idPiatto = idPiatto;
		this.nome = nome;
		this.descrizione = descrizione;
	}

	@NonNull
	@Override
	public String toString() {
		return this.idPiatto + " - " + this.nome;
	}

	@Ignore
	public Piatto(){
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}
}