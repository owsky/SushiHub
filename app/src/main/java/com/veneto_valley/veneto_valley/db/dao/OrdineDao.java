package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.veneto_valley.veneto_valley.db.entities.Ordine;

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

    @Query("DELETE FROM ordine WHERE idOrdine = :idOrdine AND status LIKE \"daOrdinare\"") //TODO: Inserire nome status corretto
    int deleteById(long idOrdine);
}
