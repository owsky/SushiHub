package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.db.entities.Ristorante;
import com.veneto_valley.veneto_valley.db.entities.Utente;

import java.util.List;

@Dao
public interface RistoranteDao {
    @Query("SELECT * FROM ristorante")
    List<Ristorante> getAll();

    @Query("SELECT * FROM ristorante WHERE idRistorante IN (:idRistoranti)")
    List<Utente> loadAllByIds(int[] idRistoranti);

    @Query("SELECT * FROM ristorante WHERE nome LIKE :nome LIMIT 1")
    Utente findByName(String nome);

    @Insert
    void insertAll(Ristorante... ristoranti);

    @Delete
    void delete(Ristorante ristorante);
}
