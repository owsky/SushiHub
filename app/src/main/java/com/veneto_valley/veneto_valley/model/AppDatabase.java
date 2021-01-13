package com.veneto_valley.veneto_valley.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.dao.PiattoDao;
import com.veneto_valley.veneto_valley.model.dao.TavoloDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;


@Database(entities = {Ordine.class, Piatto.class, Tavolo.class,}, exportSchema = false, version = 2)
@TypeConverters({TimestampConverter.class, StatusEnumConverter.class})
public abstract class AppDatabase extends RoomDatabase {
	private static AppDatabase dbInstance = null;
	
	public static synchronized AppDatabase getInstance(Context context) {
		if (dbInstance == null) {
			//TODO: Rimuovere fallback
			dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Veneto_Valley-Db")
					.allowMainThreadQueries() //TODO: Provare a rimuovere
					.fallbackToDestructiveMigration()  //TODO: Rimuovere
					.build();
		}
		return dbInstance;
	}
	
	//Entità
	public abstract OrdineDao ordineDao();
	
	public abstract PiattoDao piattoDao();
	
	public abstract TavoloDao tavoloDao();
	
}
