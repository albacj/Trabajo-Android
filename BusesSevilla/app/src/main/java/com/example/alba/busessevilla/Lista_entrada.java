package com.example.alba.busessevilla;

/**
 * Created by Alba on 20/04/2017.
 */

public class Lista_entrada {

    private String textoNumLinea;
    private String textoPuebloLinea;

    public Lista_entrada (String textoNumLinea, String textoPuebloLinea) {
        this.textoNumLinea = textoNumLinea;
        this.textoPuebloLinea = textoPuebloLinea;
    }

    public String getTextoNumLinea() {
        return textoNumLinea;
    }

    public String getTextoPuebloLinea() {
        return textoPuebloLinea;
    }

}
