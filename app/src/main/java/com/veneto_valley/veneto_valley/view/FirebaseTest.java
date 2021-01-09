package com.veneto_valley.veneto_valley.view;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Categoria;
import com.veneto_valley.veneto_valley.model.entities.Piatto;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.util.RepositoryMenu;
import com.veneto_valley.veneto_valley.util.RepositoryRistorante;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FirebaseTest extends AppCompatActivity {

    ArrayList<Ristorante> ristorantiArrayList = new ArrayList<>();
    ArrayList<Categoria> categorieArrayList = new ArrayList<>();
    Button loadRes = null;
    Button loadCat = null;
    Spinner spinnerRes = null;
    Spinner spinnerCat = null;
    LinearLayout linLay = null;

    int resElemSelected = 0;
    int menElemSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        loadRes = (Button) findViewById(R.id.loadResBtn);
        loadRes.setEnabled(false);
        loadCat = (Button) findViewById(R.id.loadMenBtn);
        loadCat.setEnabled(false);

        linLay = (LinearLayout) findViewById(R.id.linLay);

        spinnerRes = (Spinner) findViewById(R.id.spinnerRes);

        //Creating the ArrayAdapter instance having the country list
        ristorantiArrayList.add(new Ristorante("","Nessun Ristorante Selezionato","",""));
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ristorantiArrayList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerRes.setAdapter(aa);

        spinnerCat = (Spinner) findViewById(R.id.spinnerMen);
        spinnerCat.setEnabled(false);


        //Creating the ArrayAdapter instance having the country list
        categorieArrayList.add(new Categoria("Nessuna Categoria Selezionata"));
        ArrayAdapter aaMenu = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categorieArrayList);
        aaMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCat.setAdapter(aaMenu);

        // Ottengo un istanza del db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Punto la reference al "Branch" del json che mi interessa
        DatabaseReference mDatabase = database.getReference("ristoranti");

        // Creo una repositoryRistorante
        RepositoryRistorante rr = new RepositoryRistorante(linLay,ristorantiArrayList);
        // Applico il listener della repo alla mia reference nel db
        mDatabase.addListenerForSingleValueEvent(rr.RistoranteFirebaseListener);

        spinnerRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ristorantiArrayList.get(position).toString(), Toast.LENGTH_LONG).show();
                Log.w("FBTest", ristorantiArrayList.get(position).toString());
                resElemSelected = position;
                if (position > 0) {
                    loadRes.setEnabled(true);
                    spinnerCat.setEnabled(true);
                } else {
                    loadRes.setEnabled(false);
                    spinnerCat.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), categorieArrayList.get(position).toString(), Toast.LENGTH_LONG).show();
                Log.w("FBTest", categorieArrayList.get(position).toString());
                menElemSelected = position;
                if (position > 0) {
                    loadCat.setEnabled(true);
                } else {
                    loadCat.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ((Button) findViewById(R.id.loadResBtn)).setOnClickListener( v -> {
            Log.w("FBTest","Loading menu for item " + resElemSelected + " (" + ristorantiArrayList.get(resElemSelected).idRistorante + ")");
            //TODO: Caricare menu
            DatabaseReference mMenu = database.getReference("menu").child(ristorantiArrayList.get(resElemSelected).idRistorante);
            RepositoryMenu rm = new RepositoryMenu(linLay,categorieArrayList);
            mMenu.addListenerForSingleValueEvent(rm.MenuFirebaseListener);
        });

        ((Button) findViewById(R.id.loadMenBtn)).setOnClickListener(v -> {
            Log.w("FBTest","Loading category for item " + menElemSelected + " (" + categorieArrayList.get(menElemSelected).nomeCategoria + ")");
            linLay.removeAllViews();
            for (Piatto p: categorieArrayList.get(menElemSelected).piatti){
                TextView tv = new TextView(linLay.getContext());
                tv.setText(p.toString());
                linLay.addView(tv);
            }
        });

    }
}