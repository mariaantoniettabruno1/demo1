package com.example.demo1;

import java.io.*;
import java.rmi.ServerException;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", urlPatterns = {"/HelloServlet"}) //cambiare url per visualizzare servlet
public class HelloServlet extends HttpServlet {
    private String message;

    //definisco le operazioni che saranno definite una sola volta, quando la Servlet viene creata e inizializzata
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
    //metodo dove racchiudere il codice
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String persona = DAO.queryDB();
        try{
            PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title> Stampa DB Servlet </title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p>" +persona+ "</p>");
                out.println("<p>Fine Database</p>");
                out.println("<form action='/demo1/ModificaServlet' method ='get'>" +
                            "<p><input type='submit' name='BottoneModificaServlet' value='Modifica servlet'></p>" +
                            "</form>");
                out.println("<body>");
                out.println("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void destroy() {
    }
}