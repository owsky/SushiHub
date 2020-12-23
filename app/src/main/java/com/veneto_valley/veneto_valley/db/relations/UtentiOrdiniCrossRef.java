package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"idUtente", "idOrdine"})
public class UtentiOrdiniCrossRef {
    public long idUtente;
    public long idOrdine;
}
