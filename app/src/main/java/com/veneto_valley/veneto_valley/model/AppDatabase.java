package com.veneto_valley.veneto_valley.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.dao.PiattoDao;
import com.veneto_valley.veneto_valley.model.dao.RistoranteDao;
import com.veneto_valley.veneto_valley.model.dao.TavoloDao;
import com.veneto_valley.veneto_valley.model.dao.UtenteDao;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.model.entities.Utente;


@Database(entities = {Utente.class, Ordine.class, Piatto.class, Ristorante.class, Tavolo.class,}, exportSchema = false, version = 2)
@TypeConverters({TimestampConverter.class, StatusEnumConverter.class})
public abstract class AppDatabase extends RoomDatabase {
	private static AppDatabase dbInstance = null;
	
	public static synchronized AppDatabase getInstance(Context context) {
		if (dbInstance == null) {
			//TODO: Rimuovere fallback
			dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Veneto_Valley-Db")
					.allowMainThreadQueries()
					.fallbackToDestructiveMigration()
					.build();
			//TODO: Rimuovere clear
//            dbInstance.clearAllTables();
		}
		return dbInstance;
	}
	
	//Entità
	public abstract OrdineDao ordineDao();
	
	public abstract PiattoDao piattoDao();
	
	public abstract RistoranteDao ristoranteDao();
	
	public abstract TavoloDao tavoloDao();
	
	public abstract UtenteDao utenteDao();
}
