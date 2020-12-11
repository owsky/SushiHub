package com.veneto_valley.veneto_valley;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NearbyTest extends Fragment {

    Button host, client;
    EditText txt;


    public NearbyTest() {
        super(R.layout.fragment_nearby_test);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Connessione[] c = new Connessione[1];
        host = (Button)getView().findViewById(R.id.host);
        client = (Button)getView().findViewById(R.id.client);
        txt = (EditText)getView().findViewById(R.id.nome);
        String testo = txt.getText().toString();
        NearbyTest questo = this;
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c[0] = new Connessione(false,true,questo);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c[0] = new Connessione(true,false,questo);
            }
        });

    }



}