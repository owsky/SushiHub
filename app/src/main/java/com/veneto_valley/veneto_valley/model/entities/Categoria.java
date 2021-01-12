package com.veneto_valley.veneto_valley.model.entities;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Categoria {
	public final String nomeCategoria;
	public final ArrayList<Piatto> piatti;
	
	public Categoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
		piatti = new ArrayList<>();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.nomeCategoria;
	}
}
