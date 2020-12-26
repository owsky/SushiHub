package com.veneto_valley.veneto_valley;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AggiungiExtra extends Fragment {
    Button yes, no;
    Fragment questo = this;
    public AggiungiExtra(){
        super(R.layout.aggiungi_extra_fragment);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yes = (Button) getView().findViewById(R.id.annulla2);
        no = (Button) getView().findViewById(R.id.conferma2);
        //
        view.findViewById(R.id.conferma2).setOnClickListener(view12 -> {
            NavHostFragment.findNavController(questo).navigateUp();
        });
        no.setOnClickListener(view12 -> {
            int numero = Integer.parseInt(((EditText)getView().findViewById(R.id.textView2)).getText().toString());
            double prezzo = Double.parseDouble(((EditText)getView().findViewById(R.id.textView3)).getText().toString());
            String nome = ((EditText)getView().findViewById(R.id.textView17)).getText().toString();
            MenuExtra m = MenuExtra.getInstance();
            m.listaExtra.add(new MenuExtra.ExtraDish(numero, prezzo, nome));
            NavHostFragment.findNavController(questo).navigateUp();
        });
    }
}