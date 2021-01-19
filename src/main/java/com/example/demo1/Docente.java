package com.example.demo1;

public class Docente {
    private int id;
    private String nome;
    private String cognome;


    public Docente(int id, String nome, String cognome) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;

    }
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getIdDocente() {
        return id;
    }

    @Override
    public String toString() {
        return  "{ " +
                "\"id\": " + "\""+this.getIdDocente()+"\", " +
                "\"Nome\": " + "\""+this.getNome()+"\", " +
                "\"Cognome\": " + "\""+this.getCognome()+"\", " +
                " }";
    }
}
