package com.example.demo1;

public class Corso {

    private int id;
    private String Materia;

    public Corso(int id, String materia) {
        this.id = id;
        this.Materia = materia;
    }

    public String getMateria() {
        return Materia;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"Corso\": " + "\"" + this.getMateria() + "\", " +
                " }";
    }

}
