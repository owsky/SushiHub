package com.veneto_valley.veneto_valley.model.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

public interface baseDao<T> {
        @Insert
        List<Long> insertAll(T... objs);

        @Update
        void update(T obj);

        @Delete
        int delete(T obj);


    }
