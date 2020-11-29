package com.veneto_valley.veneto_valley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Extra extends AppCompatActivity {

    private static class MenuExtra{
        int numero;
        double prezzo;
        String nome;
        MenuExtra(int numero, double prezzo, String nome){
            this.numero=numero;
            this.prezzo=prezzo;
            this.nome=nome;
        }
    }

    Button addBevande, addDolci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        ArrayList<MenuExtra> bevande = new ArrayList<>();
        ArrayList<MenuExtra> dolci = new ArrayList<>();
        //due strutture per memorizzare bevande e dolci rispettivamente

        addBevande = (Button) findViewById(R.id.button2); //prendo button2
        addDolci = (Button)findViewById(R.id.button3); //prendo button3

        CustomDialogClass cdd=new CustomDialogClass(this);


        //imposto i listener dei bottoni
        addBevande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Ciao sono il bottone 1", Toast.LENGTH_LONG).show();//display the text of button1
                cdd.titolo= "INSERISCI BEVANDE";
                cdd.show();
            }
        });
        addDolci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.titolo= "INSERISCI DOLCI";
                //Toast.makeText(getApplicationContext(), "Ciao sono il bottone 2", Toast.LENGTH_LONG).show();//display the text of button1
                cdd.show();
            }
        });
    }

}
