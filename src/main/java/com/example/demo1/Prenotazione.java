package com.example.demo1;

import java.sql.Time;
import java.util.Date;

public class Prenotazione {
    private String nome;
    private String cognome;
    private String materia;
    private Date data;
    private Time ora;
    private String stato;



    public Prenotazione(String nome, String cognome, String materia, java.sql.Date data, Time ora, String stato) {
        this.nome = nome;
        this.cognome = cognome;
        this.materia = materia;
        this.data = data;
        this.ora = ora;
        this.stato = stato;
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

    public Date getData() {
        return data;
    }

    public Time getOra() {
        return ora;
    }

    public String getStato() {
        return stato;
    }

    @Override
    public String toString() {
        return  "{ " +
                "\"Nome\": " + "\""+this.getNome()+"\", " +
                "\"Cognome\": " + "\""+this.getCognome()+"\", " +
                "\"Materia\": " + "\""+this.getMateria()+"\", " +
                "\"Data\": " + "\""+this.getData()+"\", " +
                "\"Ora\": " + "\""+this.getOra()+"\", " +
                "\"Stato\": " + "\""+this.getStato()+"\"" +
                " }";
    }
}
