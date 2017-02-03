/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db_classes.DBManager;
import db_classes.Restaurant;
import db_classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
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
public class MyRestaurants extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            User user = (User) request.getSession().getAttribute("user");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>"+user.getUsername()+"</title>");
            request.getRequestDispatcher("header.jsp").include(request, response);

            out.println("<div class=\"col-md-2\"></div><div class=\"col-md-8\"><div class=\"container-fluid\"><h2>All My Restaurants</h2>"
                    + "<table id=\"res-tab\">");
            
            ArrayList<Restaurant> ristoranti = manager.getRestaurantsByUserId(user.getId());
            
            for(int i = 0; i< ristoranti.size();i++){
                String newName = ristoranti.get(i).getName();
                        String tmp = newName.replaceAll("\\s+","_");
                        out.println("<tr id=\"res-row\">"
                                + "<td><img src=\""+request.getContextPath()+"/"+ristoranti.get(i).getSinglePhotoPath()+"\" id=\"results-img\"></td>");
                        out.println("<td><ul><h1><a href=\""+request.getContextPath()+"/Profile?name="+tmp+"\" id=\"res_name\">"+ristoranti.get(i).getName()+" </a></h1></ul>");
                        
                        String address = ristoranti.get(i).getAddress()+" "+ristoranti.get(i).getCivicNumber()+" "+ristoranti.get(i).getCity();
                        out.println("<ul><a href=\"https://www.google.it/maps/?q="+URLEncoder.encode(address, "utf-8")+"\" target=\"_blank\">"+ristoranti.get(i).getAddress()+", "+ristoranti.get(i).getCivicNumber()
                                + ", "+ristoranti.get(i).getCity()+"</a></ul>");
                        
                        out.println("<ul>");
                        String [] cuisineTypes = ristoranti.get(i).getCuisineTypes();
                        for(int j = 0; j < cuisineTypes.length; j++){
                            out.print(cuisineTypes[j]);
                            if(j != cuisineTypes.length-1){
                                out.print(", ");
                            }
                        }
                        out.println("</ul>");
                        
                        out.println("<ul><a href=\""+ristoranti.get(i).getWebSiteUrl()+"\">"+ristoranti.get(i).getWebSiteUrl()+"</a> </ul></td>");
                        out.println("</tr>");
            }
            
            out.println("</table></div></div><div class=\"col-md-2\"></div>");
            out.println("<div class=\"col-md-1 col-sm-1 col-xs-0\"></div>\n" +
"            <div class=\"col-md-10\">  \n" +
"                <hr>\n" +
"                <p id=\"footer\">@2016-2017 Programmazione per il Web | 404 Group Not Found</p>\n" +
"            </div>\n" +
"            <div class=\"col-md-1 col-sm-1 col-xs-0\"></div>");        
            out.println("</body></html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
            Logger.getLogger(MyRestaurants.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MyRestaurants.class.getName()).log(Level.SEVERE, null, ex);
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
