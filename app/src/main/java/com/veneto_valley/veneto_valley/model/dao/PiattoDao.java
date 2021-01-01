package com.veneto_valley.veneto_valley.model.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.relations.OrdiniPiatto;

import java.util.List;

@Dao
public interface PiattoDao extends baseDao<Piatto>{
    @Query("SELECT * FROM piatto")
    List<Piatto> getAll();

    @Query("SELECT * FROM piatto WHERE idPiatto IN (:idPiatti)")
    List<Piatto> loadAllByIds(int[] idPiatti);

    @Query("SELECT * FROM piatto WHERE nomePiatto LIKE :nomePiatto  LIMIT 1")
    Piatto findByName(String nomePiatto);

    @Query("DELETE FROM piatto WHERE idPiatto LIKE :idPiatto")
    int deleteById(int idPiatto);

    //Relazioni
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM piatto WHERE idPiatto IN (:idPiatto)")
    List<OrdiniPiatto> getOrdiniPiatto(int idPiatto);
}