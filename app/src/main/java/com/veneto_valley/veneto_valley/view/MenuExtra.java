package com.veneto_valley.veneto_valley.view;

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
        public ExtraDish(int numero, double prezzo, String nome) {
            this.numero = numero;
            this.prezzo = prezzo;
            this.nome = nome;
        }
    }
}
