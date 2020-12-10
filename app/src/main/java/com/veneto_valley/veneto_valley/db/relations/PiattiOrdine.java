package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Piatto;

import java.util.List;

public class PiattiOrdine {
    @Embedded
    public Ordine ordine;
    @Relation(
            parentColumn = "idOrdine",
            entityColumn = "idPiatto",
            associateBy = @Junction(OrdiniPiattiCrossRef.class)
    )
    public List<Piatto> piatti;
}
