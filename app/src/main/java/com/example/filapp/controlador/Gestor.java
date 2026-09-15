package com.example.filapp.controlador;

import com.example.filapp.modelo.Fichero;

import java.io.IOException;

public class Gestor {
    private static Gestor miGestor;

    private Gestor() {

    }

    public static Gestor getMiGestor() {
        if (miGestor==null){
            miGestor = new Gestor();
        }
        return miGestor;
    }

    public void setFichero(String nom, byte[] cont, String form){
        GestorFichero g = GestorFichero.getMiGestorFichero();
        g.setFichero(nom, cont, form);
    }

    public Fichero getFichero(String nom){
        GestorFichero g = GestorFichero.getMiGestorFichero();
        return g.getFichero(nom);
    }

    public void convertirFichero(Fichero f, String form) throws IOException {
        GestorFichero g = GestorFichero.getMiGestorFichero();
        g.convertirFichero(f, form);
    }
}