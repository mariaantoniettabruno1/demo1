package com.example.demo1;

import com.google.gson.Gson;

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
            String nomeCorso = request.getParameter("NomeCorso");
            String nomeDocente = request.getParameter("nomeDocente");
            String cognomeDocente = request.getParameter("cognomeDocente");
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
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showPrenotazione(s.getAttribute("Utente").toString()));
                out.println(jsonArray);
            } else if (azione.equals("prenotaRipetizioni")) {
                //tutti
            } else if (azione.equals("disdiciPrenotazione")) {
                //Cliente
            } else if (azione.equals("segnaEffettuata")) {
                //Cliente
            } else if (azione.equals("mostraTuttaCronologia")) {
                //Amministratore
            } else if (azione.equals("showDocente")) { //Amministratore
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showDocente());
                out.println(jsonArray);
            } else if (azione.equals("showCorso")) { //Amministratore
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showCorso());
                out.println(jsonArray);
            } else if (azione.equals("showInsegna")) { //Amministratore
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showInsegna());
                out.println(jsonArray);
            } else if (azione.equals("showPrenotazioneAmministratore")) { //tutti
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showPrenotazioneAmministratore());
                out.println(jsonArray);
            } else if (azione.equals("inserisciDocente")) {
                //Amministratore
            } else if (azione.equals("inserisciCorso")) {
                //Amministratore
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
