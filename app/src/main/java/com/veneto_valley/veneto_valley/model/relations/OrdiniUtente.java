package com.veneto_valley.veneto_valley.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Utente;

import java.util.List;

public class OrdiniUtente {

    @Embedded public Utente utente;
    @Relation(
            parentColumn = "idUtente",
            entityColumn = "utente"
    )
    public List<Ordine> ordini;


}
