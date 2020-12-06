package com.veneto_valley.veneto_valley;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Extra extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        bevande = new ArrayList<>();
        dolci = new ArrayList<>();
        //due strutture per memorizzare bevande e dolci rispettivamente

        addBevande = (Button) findViewById(R.id.button2); //prendo button2
        addDolci = (Button)findViewById(R.id.button3); //prendo button3

        final Activity[] questa = {this};

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
    public void aggiungiRiga(){
        //TODO: FUNZIONE CHE AGGIORNA LA VIEW PRENDENDO I DATI DAI DUE ARRAYLIST
        LinearLayout bevandeLayout = (LinearLayout) findViewById(R.id.layout_bevande);
        LinearLayout dolciLayout = (LinearLayout) findViewById(R.id.layout_dolci);
        //a questi oggetti di tipo linear layout dovrò fare una add dei componenti
        //il metodo è addView
        int i=0,j=0;
        for(MenuExtra m : dolci){
            if(!m.inserito){
                //devo inserirlo e cambiare il flag
                //TODO: TOGLIERE LA CASELLINA E FARE UNA GRID VIEW
                TextView casellina = new TextView(this);
                casellina.setWidth(301);
                casellina.setHeight(70);
                casellina.setTextSize(20);
                casellina.setText(m.nome + "\t\t\t\t\t" + m.numero + "qta\t\t" + m.prezzo+"€");
                dolciLayout.addView(casellina,i);
                m.inserito=true;
                i++;
            }
        }
        for(MenuExtra m : bevande){
            if(!m.inserito){
                //devo inserirlo e cambiare il flag
                TextView casellina = new TextView(this);
                casellina.setWidth(301);
                casellina.setHeight(60);
                casellina.setText(m.nome + " " + m.numero + " " + m.prezzo);
                bevandeLayout.addView(casellina,j);
                m.inserito=true;
                j++;
            }
        }
    }

}
