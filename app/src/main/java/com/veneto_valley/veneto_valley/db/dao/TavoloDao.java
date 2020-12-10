package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.db.entities.Tavolo;
import com.veneto_valley.veneto_valley.db.entities.Utente;
import com.veneto_valley.veneto_valley.db.relations.OrdiniTavolo;

import java.util.List;

@Dao
public interface TavoloDao {
    @Query("SELECT * FROM tavolo")
    List<Tavolo> getAll();

    @Query("SELECT * FROM tavolo WHERE idTavolo IN (:idTavoli)")
    List<Utente> loadAllByIds(int[] idTavoli);

    @Insert
    void insertAll(Tavolo... tavoli);

    @Delete
    void delete(Tavolo tavolo);

    //Relazioni
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM tavolo WHERE idTavolo IN (:idTavolo)")
    List<OrdiniTavolo> getOrdiniTavolo(int idTavolo);
}
