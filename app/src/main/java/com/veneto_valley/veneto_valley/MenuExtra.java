package com.veneto_valley.veneto_valley;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuExtra {
    public ArrayList<ExtraDish> listaExtra;

    private static MenuExtra single_instance = null;
    private MenuExtra(){
        listaExtra = new ArrayList<>();
    }

    public static MenuExtra getInstance()
    {
        if (single_instance == null)
            single_instance = new MenuExtra();

        return single_instance;
    }
    public static  class ExtraDish{
        int numero;
        double prezzo;
        String nome;
        public boolean inserito=false;
        public ExtraDish(int numero, double prezzo, String nome) {
            this.numero = numero;
            this.prezzo = prezzo;
            this.nome = nome;
        }
    }

}
