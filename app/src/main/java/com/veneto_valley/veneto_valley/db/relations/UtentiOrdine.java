package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Utente;

import java.util.List;

public class UtentiOrdine {
    @Embedded
    public Ordine ordine;
    @Relation(
            parentColumn = "idOrdine",
            entityColumn = "idUtente",
            associateBy = @Junction(UtentiOrdiniCrossRef.class)
    )
    public List<Utente> utenti;
}
