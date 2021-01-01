package com.veneto_valley.veneto_valley.model.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.model.relations.TavoliRistorante;

import java.util.List;

@Dao
public interface RistoranteDao extends baseDao<Ristorante> {
	@Query("SELECT * FROM ristorante")
	List<Ristorante> getAll();
	
	@Query("SELECT * FROM ristorante WHERE idRistorante IN (:idRistoranti)")
	List<Ristorante> loadAllByIds(int[] idRistoranti);
	
	@Query("SELECT * FROM ristorante WHERE nome LIKE :nome LIMIT 1")
	Ristorante findByName(String nome);
	
	//Relazioni
	@Transaction //Necessario per garantire atomicità dell'operazione
	@Query("SELECT * FROM ristorante WHERE idRistorante IN (:idRistorante)")
	List<TavoliRistorante> getTavoliRistorante(int idRistorante);
}
