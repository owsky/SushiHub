package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Ristorante;
import com.veneto_valley.veneto_valley.db.entities.Tavolo;

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
