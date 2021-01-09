package com.veneto_valley.veneto_valley.view;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Ristorante;
import com.veneto_valley.veneto_valley.model.entities.RistoranteOld;
import com.veneto_valley.veneto_valley.util.RepositoryRistorante;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FirebaseTest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> arr = new ArrayList<>();
    Button loadRes = null;
    int elemSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        loadRes = (Button) findViewById(R.id.loadResBtn);
        loadRes.setEnabled(false);

        Spinner s = (Spinner) findViewById(R.id.spinnerRes);
        s.setOnItemSelectedListener(this);
        arr.add("Nessun ristorante selezionato");

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        s.setAdapter(aa);

        // Ottengo un istanza del db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Punto la reference al "Branch" del json che mi interessa
        DatabaseReference mDatabase = database.getReference("ristoranti");

        // Creo una repositoryRistorante
        RepositoryRistorante rr = new RepositoryRistorante((LinearLayout) findViewById(R.id.linLay),arr);
        // Applico il listener della repo alla mia reference nel db
        mDatabase.addListenerForSingleValueEvent(rr.RistoranteFirebaseListener);

        ((Button) findViewById(R.id.loadResBtn)).setOnClickListener( v -> {
            Log.w("FBTest","Loading menu for item " + elemSelected + " (" + arr.get(elemSelected) + ")");
            //TODO: Caricare menu
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),arr.get(position) , Toast.LENGTH_LONG).show();
        Log.w("FBTest",arr.get(position));
        elemSelected = position;
        if (position > 0) {
            loadRes.setEnabled(true);
        } else {
            loadRes.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}