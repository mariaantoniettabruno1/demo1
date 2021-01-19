package com.example.demo1;

import com.google.gson.Gson;
import org.json.JSONArray;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ModificaServlet", urlPatterns = {"/ModificaServlet"})
public class ModificaServlet extends HttpServlet {
    public void init(ServletConfig conf) throws ServletException {
        DAO.registerDriver();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServerException,IOException {
        try {
            processRequest(request,response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException {
        try {
            processRequest(request,response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            HttpSession s = request.getSession();
            PrintWriter out = response.getWriter();
            String nomeCorso = request.getParameter("NomeCorso");
            String nomeDocente = request.getParameter("nomeDocente");
            String cognomeDocente = request.getParameter("cognomeDocente");
            String user = request.getParameter("utente");
            String password = request.getParameter("password");
            String azione = request.getParameter("azione");
            if(azione.equals("checkACC")){
                s.setAttribute("Utente", user);
                s.setAttribute("Password", password);

                String valido = (DAO.checkAcc("" + user, "" + password));
                out.println(valido);

            }
            else if(azione.equals("showRipetizioni")){
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showRipetizioni());
                out.println(jsonArray);
            }
            else if(azione.equals("showPrenotazione")){
                Gson gson = new Gson();
                String jsonArray = gson.toJson(DAO.showPrenotazione(s.getAttribute("Utente").toString()));
                out.println(jsonArray);
            }


            s.setAttribute("user",user);
            String url = response.encodeURL("ModificaServlet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
