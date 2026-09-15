package com.example.filapp.modelo;

import android.graphics.Bitmap;

public class Fichero {
    String nom;
    byte[] contenido;
    String formato;

    public Fichero(String nom, byte[] contenido, String formato){
        this.nom = nom;
        this.contenido = contenido;
        this.formato = formato;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
}
