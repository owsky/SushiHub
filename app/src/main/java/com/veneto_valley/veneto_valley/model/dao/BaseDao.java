package com.veneto_valley.veneto_valley.model.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {
	@Insert
	void insert(T obj);
	
	@Update
	void update(T obj);
	
	@Delete
	void delete(T obj);
}