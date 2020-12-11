package com.veneto_valley.veneto_valley.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.dao.PiattoDao;
import com.veneto_valley.veneto_valley.db.dao.RistoranteDao;
import com.veneto_valley.veneto_valley.db.dao.TavoloDao;
import com.veneto_valley.veneto_valley.db.dao.UtenteDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Piatto;
import com.veneto_valley.veneto_valley.db.entities.Ristorante;
import com.veneto_valley.veneto_valley.db.entities.Tavolo;
import com.veneto_valley.veneto_valley.db.entities.Utente;
import com.veneto_valley.veneto_valley.db.relations.OrdiniPiattiCrossRef;
import com.veneto_valley.veneto_valley.db.relations.UtentiOrdiniCrossRef;


@Database(entities = {Utente.class, Ordine.class, Piatto.class, Ristorante.class, Tavolo.class, UtentiOrdiniCrossRef.class, OrdiniPiattiCrossRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance = null;

    public static AppDatabase getInstance(Context ctx){
        if(dbInstance == null) dbInstance= Room.databaseBuilder(ctx,AppDatabase.class,"Veneto_Valley-Db").allowMainThreadQueries().build();
        return dbInstance;
    };

    //Entità
    public abstract OrdineDao ordineDao();
    public abstract PiattoDao piattoDao();
    public abstract RistoranteDao ristoranteDao();
    public abstract TavoloDao tavoloDao();
    public abstract UtenteDao utenteDao();

}
