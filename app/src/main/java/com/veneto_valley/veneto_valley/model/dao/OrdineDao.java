package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

import java.util.List;

@Dao
public interface OrdineDao extends baseDao<Ordine> {
	@Query("SELECT * FROM Ordine")
	LiveData<List<Ordine>> getAll();
	
	@Query("SELECT * FROM Ordine WHERE utente = :user AND tavolo = :tavolo")
	LiveData<List<Ordine>> getAllByUser(long user, String tavolo);
	
	@Query("SELECT * FROM Ordine WHERE tavolo = :codiceTavolo")
	LiveData<List<Ordine>> getAllByTable(String codiceTavolo);
	
	@Query("SELECT SUM(prezzo) FROM Ordine WHERE tavolo = :tavolo")
	float getTotaleExtra(String tavolo);
	
	@Query("SELECT * FROM Ordine WHERE tavolo = :tavolo AND prezzo > 0")
	LiveData<List<Ordine>> getAllExtra(String tavolo);
	
	@Query("SELECT * FROM Ordine WHERE status LIKE :status AND tavolo = :tavolo")
	LiveData<List<Ordine>> getAllbyStatus(String status, String tavolo);
	
	@Query("SELECT * FROM Ordine WHERE status LIKE :status AND tavolo = :tavolo AND piatto = :piatto")
	Ordine getOrdineByPiatto(String status, String tavolo, String piatto);
	
	@Query("DELETE FROM Ordine WHERE tavolo = :tavolo")
	void deleteByTable(String tavolo);
	
	@Query("DELETE FROM utente WHERE idUtente != :idUtente")
	void deleteNotUser(String idUtente);
}
