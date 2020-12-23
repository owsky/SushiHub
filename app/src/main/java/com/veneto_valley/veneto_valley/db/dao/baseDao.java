package com.veneto_valley.veneto_valley.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.veneto_valley.veneto_valley.db.entities.Piatto;

public interface baseDao<T> {

        @Insert
        void insertAll(T... objs);

        @Update
        void update(T obj);

        @Delete
        void delete(T obj);


    }
