package com.veneto_valley.veneto_valley;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NearbyTest extends Fragment {

    Button host, client, invia;
    EditText txt, service;
    Activity a = this.getActivity();
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;


    public NearbyTest() {
        super(R.layout.fragment_nearby_test);

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasPermissions(this.getContext(), getRequiredPermissions())) {
            if (!hasPermissions(this.getContext(), getRequiredPermissions())) {
                if (Build.VERSION.SDK_INT < 23) {
                    ActivityCompat.requestPermissions(
                            a, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                } else {
                    requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                }
            }
        }
        final Connessione[] c = new Connessione[1];

        //LA RICEZIONE SONO FATTI DELLA CLASSE CONNESSIONE
        host = (Button)getView().findViewById(R.id.host);
        client = (Button)getView().findViewById(R.id.client);
        txt = (EditText)getView().findViewById(R.id.nome);
        service = (EditText)getView().findViewById(R.id.service);
        invia = (Button)getView().findViewById(R.id.invia);
        NearbyTest questo = this;
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SERVICE_ID=service.getText().toString();
                c[0] = new Connessione(false,questo, SERVICE_ID);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SERVICE_ID=service.getText().toString();
                c[0] = new Connessione(true,questo, SERVICE_ID);
            }
        });
        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] testo = txt.getText().toString().getBytes();
                c[0].invia(testo);
            }
        });
    }





}