package com.veneto_valley.veneto_valley.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Piatto {
	@NonNull
	@PrimaryKey(autoGenerate = false)
	public String idPiatto;
	
	public String nomePiatto;
	
	public float prezzoPiatto;
	
	@Ignore
	public Piatto() {
	}
	
	@Ignore
	public Piatto(String idPiatto) {
		this(idPiatto, "Piatto " + idPiatto);
	}
	
	public Piatto(@NonNull String idPiatto, String nomePiatto) {
		this.idPiatto = idPiatto;
		this.nomePiatto = nomePiatto;
	}
}