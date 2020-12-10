package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.db.entities.Utente;
import com.veneto_valley.veneto_valley.db.relations.OrdiniUtente;

import java.util.List;

@Dao
public interface UtenteDao {
    @Query("SELECT * FROM Utente")
    List<Utente> getAll();

    @Query("SELECT * FROM Utente WHERE idUtente IN (:idUtenti)")
    List<Utente> loadAllByIds(int[] idUtenti);

    @Query("SELECT * FROM Utente WHERE username LIKE :username LIMIT 1")
    Utente findByName(String username);

    @Insert
    void insertAll(Utente... utenti);

    @Delete
    void delete(Utente utente);

    //Relazioni
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM utente WHERE idUtente IN (:idUtente)")
    List<OrdiniUtente> getOrdiniUtente(int idUtente);
}
