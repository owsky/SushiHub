package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.util.RepositoryMenu;
import com.veneto_valley.veneto_valley.util.RepositoryRistorante;

import java.util.ArrayList;

public class MenuViewModel extends AndroidViewModel {
    RepositoryRistorante r;
    RepositoryMenu m;
    public MenuViewModel(@NonNull Application application, LinearLayout linlay) {
        super(application);
        r = new RepositoryRistorante(linlay);
        m = new RepositoryMenu(linlay);
    }
    public ArrayList<Ristorante> getRistoranti() {
        return r.getRistoranti();
    }
    public ArrayList<Categoria> getCategoria(){
        return m.getCategoria();
    }
    public RepositoryRistorante getRepoRistorante(){
        return r;
    }
    public RepositoryMenu getRepoMenu(){
        return m;
    }
}
