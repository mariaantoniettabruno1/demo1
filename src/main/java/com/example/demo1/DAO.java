package com.example.demo1;

import java.sql.*;
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
        {

        }
    }

    //inizio metodi insert
    //metodo per l'inserimento di un nuovo corso all'interno del database
    public static void insertCorso(String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("INSERT INTO CORSO(MATERIA) VALUES('" + Materia + "')");
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
    public static void insertInsegna(int idDocente, String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
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

    //metodo per l'eventuale registrazione dell'utente
    //Di default alla colonna "ruolo" è assegnato il valore "cliente". Gli amministratori vengono dichiarati direttamente nel database.
    public static void insertUtente(String account, String pwd) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("INSERT INTO UTENTE(ACCOUNT,PASSWORD) VALUES('" + account + "','" + pwd + "')");
        System.out.println("Aggiunta la riga " + account + pwd);
        st.close();
        connection.close();
    }

    //metodo per la prenotazione da parte di un utente, utilizzato anche per  mostrare la cronologia in seguito.
    public static void insertPrenotazione(int idDocente, String Materia, String Account, String Data, String Ora) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        //inizio inserimento prenotazione
        int righe = st.executeUpdate("INSERT INTO Prenotazione(idDocente,Materia,Account,Data,Ora) " +
                "SELECT * " +
                //controllo che la riga che stiamo cercando di inserire esista dentro la tabella delle ripetizioni
                "FROM(SELECT " + idDocente + " AS idDocente,'" + Materia + "' AS Materia,'" + Account + "' AS Account, '" + Data + "' AS Data, '" + Ora + "' AS Ora) AS query " +
                "WHERE EXISTS(SELECT idDocente,Materia,Data,Ora " +
                "FROM ripetizioni " +
                //controlli vari  tra cui anche che la ripetizione sia disponibile
                "WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.Materia='" + Materia + " ' AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "' AND ripetizioni.disponibilita='si')" +
                //controllo che la riga non esista già dentro la tabella
                "AND NOT EXISTS(SELECT idDocente, Materia, Data, Ora FROM Prenotazione " +
                "WHERE Prenotazione.idDocente=" + idDocente + " AND Prenotazione.Data='" + Data + "' AND Prenotazione.Ora='" + Ora + "')" +
                //controllo che la ripetizione sia tra lunedi e venerdi, e che sia tra le 15 e le 18
                "AND WEEKDAY('" + Data + "')<5 AND HOUR('" + Ora + "')>=15 AND HOUR('" + Ora + "')<=18");

        if (righe > 0) {
            System.out.println("Prenotazione in data " + Data + " effettuata con successo.");
            st.executeUpdate("UPTADE ripetizioni SET disponibilita='no' WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.Materia='" + Materia + " ' AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "'");
        } else {
            System.out.println("La prenotazione non è andata a buon fine, è possibile che dei dati siano sbagliati o che la ripetizione sia già stata prenotata.");
        }


        st.close();
        connection.close();
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
        int righe = st.executeUpdate("DELETE FROM Docente WHERE idDocente=" + idDocente + ";");
        if (righe > 0) {
            System.out.println("Cancellata la riga " + idDocente);
        } else {
            System.out.println("Non esiste il docente " + idDocente + " nel database");
            st.close();
            connection.close();
        }
    }

    //metodo per l'eliminazione dell'associazione corso docente
    public static void deleteInsegna(int idDocente, String Materia) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM Insegna WHERE idDocente=" + idDocente + " AND Materia='" + Materia + "'");
        System.out.println("Cancellata la riga " + idDocente + "," + Materia);
        st.close();
        connection.close();
    }

    //l'eliminazione di una prenotazione
    public static void deletePrenotazione(String idDocente, String Materia, String account, Date Data, Time Ora) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        st.executeUpdate("UPDATE Prenotazione SET  'stato' = 'disdetta'  WHERE idDocente=" + idDocente + " AND Materia='" + Materia + "' AND Account='" + account + "' AND Data='" + Data + "' AND Ora='" + Ora + "'");
        st.executeUpdate("UPDATE ripetizioni SET disponibilita='si' WHERE ripetizioni.idDocente=" + idDocente + " AND ripetizioni.Materia='" + Materia + " ' AND ripetizioni.Data='" + Data + "' AND ripetizioni.Ora='" + Ora + "'");
        System.out.println("Cancellata la riga " + idDocente + "," + Materia + "," + account);
        st.close();
        connection.close();
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

    public static void PrenotazioneEffettuata(ArrayList<Prenotazione> id, ArrayList<Prenotazione> Materia, ArrayList<Prenotazione> Account, ArrayList<Prenotazione> Data, ArrayList<Prenotazione> Ora) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();

        st.executeUpdate("UPDATE Prenotazione SET  'stato' = 'effettuata'  WHERE idDocente=" + id + " AND Materia='" + Materia + "' AND Account='" + Account + "' AND Data='" + Data + "' AND Ora='" + Ora + "'");
        st.close();
        connection.close();
    }
   /* public static String queryDB() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Docente: " + "\n";
        rs = st.executeQuery("SELECT * FROM Docente");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Nome = " + rs.getString("NOME") + "\n";

        }
        out = out + "Tabella Corso: " + "\n";
        rs = st.executeQuery("SELECT * FROM CORSO");
        while (rs.next()) {
            out = out + "nomeCorso= " + rs.getString("Materia") + "\n";
        }
        out = out + "Tabella Insegna: " + "\n";
        rs = st.executeQuery("SELECT * FROM Insegna");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Materia = " + rs.getString("Materia") + "\n";
        }
        out = out + "Tabella Prenotazione: " + "\n";
        rs = st.executeQuery("SELECT * FROM Prenotazione");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Materia = " + rs.getString("Materia") + ", account = " + rs.getString("account") + ", Data = " + rs.getString("Data") + ", Ora = " + rs.getString("Ora") + "\n";
        }
        out = out + "Fine";
        rs.close();
        st.close();
        conn.close();
        return out;
    }
    */


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
        ResultSet rs;
        Insegna ins;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            /*rs = st.executeQuery("SELECT Nome,Cognome,Materia" +
                                     "FROM Insegna,Docente" +"WHERE Docente.idDocente = Insegna.idDocente;");*/
            rs = st.executeQuery("SELECT * FROM Insegna");

            while (rs.next()) {
                ins = new Insegna(rs.getInt("idDocente"), rs.getString("Materia"));
                out.add(ins);
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
                rip = new Ripetizioni(rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getDate("Data"), rs.getTime("Ora"));
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
                pre = new Prenotazione(rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getDate("Data"), rs.getTime("Ora"), rs.getString("Stato"));
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

    public static ArrayList<Prenotazione> showPrenotazioneAmministratore() throws SQLException {
        Connection conn = null;
        ArrayList<Prenotazione> out = new ArrayList<>();
        Prenotazione pre;

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT Nome, Cognome, Materia, Data, Ora, stato " +
                    "FROM prenotazione, docente " +
                    "WHERE docente.idDocente = prenotazione.idDocente; ");
            while (rs.next()) {
                pre = new Prenotazione(rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Materia"), rs.getDate("Data"), rs.getTime("Ora"), rs.getString("Stato"));
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
