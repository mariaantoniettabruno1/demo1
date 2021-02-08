package com.example.demo1;

import java.sql.Time;
import java.util.Date;

public class Ripetizioni {
    private int id;
    private String nome;
    private String cognome;
    private String materia;
    private String data;
    private String ora;


    public Ripetizioni(int id, String nome, String cognome, String materia, String data, String ora) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.materia = materia;
        this.data = data;
        this.ora = ora;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getMateria() {
        return materia;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }


    @Override
    public String toString() {
        return "{ " +
                "\"#\": " + "\"" + this.getId() + "\", " +
                "\"Nome\": " + "\"" + this.getNome() + "\", " +
                "\"Cognome\": " + "\"" + this.getCognome() + "\", " +
                "\"Materia\": " + "\"" + this.getMateria() + "\", " +
                "\"Data\": " + "\"" + this.getData() + "\", " +
                "\"Ora\": " + "\"" + this.getOra() + "\", " +
                " }";
    }
}
