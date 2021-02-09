package com.example.demo1;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class DAO {
    private static final String url = "jdbc:mysql://localhost:3306/test?useSSL=false";
    private static final String user = "root";
    private static final String password = "";

    //Registrare i driver JDBC necessari all'interazione con il db usato
    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Correct registration of Driver");
        } catch (SQLException e) {
            System.out.println("Error in registerDriver" + e.getMessage());
        }

    }

    //inizio metodi insert
    //metodo per l'inserimento di un nuovo corso all'interno del database
    public static int insertCorso(String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        int righe = st.executeUpdate("INSERT INTO CORSO(MATERIA) VALUES('" + Materia + "')");
        System.out.println("Aggiunta la riga " + Materia);
        st.close();
        connection.close();
    }

    //metodo per l'inserimento di un nuovo docente nel database
    public static void insertDocente(String Nome, String Cognome) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("INSERT INTO DOCENTE(NOME,COGNOME) VALUES('" + Nome + "','" + Cognome + "')");     //id auto increment
        System.out.println("Aggiunta la riga " + Nome + Cognome);
        st.close();
        connection.close();
    }

    //metodo per l'associazione tra corso e docente
    public static int insertInsegna(String nome,String cognome, String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        int idDocente = getIdDocente(nome,cognome);
        int idMateria = getIdMateria(Materia);
        int righe = 0;
        Statement st = connection.createStatement();
        int righe = st.executeUpdate("INSERT INTO Insegna(idDocente,Materia) VALUES('" + idDocente + "','" + Materia + "')");
        if (righe > 0) {
            System.out.println("Aggiunta la riga " + idDocente + "," + Materia);
        } else {
            System.out.println("Docente e/o materia non sono presenti nel database");
        }
        st.close();
        connection.close();
    }

    //metodo per la prenotazione da parte di un utente, utilizzato anche per  mostrare la cronologia in seguito.
    public static int insertPrenotazione(String Nome, String Cognome, String Materia, String Account, String Data, String Ora) throws SQLException, ParseException {
        int idDocente = getIdDocente(Nome,Cognome);
        int idMateria = getIdMateria(Materia);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();


            //inizio inserimento prenotazione
            int righe = st.executeUpdate("INSERT INTO Prenotazione(Nome,Cognome,Materia,Account,Data,Ora) " +
                    "SELECT * " +
                    //controllo che la riga che stiamo cercando di inserire esista dentro la tabella delle ripetizioni
                    "FROM(SELECT '" + Nome + "' AS Nome, '" + Cognome + "' as Cognome, '" + Materia + "' AS Materia,'" + Account + "' AS Account, '" + Data + "' AS Data, '" + Ora + "' AS Ora) AS query " +
                    "WHERE EXISTS(SELECT idDocente,iDMateria,Data,Ora " +
                    "FROM ripetizioni " +
                    //controlli vari  tra cui anche che la ripetizione sia disponibile
                    "WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.idMateria=" + idMateria + " AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "' AND UPPER(ripetizioni.disponibilita)='si')" +
                    //controllo che la riga non esista già dentro la tabella
                    "AND NOT EXISTS(SELECT Nome, Cognome, Materia, Data, Ora FROM Prenotazione " +
                    "WHERE Prenotazione.Nome='" + Nome + "' AND Prenotazione.Cognome='"+ Cognome + "' AND Prenotazione.Materia = '" + Materia + "' AND Prenotazione.Data='" + Data + "' AND Prenotazione.Ora='" + Ora + "' AND stato = 'attiva')" +
                    //controllo che la ripetizione sia tra lunedi e venerdi, e che sia tra le 15 e le 18
                    "AND WEEKDAY('" + Data + "')<5 AND HOUR('" + Ora + "')>=15 AND HOUR('" + Ora + "')<=18");

            if (righe > 0) {
                System.out.println("Prenotazione in data " + Data + " effettuata con successo.");
                st.executeUpdate("UPDATE ripetizioni SET disponibilita='no' WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.idMateria=" + idMateria + " AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "'");
            } else {
                System.out.println("La prenotazione non è andata a buon fine, è possibile che dei dati siano sbagliati o che la ripetizione sia già stata prenotata.");
            }

        st.close();
        connection.close();
        return righe;
    }

    public static int getIdDocente(String Nome, String Cognome) throws SQLException  {
        Connection conn = null;
        int id = 0;

        try {
            conn = DriverManager.getConnection(url, user, password);

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT id FROM docente WHERE docente.nome='"+Nome+"' AND docente.cognome = '"+Cognome+"';");

            while(rs.next()){
                id = rs.getInt("id");
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return id;

    }

    public static int getIdMateria(String Materia) throws SQLException  {
        Connection conn = null;
        int id = 0;

        try {
            conn = DriverManager.getConnection(url, user, password);

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT id FROM corso WHERE corso.Materia='"+Materia+"'");

            while(rs.next()){
                id = rs.getInt("id");
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return id;

    }

    //inizio metodi delete

    //metodo per l'eliminazione di un corso
    public static void deleteCorso(String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        int righe = st.executeUpdate("DELETE FROM CORSO WHERE Materia='" + Materia + "';");

        if (righe > 0) {
            System.out.println("Cancellata la riga " + Materia);
        } else {
            System.out.println("Non esiste la materia " + Materia + " nel database.");
        }

        st.close();
        connection.close();
    }

    //metodo per l'eliminazione di un docente
    public static void deleteDocente(int idDocente) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        int righe = st.executeUpdate("DELETE FROM docente WHERE id = " + idDocente + " ;");
        if (righe > 0) {
            System.out.println("Cancellata la riga " + idDocente);
        } else {
            System.out.println("Non esiste il docente " + idDocente + " nel database");
            st.close();
            connection.close();
        }
    }

    //metodo per l'eliminazione dell'associazione corso docente
    public static void deleteInsegna(String nome, String cognome, String Materia) throws SQLException {
        int idDocente = getIdDocente(nome, cognome);
        int idMateria = getIdMateria(Materia);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM Insegna WHERE idDocente=" + idDocente + " AND Materia='" + Materia + "'");
        System.out.println("Cancellata la riga " + idDocente + "," + Materia);
        st.close();
        connection.close();
    }

    //l'eliminazione di una prenotazione
    public static int deletePrenotazione(String Nome, String Cognome, String Materia, String account, String Data, String Ora) throws SQLException {
        int idDocente = getIdDocente(Nome,Cognome);
        int idMateria = getIdMateria(Materia);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        int valido = 0;
        ResultSet rs = st.executeQuery("SELECT CURRENT_DATE < '"+Data+"' AS valido");
        while(rs.next()){
            valido = rs.getInt("valido");
        }
        if(valido == 1){
            int righe = st.executeUpdate("UPDATE Prenotazione SET  stato = 'disdetta'  WHERE Nome='" + Nome + "' AND Cognome='"+ Cognome +"' AND Materia='" + Materia + "' AND Account='" + account + "' AND Data='" + Data + "' AND Ora='" + Ora + "'");
            if(righe>0){
                st.executeUpdate("UPDATE ripetizioni SET disponibilita='si' WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.idMateria=" + idMateria + "  AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "'");
            }
            else{
                System.out.println("Prenotazione non esiste");
            }
        }else{
            System.out.println("La ripetizione che si intende disdire è già stata effettuata dal professore.");
        }



        st.close();
        connection.close();
        return valido;
    }

    //metodo per il login, controlla che esista l'account e restituisce il ruolo
    public static String checkAcc(String account, String pwd) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        ResultSet rs;
        String out = "";
        rs = st.executeQuery("SELECT ruolo FROM Utente WHERE account = '" + account + "' AND password= '" + pwd + "'");
        while (rs.next()) {
            out = out + rs.getString("ruolo");
        }
        rs.close();
        st.close();
        connection.close();
        return out;

    }

    public static int prenotazioneEffettuata(String nome, String cognome, String materia, String account, String data, String ora) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        int valido = 0;
        ResultSet rs = st.executeQuery("SELECT CURRENT_DATE > '"+data+"' AS valido");
        while(rs.next()){
            valido = rs.getInt("valido");
        }
        if(valido == 1){
            st.executeUpdate("UPDATE Prenotazione SET  stato = 'effettuata'  WHERE Nome='" + nome + "' AND Cognome='" +
                    cognome + "' AND Materia='"+ materia +"' AND Account='" + account + "' AND Data='" +
                    data + "' AND Ora='" + ora + "'");
        }

        st.close();
        connection.close();
    }

    public static ArrayList<Docente> showDocente() throws SQLException {
        Connection conn = null;
        ArrayList<Docente> out = new ArrayList<>();
        ResultSet rs;
        Docente doc;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            rs = st.executeQuery("SELECT * FROM Docente");

            while (rs.next()) {
                doc = new Docente(rs.getInt("idDocente"), rs.getString("Nome"), rs.getString("Cognome"));
                out.add(doc);
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static ArrayList<Corso> showCorso() throws SQLException {
        Connection conn = null;
        ArrayList<Corso> out = new ArrayList<>();
        ResultSet rs;
        Corso corso;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            rs = st.executeQuery("SELECT * FROM Corso");

            while (rs.next()) {
                corso = new Corso(rs.getString("Materia"));
                out.add(corso);
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static ArrayList<Insegna> showInsegna() throws SQLException {
        Connection conn = null;
        ArrayList<Insegna> out = new ArrayList<>();
        Insegna insegna;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT insegna.id, docente.Nome, docente.Cognome, corso.Materia " +
                                    "FROM docente, insegna, corso " +
                                    "WHERE docente.id = insegna.idDocente AND corso.id=insegna.idMateria ; ");

            while (rs.next()) {
                insegna = new Insegna(rs.getInt("id"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"));
                out.add(insegna);
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;


    }

    public static ArrayList<Ripetizioni> showRipetizioni() {
        Connection conn = null;
        ArrayList<Ripetizioni> out = new ArrayList<>();
        Ripetizioni rip;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT Nome, Cognome, Materia, Data, Ora " +
                    "FROM docente, ripetizioni " +
                    "WHERE docente.idDocente = ripetizioni.idDocente " +
                    "AND (ripetizioni.disponibilita='si' OR  ripetizioni.disponibilita='Si' OR ripetizioni.disponibilita='SI'); ");

            while (rs.next()) {
                rip = new Ripetizioni(rs.getInt("id"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getString("Data"), rs.getString("Ora"));
                out.add(rip);
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }


    public static ArrayList<Prenotazione> showPrenotazione(String account) throws SQLException {
        Connection conn = null;
        ArrayList<Prenotazione> out = new ArrayList<>();
        Prenotazione pre;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT Nome, Cognome, Materia, account, Data, Ora, stato " +
                    "FROM prenotazione, docente " +
                    "WHERE prenotazione.account ='" + account + "' AND docente.idDocente = prenotazione.idDocente; ");
            while (rs.next()) {
                pre = new Prenotazione(rs.getInt("id"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getString("Data"), rs.getString("Ora"), rs.getString("Stato"));
                out.add(pre);
            }


            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;

    }

    public static ArrayList<PrenotazioneAmministratore> showPrenotazioneAmministratore() throws SQLException {
        Connection conn = null;
        ArrayList<PrenotazioneAmministratore> out = new ArrayList<>();
        PrenotazioneAmministratore pre;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT id, Nome, Cognome, Materia, Data, Ora, Stato, Account " +
                    "FROM prenotazione ");

            while (rs.next()) {
                pre = new PrenotazioneAmministratore(rs.getInt("id"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getString("Data"), rs.getString("Ora"), rs.getString("Stato"), rs.getString("account"));
                out.add(pre);
            }


            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

}
