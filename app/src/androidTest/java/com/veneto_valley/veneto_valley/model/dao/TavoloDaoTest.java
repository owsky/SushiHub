package com.veneto_valley.veneto_valley.model.dao;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.veneto_valley.veneto_valley.model.AppDatabase;
import com.veneto_valley.veneto_valley.model.TestUtil;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TavoloDaoTest {
    private TavoloDao tavoloDao;
    private AppDatabase db;
    ArrayList<Tavolo> tavoloArrayList;
    Random rand = new Random();

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        tavoloDao = db.tavoloDao();
        tavoloArrayList = TestUtil.creaTavoli(rand.nextInt(15)+5);
    }

    @After
    public void tearDown() throws Exception {
        db.clearAllTables();
        db.close();
    }

    @Test
    public void getAll() {
        insertAll();
        List<Tavolo> tavoloList = tavoloDao.getAll();
        Assert.assertArrayEquals(tavoloArrayList.toArray(),tavoloList.toArray());
    }

    @Test
    public void loadById() {
        insertAll();
        int idTavolo = rand.nextInt(tavoloArrayList.size());
        Tavolo t = tavoloDao.loadById(tavoloArrayList.get(idTavolo).idTavolo);
        Assert.assertEquals(tavoloArrayList.get(idTavolo),t);
    }

    @Test
    public void loadAllByIds() {
    }

    @Test
    public void insertAll() {
        tavoloDao.insertAll(tavoloArrayList.toArray(new Tavolo[0]));
    }

    @Test
    public void deleteById() {
    }
}