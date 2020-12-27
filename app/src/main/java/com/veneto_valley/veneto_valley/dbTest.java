package com.veneto_valley.veneto_valley;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.OrdineDao;
import com.veneto_valley.veneto_valley.db.dao.PiattoDao;
import com.veneto_valley.veneto_valley.db.dao.TavoloDao;
import com.veneto_valley.veneto_valley.db.entities.Ordine;
import com.veneto_valley.veneto_valley.db.entities.Piatto;
import com.veneto_valley.veneto_valley.db.entities.Tavolo;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class dbTest extends AppCompatActivity {
    private static final String TAG = "dbTestLog";
    private TextView nElements;
    private TextView tableName;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

        nElements = (TextView) findViewById(R.id.nElementi);
        tableName = (TextView) findViewById(R.id.nomeTabella);
        ll = (LinearLayout) findViewById(R.id.linLay);


        findViewById(R.id.addBtn).setOnClickListener(v -> {
            addBtnPressed();
        });

        findViewById(R.id.rmBtn).setOnClickListener(v -> {
            rmBtnPressed();
        });

        findViewById(R.id.syncBtn).setOnClickListener(v -> {
            syncBtnPressed();
        });
    }

    private void addBtnPressed(){
        Log.i(TAG,"Add Button Pressed");
        OrdineDao ordineDao = AppDatabase.getInstance(getApplicationContext()).ordineDao();
        Ordine o = new Ordine("a","a12");
        o.status = "daOrdinare";
        ordineDao.insertAll(o);
        syncBtnPressed();
    }

    private void rmBtnPressed(){
        Log.i(TAG,"Remove Button Pressed");
        LinearLayout tmpLL = (LinearLayout) ll.getChildAt(0);
        TextView tmpTv = (TextView) tmpLL.getChildAt(1);
        OrdineDao ordineDao = AppDatabase.getInstance(getApplicationContext()).ordineDao();
        Log.i(TAG,String.valueOf(ordineDao.deleteById(Integer.parseInt((String) tmpTv.getText()))));
        syncBtnPressed();
    }

    private void syncBtnPressed(){
        Log.i(TAG,"Sync Button Pressed");
        OrdineDao ordineDao = AppDatabase.getInstance(getApplicationContext()).ordineDao();
        List<Ordine> tavoloList = ordineDao.getAll();
        ll.removeAllViews();
        for (Ordine p : tavoloList){
            LinearLayout tmpLL = new LinearLayout(getApplicationContext());
            tmpLL.setOrientation(LinearLayout.HORIZONTAL);

            TextView tmp = new TextView(getApplicationContext());
            tmp.setText("id:\t");
            tmp.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp);

            TextView tmp1 = new TextView(getApplicationContext());
            tmp1.setText(String.valueOf(p.idOrdine));
            tmp1.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp1);

            TextView tmp2 = new TextView(getApplicationContext());
            tmp2.setText("Status:\t");
            tmp2.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp2);

            TextView tmp3 = new TextView(getApplicationContext());
            tmp3.setText(String.valueOf(p.status));
            tmp3.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp3);

            ll.addView(tmpLL,0);
        }
    }
}