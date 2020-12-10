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

import java.util.ArrayList;

public class Extra extends Fragment {

    private static final String TAG = "MyActivity";

    public Extra(){
        super(R.layout.activity_extra);
    }


    public static class MenuExtra{
        int numero;
        double prezzo;
        String nome;
        public boolean inserito=false;
        MenuExtra(int numero, double prezzo, String nome){
            this.numero=numero;
            this.prezzo=prezzo;
            this.nome=nome;
        }
    }

    Button addBevande, addDolci;
    ArrayList<MenuExtra> bevande;
    ArrayList<MenuExtra> dolci;
    Extra questo = this;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bevande = new ArrayList<>();
        dolci = new ArrayList<>();
        //due strutture per memorizzare bevande e dolci rispettivamente

        addBevande = (Button) getView().findViewById(R.id.button2); //prendo button2
        addDolci = (Button) getView().findViewById(R.id.button3); //prendo button3

        final Activity[] questa = {this.getActivity()};

        final CustomDialogClass[] cdd = new CustomDialogClass[1];


        //imposto i listener dei bottoni
        addBevande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd[0] =new CustomDialogClass(questa[0], "INSERISCI BEVANDE", questo);
                cdd[0].show();
            }
        });
        addDolci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd[0] =new CustomDialogClass(questa[0], "INSERISCI DOLCI", questo);
                cdd[0].show();
            }
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
        //TODO: FUNZIONE CHE AGGIORNA LA VIEW PRENDENDO I DATI DAI DUE ARRAYLIST
        LinearLayout bevandeLayout = (LinearLayout) getView().findViewById(R.id.layout_bevande);
        LinearLayout dolciLayout = (LinearLayout) getView().findViewById(R.id.layout_dolci);
        //a questi oggetti di tipo linear layout dovrò fare una add dei componenti
        //il metodo è addView
        int i=0,j=0;
        for(MenuExtra m : dolci){
            if(!m.inserito){
                dolciLayout.addView(faiCasellina(m.numero, m.prezzo, m.nome), i);
                m.inserito=true;
                i++;
            }
        }
        for(MenuExtra m : bevande){
            if(!m.inserito){
                bevandeLayout.addView(faiCasellina(m.numero, m.prezzo, m.nome), j);
                m.inserito=true;
                j++;
            }
        }
    }

}
