package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.model.relations.OrdiniTavolo;

import java.util.List;

@Dao
public interface TavoloDao extends baseDao<Tavolo>{
    @Query("SELECT * FROM tavolo")
    LiveData<List<Tavolo>> getAll();
    
    @Query("SELECT * FROM tavolo WHERE idTavolo <> :tavolo")
    LiveData<List<Tavolo>> getAllMinusCurr(String tavolo);
    
    @Query("SELECT costoMenu FROM tavolo WHERE idTavolo = :tavolo")
    float getCostoMenu(String tavolo);

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
