package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.util.RepositoryMenu;
import com.veneto_valley.veneto_valley.util.RepositoryRistorante;
import com.veneto_valley.veneto_valley.view.ListaRistorantiAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private RepositoryRistorante repositoryRistorante = null;
    private RepositoryMenu repositoryMenu = null;
    
    public MenuViewModel(@NonNull Application application) {
        super(application);
    }
    public List<Ristorante> getRistoranti(ListaRistorantiAdapter adapter) {
        if (repositoryRistorante == null)
            repositoryRistorante = new RepositoryRistorante(adapter);
        return repositoryRistorante.getRistoranti();
    }
    public List<Categoria> getCategoria(){
        if (repositoryMenu == null)
            repositoryMenu = new RepositoryMenu();
        return repositoryMenu.getCategoria();
    }
}
