/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db_classes.DBManager;
import db_classes.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gianma
 */
public class PwChangeServlet extends HttpServlet {

    private DBManager manager;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pw1 = request.getParameter("password1");
        String pw2 = request.getParameter("password2");
        String username = request.getParameter("username");
        
        System.out.println("Username: "+username);
        
        if(manager.badPassword(pw1, pw2)){
            request.setAttribute("message", "Password errata!");
            RequestDispatcher rd = request.getRequestDispatcher("pwchange_page.jsp");
            rd.forward(request, response);
        }else{
            if(manager.changePWQuery(username, pw2)){
                HttpSession session = request.getSession(false);
                if(session.getAttribute("user") == null){
                    try {
                        User user = manager.existingUser(username);
                        session.setAttribute("user", user);
                    } catch (SQLException ex) {
                        Logger.getLogger(PwChangeServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                response.sendRedirect(request.getContextPath()+"/profile_page.jsp");
            }else{
                request.setAttribute("message", "Something went wrong!");
                RequestDispatcher rd = request.getRequestDispatcher("pwchange_page.jsp");
                rd.forward(request, response);
            }
            
        }
        
        
    }

}
