package com.veneto_valley.veneto_valley.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ristorante {
	public final String nome;
	@PrimaryKey
	public long idRistorante;
	
	public Ristorante(String nome) {
		this.nome = nome;
	}
}
