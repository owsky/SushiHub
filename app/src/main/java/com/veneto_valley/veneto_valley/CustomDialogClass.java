package com.veneto_valley.veneto_valley;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialogClass extends Dialog{

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public TextView label;
    public String titolo;
    public Extra istanza;


    public CustomDialogClass(Activity a, String titolo, Extra istanza) {
        super(a);
        this.titolo = titolo;
        this.istanza=istanza;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        label= (TextView) findViewById(R.id.txt_dia);
        label.setText(titolo);
        yes = (Button) findViewById(R.id.annulla);
        no = (Button) findViewById(R.id.conferma);
        //
        yes.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            dismiss();
        }
        });
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int numero = Integer.parseInt(((EditText)findViewById(R.id.textView9)).getText().toString());
                double prezzo = Double.parseDouble(((EditText)findViewById(R.id.textView10)).getText().toString());
                String nome = ((EditText)findViewById(R.id.textView6)).getText().toString();
                if(titolo.equals("INSERISCI BEVANDE")){
                    istanza.bevande.add(new Extra.MenuExtra(numero,prezzo,nome));
                }else{
                    istanza.dolci.add(new Extra.MenuExtra(numero,prezzo,nome));
                }
                istanza.aggiungiRiga();
                dismiss();
            }
        });
    }
}
