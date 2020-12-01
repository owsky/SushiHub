package com.veneto_valley.veneto_valley;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
        // TODO Auto-generated constructor stub
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

        yes.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            dismiss();
        }
        });
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                istanza.aggiungiRiga(label.getText().equals("INSERISCI DOLCI"));
                dismiss();
            }
        });




    }



    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.annulla:
                titolo="";
                dismiss();
                break;
            case R.id.conferma:
                istanza.aggiungiRiga(titolo.equals("INSERISCI DOLCE"));
                titolo="";
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
    */
}
