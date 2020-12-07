package com.veneto_valley.veneto_valley;

public class Ordine {
	public String codice, descrizione;
	public int quantita;
	
	public Ordine() {
	
	}
	
	public Ordine(String codice) {
		this.codice = codice;
		this.quantita = 1;
	}
	
	public Ordine(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.quantita = 1;
	}
	
	public Ordine(String codice, String descrizione, int quantita) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.quantita = quantita;
	}
}
