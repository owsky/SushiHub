package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.model.entities.Table;

import java.util.List;

@Dao
public interface TableDao extends BaseDao<Table> {
    @Query("SELECT * FROM `Table` WHERE id = :table")
    Table getTable(String table);

    @Query("SELECT * FROM `Table` WHERE checkedOut")
    LiveData<List<Table>> getAllButCurrent();

    @Query("SELECT menuPrice FROM `Table` WHERE id = :table")
    float getMenuPrice(String table);

    @Query("DELETE FROM `Table`")
    void deleteAllTables();
}
