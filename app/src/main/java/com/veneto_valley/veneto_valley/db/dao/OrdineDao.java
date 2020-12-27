package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.relations.OrdiniPiatto;
import com.veneto_valley.veneto_valley.db.relations.UtentiOrdine;

import java.util.List;

@Dao
public interface OrdineDao extends baseDao<Ordine>{
    @Query("SELECT * FROM Ordine")
    List<Ordine> getAll();

    @Query("SELECT * FROM Ordine WHERE idOrdine = :idOrdine")
    public Ordine getOrdineById(long idOrdine);

    @Query("SELECT * FROM Ordine WHERE idOrdine IN (:idOrdini)")
    List<Ordine> loadAllByIds(long[] idOrdini);

    @Query("SELECT * FROM Ordine WHERE status LIKE :status")
    List<Ordine> getAllbyStatus(String status);

    @Update
    public void updateOrdini(Ordine... ordini);

    @Query("UPDATE Ordine SET status=:newStatus WHERE idOrdine = :idOrdine")
    void updateOrdineStatusByID(long idOrdine, String newStatus);

    //Relazioni
    /* FIXME: Devo rivedere la relazione
    @Transaction //Necessario per garantire atomicità dell'operazione
    @Query("SELECT * FROM Ordine WHERE idOrdine = :idOrdine")
    List<UtentiOrdine> getUtentiOrdine(long idOrdine);
    */

    @Query("DELETE FROM ordine WHERE idOrdine = :idOrdine AND status LIKE \"daOrdinare\"") //TODO: Inserire nome status corretto
    int deleteById(long idOrdine);
}
