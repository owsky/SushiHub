package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.List;

@Dao
public interface OrdineDao extends baseDao<Ordine>{
    @Query("SELECT * FROM Ordine")
    LiveData<List<Ordine>> getAll();
    
    @Query("SELECT * FROM Ordine WHERE utente = :user AND tavolo = :tavolo")
    LiveData<List<Ordine>> getAllByUser(long user, String tavolo);
    
    @Query("SELECT * FROM Ordine WHERE tavolo = :codiceTavolo")
    LiveData<List<Ordine>> getAllByTable(String codiceTavolo);

    @Query("SELECT * FROM Ordine WHERE idOrdine = :idOrdine")
    Ordine getOrdineById(long idOrdine);

    @Query("SELECT * FROM Ordine WHERE idOrdine IN (:idOrdini)")
    List<Ordine> loadAllByIds(long[] idOrdini);

    @Query("SELECT * FROM Ordine WHERE status LIKE :status AND tavolo = :tavolo")
    LiveData<List<Ordine>> getAllbyStatus(String status, String tavolo);
    
    @Query("SELECT * FROM Ordine WHERE status LIKE :status AND tavolo = :tavolo AND piatto = :piatto")
    Ordine getOrdineByPiatto(String status, String tavolo, String piatto);

    @Update
    void updateOrdini(Ordine... ordini);

    @Query("UPDATE Ordine SET status=:newStatus WHERE idOrdine = :idOrdine")
    void updateOrdineStatusByID(long idOrdine, String newStatus);

    @Query("DELETE FROM ordine WHERE idOrdine = :idOrdine AND status LIKE \"pending\"") //TODO: Inserire nome status corretto
    int deleteById(long idOrdine);
    
    @Query("DELETE FROM Ordine WHERE tavolo = :tavolo")
    void deleteByTable(String tavolo);

    @Query("DELETE FROM utente WHERE idUtente != :idUtente")
    void deleteNotUser(String idUtente);
}
