package com.veneto_valley.veneto_valley.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import com.veneto_valley.veneto_valley.db.dao.UtenteDao;
import com.veneto_valley.veneto_valley.db.entities.Utente;


@Database(entities = {Utente.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance = null;

    public abstract UtenteDao userDao();

    public static AppDatabase getInstance(Context ctx){
        if(dbInstance == null) dbInstance= Room.databaseBuilder(ctx,AppDatabase.class,"Veneto_Valley-Db").build();
        return dbInstance;
    };
}
