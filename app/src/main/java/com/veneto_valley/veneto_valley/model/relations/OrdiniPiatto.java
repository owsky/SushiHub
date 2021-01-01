package com.veneto_valley.veneto_valley.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Piatto;

import java.util.List;

public class OrdiniPiatto {
	@Embedded
	public Piatto piatto;
	@Relation(
			parentColumn = "idPiatto",
			entityColumn = "piatto"
	)
	public List<Ordine> ordine;
}
