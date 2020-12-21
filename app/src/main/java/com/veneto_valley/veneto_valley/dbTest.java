package com.veneto_valley.veneto_valley;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.dao.PiattoDao;
import com.veneto_valley.veneto_valley.db.entities.Piatto;

import java.util.ArrayList;
import java.util.List;

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
        PiattoDao piattoDao = AppDatabase.getInstance(getApplicationContext()).piattoDao();
        piattoDao.insertAll(new Piatto());
        syncBtnPressed();
    }

    private void rmBtnPressed(){
        Log.i(TAG,"Remove Button Pressed");
        LinearLayout tmpLL = (LinearLayout) ll.getChildAt(0);
        TextView tmpTv = (TextView) tmpLL.getChildAt(1);
        PiattoDao piattoDao = AppDatabase.getInstance(getApplicationContext()).piattoDao();
        piattoDao.deleteById(Integer.parseInt((String) tmpTv.getText()));
        syncBtnPressed();
    }

    private void syncBtnPressed(){
        Log.i(TAG,"Sync Button Pressed");
        PiattoDao piattoDao = AppDatabase.getInstance(getApplicationContext()).piattoDao();
        List<Piatto> piattoArrayList = piattoDao.getAll();
        ll.removeAllViews();
        for (Piatto p : piattoArrayList){
            LinearLayout tmpLL = new LinearLayout(getApplicationContext());
            tmpLL.setOrientation(LinearLayout.HORIZONTAL);

            TextView tmp = new TextView(getApplicationContext());
            tmp.setText("id piatto:\t");
            tmp.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp);

            TextView tmp1 = new TextView(getApplicationContext());
            tmp1.setText(String.valueOf(p.getIdPiatto()));
            tmp1.setTextColor(Color.LTGRAY);
            tmpLL.addView(tmp1);

            ll.addView(tmpLL,0);
        }
    }
}