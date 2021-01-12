package com.example.demo1;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "UtenteServlet", urlPatterns = {"/UtenteServlet"})

public class UtenteServlet extends HttpServlet {
    public void init(ServletConfig conf) throws ServletException {
        DAO.registerDriver();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);

    }
    private void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try{
            HttpSession s = request.getSession();
            PrintWriter out = response.getWriter();
            String user = request.getParameter("account");
            String password = request.getParameter("password");
            if(user!=null && password!=null) {
                if (DAO.checkAcc(""+user,""+password).equals("cliente")) {
                    out.println("loggato come cliente");
                }
                else{
                    out.println("variabili nulle");
                }
        }
            s.setAttribute("user",user);
            String url = response.encodeURL("UtenteServlet");
    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
