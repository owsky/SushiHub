package com.veneto_valley.veneto_valley.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverters;

import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.dao.PiattoDao;
import com.veneto_valley.veneto_valley.db.dao.RistoranteDao;
import com.veneto_valley.veneto_valley.db.dao.TavoloDao;
import com.veneto_valley.veneto_valley.db.dao.UtenteDao;
import com.veneto_valley.veneto_valley.db.dao.UtentiOrdiniCrossRefDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Piatto;
import com.veneto_valley.veneto_valley.db.entities.Ristorante;
import com.veneto_valley.veneto_valley.db.entities.Tavolo;
import com.veneto_valley.veneto_valley.db.entities.Utente;
import com.veneto_valley.veneto_valley.db.relations.UtentiOrdiniCrossRef;


@Database(entities = {Utente.class, Ordine.class, Piatto.class, Ristorante.class, Tavolo.class, UtentiOrdiniCrossRef.class}, version = 1)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance = null;

    public static AppDatabase getInstance(Context ctx){
        if(dbInstance == null){
            //TODO: Rimuovere fallback
            dbInstance= Room.databaseBuilder(ctx,AppDatabase.class,"Veneto_Valley-Db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            //TODO: Rimuovere clear
            dbInstance.clearAllTables();
        }
        return dbInstance;
    }

    //Entità
    public abstract OrdineDao ordineDao();
    public abstract PiattoDao piattoDao();
    public abstract RistoranteDao ristoranteDao();
    public abstract TavoloDao tavoloDao();
    public abstract UtenteDao utenteDao();
    public abstract UtentiOrdiniCrossRefDao utentiOrdiniCrossRefDao();
}
