package com.veneto_valley.veneto_valley.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.veneto_valley.veneto_valley.model.entities.Order;
import com.veneto_valley.veneto_valley.model.entities.Table;
import com.veneto_valley.veneto_valley.util.RepositoryTables;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private final RepositoryTables repositoryTables;
    private LiveData<List<Table>> tables = null;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repositoryTables = new RepositoryTables(application);
    }

    public LiveData<List<Table>> getTables() {
        if (tables == null)
            tables = repositoryTables.getTablesHistory();
        return tables;
    }

    public LiveData<List<Order>> getOrders(Table table) {
        return repositoryTables.getOrdersHistory(table);
    }

    public void deleteTable(Table table) {
        repositoryTables.deleteTable(table);
    }

    public void deleteAllTables() {
        repositoryTables.deleteAllTables();
    }
}
