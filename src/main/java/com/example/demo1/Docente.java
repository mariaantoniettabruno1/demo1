package com.example.demo1;

public class Docente {
    private String nome;
    private String cognome;
    private int id;

    public Docente(String nome, String cognome, int id) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
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

}
