package com.veneto_valley.veneto_valley;

public class Ordine {
	public String codice, descrizione;
	
	public Ordine(String codice) {
		this.codice = codice;
	}
	
	public Ordine(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}
}
