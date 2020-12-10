package com.veneto_valley.veneto_valley.db.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"idOrdine","idPiatto"})
public class OrdiniPiattiCrossRef {
    public int idOrdine;
    public int idPiatto;
}

