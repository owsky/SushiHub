package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Piatto;

public class OrdinePiatto {
        @Embedded public Ordine ordine;
        @Relation(
                parentColumn = "idOrdine",
                entityColumn = "userOwnerId"
        )
        public Piatto piatto;
    }
