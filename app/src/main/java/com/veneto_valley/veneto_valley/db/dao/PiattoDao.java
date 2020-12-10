package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.db.entities.Piatto;
import com.veneto_valley.veneto_valley.db.entities.Utente;

import java.util.List;

@Dao
public interface PiattoDao {
    @Query("SELECT * FROM piatto")
    List<Piatto> getAll();

    @Query("SELECT * FROM piatto WHERE idPiatto IN (:idPiatti)")
    List<Piatto> loadAllByIds(int[] idPiatti);

    @Query("SELECT * FROM piatto WHERE nomePiatto LIKE :nomePiatto  LIMIT 1")
    Utente findByName(String nomePiatto);

    @Insert
    void insertAll(Piatto... piatti);

    @Delete
    void delete(Piatto piatto);
}