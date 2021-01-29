package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.model.entities.Table;

import java.util.List;

@Dao
public interface TableDao extends BaseDao<Table> {
	@Query("SELECT * FROM `Table` WHERE id = :tavolo")
	Table getTable(String tavolo);
	
	@Query("SELECT * FROM `Table` WHERE checkedOut")
	LiveData<List<Table>> getAllButCurrent();
	
	@Query("SELECT menuPrice FROM `Table` WHERE id = :tavolo")
	float getMenuPrice(String tavolo);
	
	@Query("DELETE FROM `Table`")
	void deleteAllTables();
}
