package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Utente;
import com.veneto_valley.veneto_valley.db.relations.UtentiOrdine;

import java.util.List;

@Dao
public interface OrdineDao {
    @Query("SELECT * FROM ordine")
    List<Ordine> getAll();

    @Query("SELECT * FROM ordine WHERE idOrdine IN (:idOrdini)")
    List<Utente> loadAllByIds(int[] idOrdini);

    @Insert
    void insertAll(Ordine... ristoranti);

    @Delete
    void delete(Ordine ordine);

    //Relazioni
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM ordine WHERE idOrdine IN (:idOrdine)")
    List<UtentiOrdine> getUtentiOrdine(int idOrdine);


}
