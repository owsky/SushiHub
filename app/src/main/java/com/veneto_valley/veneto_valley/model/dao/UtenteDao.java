package com.veneto_valley.veneto_valley.model.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.model.entities.Utente;
import com.veneto_valley.veneto_valley.model.relations.OrdiniUtente;

import java.util.List;

@Dao
public interface UtenteDao extends baseDao<Utente> {
	@Query("SELECT * FROM Utente")
	List<Utente> getAll();
	
	@Query("SELECT * FROM Utente WHERE idUtente = :idUtente")
	Utente loadById(String idUtente);
	
	//Relazioni
	@Transaction //Necessario per garantire atomicità dell'operazione
	@Query("SELECT * FROM utente WHERE idUtente IN (:idUtente)")
	List<OrdiniUtente> getOrdiniUtente(String idUtente);
	
	@Query("DELETE FROM utente WHERE idUtente != :idUtente")
	int deleteNotUser(String idUtente);
}

