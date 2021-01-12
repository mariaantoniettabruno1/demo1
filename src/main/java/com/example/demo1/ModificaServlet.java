package com.example.demo1;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.sql.SQLException;

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
/*
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title> Modifica della Servlet </title>");
            out.println("</head>");
            out.println("<body>");
            if(nomeCorso!=null){
                DAO.insertCorso(""+nomeCorso+"");
                out.println("Corso "+nomeCorso+"Aggiunto");
            }
       else*/

                if(nomeDocente!=null && cognomeDocente!=null){
                if(user!=null && password!=null) {
                    if (DAO.checkAcc(""+user,""+password).equals("amministratore")) {
                        /*DAO.insertDocente("" + nomeDocente, "" + cognomeDocente);
                        out.println("Docente " + nomeDocente + " " + cognomeDocente + " Aggiunto. <br>");*/
                        out.println("Loggato come amministratore");
                    }
                    else {
                        //out.println(document.getElementById("cap").value = risp);
                        out.println("I dati inseriti sono sbagliati o non disponi delle autorizzazioni necessarie");
                    }
                }
                else{
                    out.println("Le variabili dell'account sono nulle.");
                }
            }
            else {
                out.println("Le variabili del docente sono nulle.");
            }
            s.setAttribute("user",user);
            String url = response.encodeURL("ModificaServlet");
            /*
            String azione = request.getParameter("action");
            if (azione!=null && azione.equals("invalida")) {
                s.invalidate();
                out.println("<p>Sessione invalidata!</p>");
                out.println("<p>Ricarica <a href=\"" + url + "\"> la pagina</a></p>");
            }
            else{
                out.println("<p>Invalida <a href=\"" + url + "?action=invalida\"> la sessione</a></p>");
                out.println("<p>Ricarica <a href=\"" + url + "\"> la pagina</a></p>");
            }

            out.println("<body>");
            out.println("</html>");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
