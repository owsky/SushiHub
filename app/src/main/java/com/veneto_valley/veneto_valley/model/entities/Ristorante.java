package com.veneto_valley.veneto_valley.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

//TODO: Proposta: rimuoviamo dal db locale?
@Entity
public class Ristorante {
	@PrimaryKey
	@NonNull
	public String idRistorante;
	public String nome;
	public String indirizzo;
	public String localita;

	@Ignore
	public Ristorante(String nome) {
		this.nome = nome;
	}

	public Ristorante(String idRistorante, String nome, String indirizzo, String localita) {
		this.nome = nome;
		this.idRistorante = idRistorante;
		this.indirizzo = indirizzo;
		this.localita = localita;
	}

	public ArrayList<Ristorante> getRistoranti(){
		ArrayList<Ristorante> ristoranteArrayList = new ArrayList<>();
		ristoranteArrayList.add(new Ristorante("GiappoTV", "SanShi Treviso", "Via dalle palle 12", "Treviso (TV)"));
		ristoranteArrayList.add(new Ristorante("GiappoPD", "Sushiko Padova", "Via dalle palle 11", "Padova (PD)"));
		return  ristoranteArrayList;
	}
}
