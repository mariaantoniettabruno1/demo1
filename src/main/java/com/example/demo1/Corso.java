package com.example.demo1;

public class Corso {

    private String Materia;

    public Corso(String materia) {
        this.Materia = materia;
    }

    public String getMateria() {
        return Materia;
    }

    @Override
    public String toString() {
        return  "{ " +
                "\"Corso\": " + "\""+this.getMateria()+"\", " +
                " }";
    }

}
