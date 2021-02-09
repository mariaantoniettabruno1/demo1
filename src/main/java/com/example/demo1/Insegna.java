package com.example.demo1;

public class Insegna {

    private int idDocente;
    private String materia;

    public Insegna(int idDocente, String materia) {

        this.idDocente = idDocente;
        this.materia = materia;
    }


    public int getIdDocente() {
        return idDocente;
    }

    public String getMateria() {
        return materia;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"idDocente\": " + "\"" + this.getIdDocente() + "\", " +
                "\"Materia\": " + "\"" + this.getMateria() + "\", " +
                " }";
    }
}
