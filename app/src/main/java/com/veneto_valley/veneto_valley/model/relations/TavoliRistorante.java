package com.veneto_valley.veneto_valley.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.List;

public class TavoliRistorante {
	@Embedded
	public Ristorante ristorante;
	@Relation(
			parentColumn = "idRistorante",
			entityColumn = "ristorante"
	)
	public List<Tavolo> tavoli;
}
