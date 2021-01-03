package com.veneto_valley.veneto_valley.model.entities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.dao.OrdineDao;
import com.veneto_valley.veneto_valley.model.dao.UtenteDao;


@Entity
public class Utente {
	@PrimaryKey(autoGenerate = true)
	public int idUtente;
	@NonNull
	public String username;
	
	public Utente(@NonNull String username) {
		this.username = username;
	}
	
	public static Utente getUser(@NonNull String idUtente, Application application) {
		UtenteDao utenteDao = AppDatabase.getInstance(application).utenteDao();
		Utente u = utenteDao.getUtente(idUtente);
		if (u == null) {
			Utente tmp = new Utente(idUtente);
			utenteDao.insert(tmp);
			u = utenteDao.getUtente(idUtente);
		}
		return u;
	}
	
	public static void deleteAllButCurrentUser(Application application) {
		UtenteDao utenteDao = AppDatabase.getInstance(application).utenteDao();
		OrdineDao ordineDao = AppDatabase.getInstance(application).ordineDao();
		
	}
}
