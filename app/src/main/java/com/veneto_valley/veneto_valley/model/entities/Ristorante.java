package com.veneto_valley.veneto_valley.model.entities;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class Ristorante {
	@NotNull
	public String idRistorante;
	public String nome;
	public String indirizzo;
	public String localita;

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
