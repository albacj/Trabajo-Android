package com.example.alba.busessevilla;

/**
 * Created by Alba on 20/04/2017.
 */

public class Lista_entrada {

    private String textoNumLinea;
    private String textoPuebloLinea;
    private String id_linea;

    public Lista_entrada (String textoNumLinea, String textoPuebloLinea, String id_linea) {
        this.textoNumLinea = textoNumLinea;
        this.textoPuebloLinea = textoPuebloLinea;
        this.id_linea = id_linea;
    }

    public String getTextoNumLinea() {
        return textoNumLinea;
    }

    public String getTextoPuebloLinea() {
        return textoPuebloLinea;
    }

    public String getId_linea() {
        return id_linea;
    }

}
