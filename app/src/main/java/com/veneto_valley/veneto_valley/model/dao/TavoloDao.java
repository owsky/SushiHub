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
	
	@Query("SELECT * FROM tavolo WHERE checkedOut")
	LiveData<List<Tavolo>> getAllButCurrent();
	
	@Query("SELECT costoMenu FROM tavolo WHERE idTavolo = :tavolo")
	float getCostoMenu(String tavolo);
	
	@Query("DELETE FROM tavolo")
	void deleteAllTables();
}
