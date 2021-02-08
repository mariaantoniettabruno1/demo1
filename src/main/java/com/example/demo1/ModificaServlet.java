package com.example.demo1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.text.ParseException;
//grazie raja
import java.util.stream.Collectors;

@WebServlet(name = "ModificaServlet", urlPatterns = {"/ModificaServlet"})
public class ModificaServlet extends HttpServlet {
    public void init(ServletConfig conf) throws ServletException {
        DAO.registerDriver();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession s = request.getSession();
            PrintWriter out = response.getWriter();
            String user = request.getParameter("utente");
            String password = request.getParameter("password");
            String azione = request.getParameter("azione");

            if (azione.equals("checkACC")) {
                s.setAttribute("Utente", user);
                s.setAttribute("Password", password);

                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.checkAcc("" + user, "" + password));
                out.println(jsonArray);

            } else if (azione.equals("showRipetizioni")) { //tutti
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showRipetizioni());
                out.println(jsonArray);
            } else if (azione.equals("showPrenotazione")) { //tutti
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonArray = gson.toJson(DAO.showPrenotazione(user));
                out.println(jsonArray);
            } else if (azione.equals("inserisciPrenotazione")) {

                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String materia =request.getParameter("materia");
                String account =request.getParameter("account");
                String data =request.getParameter("data");
                String ora =request.getParameter("ora");
                //System.out.println(nome + " " + cognome + " " +  materia + " " + account + " " + data + " " + ora);

                DAO.insertPrenotazione(nome,cognome,materia,account,data,ora);

            } else if (azione.equals("deletePrenotazione")) {
                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String materia =request.getParameter("materia");
                String account =request.getParameter("account");
                String data =request.getParameter("data");
                String ora =request.getParameter("ora");
                int valido = DAO.deletePrenotazione(nome,cognome,materia,account,data,ora);
                out.println(valido);

            } else if (azione.equals("segnaEffettuata")) {

                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String materia = request.getParameter("materia");
                String account = request.getParameter("account");
                String data = request.getParameter("data");
                String ora = request.getParameter("ora");
                int valido = DAO.prenotazioneEffettuata(nome, cognome, materia, account, data, ora);
                out.println(valido);

            }else if (azione.equals("showDocente")) { //Amministratore

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonArray = gson.toJson(DAO.showDocente());
                out.println(jsonArray);

            } else if (azione.equals("showCorso")) { //Amministratore
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonArray = gson.toJson(DAO.showCorso());
                out.println(jsonArray);
            } else if (azione.equals("showInsegna")) { //Amministratore
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonArray = gson.toJson(DAO.showInsegna());
                out.println(jsonArray);
            } else if (azione.equals("showPrenotazioneAmministratore")) { //tutti
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonArray = gson.toJson(DAO.showPrenotazioneAmministratore());
                out.println(jsonArray);
            } else if (azione.equals("inserisciDocente")) {
                //Amministratore
            } else if (azione.equals("inserisciCorso")) {
                String materia = request.getParameter("materia");
                int valido = DAO.insertCorso(materia);
                out.println(valido);

            } else if (azione.equals("inserisciInsegna")) {
                //Amministratore
            } else if (azione.equals("deleteDocente")) {
                //Amministratore
            } else if (azione.equals("deleteCorso")) {
                //Amministratore
            } else if (azione.equals("deleteInsegna")) {
                //Amministratore
            }

            String url = response.encodeURL("ModificaServlet");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
