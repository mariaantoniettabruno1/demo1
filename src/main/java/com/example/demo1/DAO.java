package com.example.demo1;
import java.sql.*;

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
    public static void deletePrenotazione(int idDocente, String Materia, String account, String Data, String Ora) throws SQLException {
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

    public static void PrenotazioneEffettuata(int idDocente, String Materia, String Account, String Data, String Ora) throws SQLException{
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        /*
        ResultSet rs = st.executeQuery("SELECT * FROM prenotazione WHERE idDocente="+idDocente+"");
        int idDocente = rs.getInt("idDocente");
        String Materia = rs.getString("Materia");
        String Account = rs.getString("Account");
        String Data = rs.getString("Data");
        String Ora = rs.getString("Ora");
        */
        st.executeUpdate("UPDATE Prenotazione SET  'stato' = 'effettuata'  WHERE idDocente=" + idDocente + " AND Materia='" + Materia + "' AND Account='" + Account + "' AND Data='" + Data + "' AND Ora='" + Ora + "'");
        st.close();
        connection.close();
    }
    public static String queryDB() throws SQLException {
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

    public static String showDocente() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Docente: " + "\n";
        rs = st.executeQuery("SELECT * FROM Docente");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Nome = " + rs.getString("NOME") + "\n";
        }
        out = out + "Fine";
        rs.close(); st.close(); conn.close(); return out;
    }

    public static String showCorso() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Corso: " + "\n";
        rs = st.executeQuery("SELECT * FROM Corso");
        while (rs.next()) {
            out = out + "nomeCorso= " + rs.getString("Materia") + "\n";
        }
        out = out + "Fine";
        rs.close(); st.close(); conn.close(); return out;
    }

    public static String showInsegna() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Insegna: " + "\n";
        rs = st.executeQuery("SELECT * FROM Insegna");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Materia = " + rs.getString("Materia") + "\n";
        }
        out = out + "Fine";
        rs.close(); st.close(); conn.close(); return out;
    }

    public static String showRipetizioni() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Ripetizioni: " + "\n";
        rs = st.executeQuery("SELECT * FROM ripetizioni");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Materia = " + rs.getString("Materia" + ", Data = " + rs.getString("Data") +
                    ", ora = " + rs.getString("Ora") + ", disponibilita = " + rs.getString("disponibilita")   );
        }
        out = out + "Fine";
        rs.close(); st.close(); conn.close(); return out;
    }

    public static String showPrenotazione(String account) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet rs;
        String out = "";
        out = out + "Tabella Prenotazione: " + "\n";
        rs = st.executeQuery("SELECT * FROM prenotazione WHERE Prenotazione.account =" + account +"");
        while (rs.next()) {
            out = out + "idDocente= " + rs.getInt("idDocente") + ", Materia = " + rs.getString("Materia" + ", Account = " + rs.getString("Account") +
                    ", Data = " + rs.getString("Data") + ", Ora = " + rs.getString("Ora") + ", stato = " + rs.getString("Stato"));
        }
        out = out + "Fine";
        rs.close(); st.close(); conn.close(); return out;
    }

}
