package com.veneto_valley.veneto_valley.model;

import androidx.room.TypeConverter;

import com.veneto_valley.veneto_valley.model.entities.Order;

public class StatusEnumConverter {
	@TypeConverter
	public static String fromStatusEnum(Order.OrderStatus value) {
		return value.toString();
	}
	
	@TypeConverter
	public static Order.OrderStatus toStatusEnum(String value) {
		return Order.OrderStatus.valueOf(value);
	}
}
