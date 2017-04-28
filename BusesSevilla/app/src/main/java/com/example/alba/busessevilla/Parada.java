package com.example.alba.busessevilla;

import java.util.ArrayList;

/**
 * Created by Adrián on 28/04/2017.
 */

class Parada {

    private String nombre;
    private String municipio;
    private String latitud;
    private String longitud;
    private ArrayList<String> horarios;
    private Boolean valido;

    public Parada(String nombre, String latitud, String longitud, Boolean valido) {
        this.nombre = nombre;
        this.municipio = "";
        this.latitud = latitud;
        this.longitud = longitud;
        this.valido = valido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public ArrayList<String> getHorarios() {
        return horarios;
    }

    public Boolean esValido() {
        return valido;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setHorarios(ArrayList<String> horarios) {
        this.horarios = horarios;
    }
}