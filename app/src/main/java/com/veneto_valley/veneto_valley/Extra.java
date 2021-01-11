package com.veneto_valley.veneto_valley;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import java.util.ArrayList;
import java.util.Objects;

public class Extra extends Fragment {
    private static final String TAG = "MyActivity";
    Fragment questo = this;
    public Extra(){
        super(R.layout.activity_extra);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aggiungiRiga();
        view.findViewById(R.id.button2).setOnClickListener(view11 -> {
            NavHostFragment.findNavController(questo).navigate(R.id.action_extra_to_aggiungiExtra);
        });
    }

    @SuppressLint("SetTextI18n")
    private LinearLayout faiCasellina(int numero, Double prezzo, String nome){
        LinearLayout layoutCasellina = new LinearLayout(this.getContext());
        layoutCasellina.setOrientation(LinearLayout.HORIZONTAL);
        //abbiamo fatto linear layout orizzontale
        //ora facciamo le tre caselline
        TextView casellina1 = new TextView(this.getContext());
        TextView casellina2 = new TextView(this.getContext());
        TextView casellina3 = new TextView(this.getContext());
        casellina1.setWidth(500);
        casellina2.setWidth(200);
        casellina3.setWidth(250);
        casellina1.setHeight(70);
        casellina2.setHeight(70);
        casellina3.setHeight(70);
        casellina1.setTextSize(20);
        casellina2.setTextSize(20);
        casellina3.setTextSize(20);
        casellina1.setText(nome);
        casellina2.setText(numero + " qt");
        casellina3.setText(prezzo + "€");
        casellina1.setBackgroundColor(Color.LTGRAY);
        casellina2.setBackgroundColor(Color.LTGRAY);
        casellina3.setBackgroundColor(Color.LTGRAY);
        layoutCasellina.addView(casellina1, 0);
        layoutCasellina.addView(casellina2, 1);
        layoutCasellina.addView(casellina3, 2);
        return layoutCasellina;

    }


    @SuppressLint("SetTextI18n")
    public void aggiungiRiga(){
        LinearLayout bevandeLayout = (LinearLayout) getView().findViewById(R.id.layout_bevande);
        //a questi oggetti di tipo linear layout dovrò fare una add dei componenti
        //il metodo è addView
        MenuExtra m = MenuExtra.getInstance();
        int j=0;
        if(!m.listaExtra.isEmpty()){
            for(MenuExtra.ExtraDish me : m.listaExtra){
                bevandeLayout.addView(faiCasellina(me.numero, me.prezzo, me.nome), j);
                j++;

            }
        }

    }






}
