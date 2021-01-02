package com.veneto_valley.veneto_valley.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Piatto {
	@PrimaryKey
	@NonNull
	public final String idPiatto;
	
	public final String nomePiatto;
	
	public float prezzoPiatto;
	
	public Piatto(@NonNull String idPiatto, String nomePiatto) {
		this.idPiatto = idPiatto;
		this.nomePiatto = nomePiatto;
	}
}