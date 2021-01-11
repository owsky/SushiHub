package com.veneto_valley.veneto_valley.model;

import androidx.room.TypeConverter;

import java.util.Date;

public class TimestampConverter {
	@TypeConverter
	public static Date fromTimestamp(Long value) {
		return value == null ? null : new Date(value);
	}
	
	@TypeConverter
	public static Long dateToTimestamp(Date date) {
		return date == null ? null : date.getTime();
	}
}
