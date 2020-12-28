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
public interface TavoloDao extends baseDao<Tavolo>{
    @Query("SELECT * FROM tavolo")
    List<Tavolo> getAll();

    @Query("SELECT * FROM tavolo WHERE idTavolo IN (:idTavoli)")
    List<Tavolo> loadAllByIds(String[] idTavoli);

    @Query("SELECT * FROM tavolo WHERE idTavolo = :idTavolo")
    Tavolo loadById(String idTavolo);

    @Override
    @Insert
    List<Long> insertAll(Tavolo... objs);

    //Relazioni
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM tavolo WHERE idTavolo IN (:idTavolo)")
    List<OrdiniTavolo> getOrdiniTavolo(int idTavolo);

    @Query("DELETE FROM tavolo WHERE idTavolo LIKE :idTavolo")
    int deleteById(int idTavolo);

}
