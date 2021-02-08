package com.example.demo1;

public class PrenotazioneAmministratore {
    private int id;
    private String nome;
    private String cognome;
    private String materia;
    private String data;
    private String ora;
    private String stato;
    private String account;


    public PrenotazioneAmministratore(int id,String nome, String cognome, String materia, String data, String ora, String stato, String account) {
       this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.materia = materia;
        this.data = data;
        this.ora = ora;
        this.stato = stato;
        this.account = account;

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

    public String getStato() {
        return stato;
    }

    public String getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"#\": " + "\"" + this.getNome() + "\", " +
                "\"Nome\": " + "\"" + this.getNome() + "\", " +
                "\"Cognome\": " + "\"" + this.getCognome() + "\", " +
                "\"Materia\": " + "\"" + this.getMateria() + "\", " +
                "\"Data\": " + "\"" + this.getData() + "\", " +
                "\"Ora\": " + "\"" + this.getOra() + "\", " +
                "\"Stato\": " + "\"" + this.getStato() + "\"" +
                "\"Account\": " + "\"" + this.getAccount() + "\"" +
                " }";
    }
}
