package com.veneto_valley.veneto_valley.model;

import androidx.room.TypeConverter;

import com.veneto_valley.veneto_valley.model.entities.Ordine;

public class StatusEnumConverter {
    @TypeConverter
    public static String fromStatusEnum(Ordine.statusOrdine value) {
        return value.toString();
    }

    @TypeConverter
    public static Ordine.statusOrdine toStatusEnum(String value) {
        return Ordine.statusOrdine.valueOf(value);
    }
}
