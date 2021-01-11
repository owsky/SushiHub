package com.veneto_valley.veneto_valley.model;

import androidx.room.TypeConverter;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

public class StatusEnumConverter {
	@TypeConverter
	public static String fromStatusEnum(Ordine.StatusOrdine value) {
		return value.toString();
	}
	
	@TypeConverter
	public static Ordine.StatusOrdine toStatusEnum(String value) {
		return Ordine.StatusOrdine.valueOf(value);
	}
}
