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
import java.util.Calendar;
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
public class AddComment extends HttpServlet {

    
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
        
        String review = request.getParameter("comment");
        User user = (User) request.getSession().getAttribute("user");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        Calendar calendar = Calendar.getInstance();

       
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp ora = new java.sql.Timestamp(now.getTime());

        String restName =  (String) request.getSession().getAttribute("RestName");
        System.out.println(restName);
        int restaurantId = manager.getRestaurantId(restName);
                System.out.println(restaurantId);
        int restaurantOwner = manager.getOwnerId(restaurantId);
        int idReview = this.manager.addReviewPerRestaurant(user.getId(), restaurantId, 0, ora, title, description, 0);
        //se non c'è l' immagine mando notifica senza immagine
        if(image==""){
            System.out.println("porcoddio");
            try{
               
            manager.notifyUser(user.getId(), restaurantOwner,idReview , 0);
            }catch(SQLException e){
                System.out.println(e.toString());
            }
        }else{
        
        }
        
        response.sendRedirect("Profile?name="+restName);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
