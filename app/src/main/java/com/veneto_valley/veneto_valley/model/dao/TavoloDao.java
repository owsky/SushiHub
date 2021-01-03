package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.model.relations.OrdiniTavolo;

import java.util.List;

@Dao
public interface TavoloDao extends baseDao<Tavolo> {
	@Query("SELECT * FROM Tavolo WHERE idTavolo = :tavolo")
	Tavolo getTavolo(String tavolo);
	
	@Query("SELECT * FROM tavolo WHERE checkedOut IS 1")
	LiveData<List<Tavolo>> getAllButCurrent();
	
	@Query("SELECT costoMenu FROM tavolo WHERE idTavolo = :tavolo")
	float getCostoMenu(String tavolo);
	
	@Query("SELECT * FROM tavolo WHERE idTavolo = :idTavolo")
	Tavolo loadById(String idTavolo);
	
	//Relazioni
	@Transaction //Necessario per garantire atomicità dell'operazione
	@Query("SELECT * FROM tavolo WHERE idTavolo IN (:idTavolo)")
	List<OrdiniTavolo> getOrdiniTavolo(int idTavolo);
	
	@Query("DELETE FROM tavolo WHERE idTavolo LIKE :idTavolo")
	int deleteById(int idTavolo);
	
}
