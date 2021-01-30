package com.veneto_valley.veneto_valley.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.veneto_valley.veneto_valley.model.dao.OrderDao;
import com.veneto_valley.veneto_valley.model.dao.TableDao;
import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.model.entities.Table;


@Database(entities = {Order.class, Table.class,}, exportSchema = false, version = 1)
@TypeConverters({TimestampConverter.class, StatusEnumConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance = null;

    public static synchronized AppDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Veneto_Valley-Db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }


    public abstract OrderDao orderDao();

    public abstract TableDao tableDao();

}
