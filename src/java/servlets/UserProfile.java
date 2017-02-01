/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db_classes.DBManager;
import db_classes.Notification;
import db_classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leonardo
 */
public class UserProfile extends HttpServlet {
    
    private DBManager manager;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        User user = (User) request.getSession().getAttribute("user");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>"+user.getUsername()+"</title>");
        request.getRequestDispatcher("header.jsp").include(request, response);
        
        out.println("<div class=\"jumbotron\" id=\"jumbo-res\" >");
        out.println("<h1 id=\"profile-res-title\">"+user.getFirstname()+" "+user.getLastname()+"</h1></div>");
        out.println("<div class=\"row\">");
            //qua vanno gli stat utente
            out.println("<div class=\"col-md-4 col-md-offset-2\">"
                    + "<h3>Username: </h3>"+user.getUsername()
                    + "<h3>eMail: </h3>"+user.getUsername()
                    + "</div>");
            //qua vanno le notifiche utente
            ArrayList<Notification> notifiche = new ArrayList<>();
            notifiche = manager.getNotificationPerUser(user.getId());
            out.println("<div class=\"col-md-4 col-md-offset-6\">");
            for(int i = 0;i<notifiche.size();i++){
                String tipoNotifica = null;
                if(notifiche.get(i).getType()==0){
                    tipoNotifica = "commento semplice";
                }
                if(notifiche.get(i).getType()==1){
                    tipoNotifica = "commento con foto";
                }if(notifiche.get(i).getType()==2){
                    tipoNotifica = "una segnalazione alla foto";
                }if(notifiche.get(i).getType()==3){
                    tipoNotifica = "una risposta alla recensione";
                }if(notifiche.get(i).getType()==4){
                    tipoNotifica = "like al tuo commento";
                }
                out.println("<hr><div>"
                        + "L' utente "+manager.getUsernameFromId(notifiche.get(i).getNotifier_id())
                        + " Ha eseguito "+tipoNotifica
                        + "<br>"
                        + "ID review = <a href=\"ValidateNotification?id="+notifiche.get(i).getId()+"\">"+notifiche.get(i).getId()+"</a>"
                        + "</div>");
            }
            out.println("</div>");
        out.println("</div>");
        out.println("<script src=\"media/js/scripts.js\"></script></body></html>");

        
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UserProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UserProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
