package com.veneto_valley.veneto_valley.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.List;

public class OrdiniTavolo {
	@Embedded
	public Tavolo tavolo;
	@Relation(
			parentColumn = "idTavolo",
			entityColumn = "tavolo"
	)
	public List<Ordine> ordini;
}
