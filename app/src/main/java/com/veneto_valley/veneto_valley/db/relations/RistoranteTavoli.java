package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.db.entities.Ristorante;
import com.veneto_valley.veneto_valley.db.entities.Tavolo;

import java.util.List;

public class RistoranteTavoli {
    @Embedded public Ristorante ristorante;
     @Relation(
             parentColumn = "idRistorante",
             entityColumn = "ristorante"
     )
    public List<Tavolo> tavoli;
}
