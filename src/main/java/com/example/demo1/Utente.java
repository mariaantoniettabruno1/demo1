package com.example.demo1;

public class Utente {
    private String account;
    private String password;
    private String ruolo;

    public Utente(String account,String password, String ruolo) {
        this.account = account;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getRuolo() {
        return ruolo;
    }
    @Override
    public String toString() {
        return  "{ " +
                "\"Account\": " + "\""+this.getAccount()+"\", " +
                "\"Password\": " + "\""+this.getPassword()+"\", " +
                "\"Ruolo\": " + "\""+this.getRuolo()+"\", " +
                " }";
    }
}
