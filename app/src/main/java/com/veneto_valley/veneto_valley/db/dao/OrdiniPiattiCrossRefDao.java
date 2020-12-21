package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.veneto_valley.veneto_valley.db.relations.OrdiniPiattiCrossRef;

@Dao
public interface OrdiniPiattiCrossRefDao {

    @Insert
    void insertAll(OrdiniPiattiCrossRef... ordiniPiattiCrossRefs);

    @Delete
    void delete(OrdiniPiattiCrossRef ordiniPiattiCrossRef);
}
