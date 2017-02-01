/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db_classes.DBManager;
import db_classes.Restaurant;
import db_classes.Review;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author leonardo
 */
public class Profile extends HttpServlet {
    
    
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
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //prendo il nome del ristorante passato come parametro
        String tmp = request.getParameter("name");
        String restName = tmp.replaceAll("_", " ");
                
        out.println("<html><head><title>"+restName+"</title>");
        request.getRequestDispatcher("header.jsp").include(request, response);
        Restaurant res_tmp = manager.getRestaurant(restName);
        System.out.println(request.getContextPath()+"/"+res_tmp.getSinglePhotoPath());
        out.println("<div class=\"container-fluid\"><div class=\"jumbotron\" id=\"jumbo-res\" style=\"background-image: url(../"+request.getContextPath()+"/"+res_tmp.getSinglePhotoPath()+");\">");
        //        + "<img src=\""+request.getContextPath()+"/"+res_tmp.getSinglePhotoPath()+"\">");
        out.println("<h1 id=\"profile-res-title\">"+res_tmp.getName()+"</h1>");
        if(request.getSession().getAttribute("user")!=null){
            //puo settare il voto con le stelline solo se loggato
            out.println("<div name=\"rating\" class=\"col-md-offset-8 acidjs-rating-stars\">"+
                        "<form>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-0\" value=\"5\" /><label for=\"group-2-0\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-1\" value=\"4\" /><label for=\"group-2-1\"></label>"+
                            "<input type=\"radio\" checked=\"checked\" name=\"group-2\" id=\"group-2-2\" value=\"3\" /><label for=\"group-2-2\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-3\" value=\"2\" /><label for=\"group-2-3\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-4\"  value=\"1\" /><label for=\"group-2-4\"></label>"+
                        "</form></div>");
            }else{
                //vuol dire che non è loggato e non puo settare le stelline
                out.println("<div name=\"rating\" class=\"col-md-offset-8 acidjs-rating-stars acidjs-rating-disabled\">"+
                        "<form>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-0\" value=\"5\" /><label for=\"group-2-0\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-1\" value=\"4\" /><label for=\"group-2-1\"></label>"+
                            "<input type=\"radio\"  name=\"group-2\" id=\"group-2-2\" value=\"3\" /><label for=\"group-2-2\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-3\" value=\"2\" /><label for=\"group-2-3\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-4\"  value=\"1\" /><label for=\"group-2-4\"></label>"+
                        "</form></div>");
            }

        out.println("</div></div>");
        
        
        
        //qui ci metto descrizione ristorante + stelline
        out.println("<div class=\"container\"><div class=col-md-2></div>");
        
        out.println("<div class=col-md-8>");

        //qua descrizione
        out.println(res_tmp.getDescription()
        +"<br><h3>Oradi di apertura:</h3> 11:30 - 14:00 // 18:00 - 23:00"
        +"<br><h3>Prices:"
        +"<br><h3>Where are we:<br></h3>");
        //qui andrebbe mappa
           
        
        out.println("<hr><div class=\"container\"><div class=\"col-md-2\"></div>"
                + "<div class=\"col-md-8\">");
        if(request.getSession().getAttribute("user")!=null){
            //se è loggato allora ha l' opportunità di commentare e mettere le stelline

            //qua invece è il commento vero e proprio
            out.println("<form action=\"AddComment\" type=\"post\">"
                    + "<label for=\"comment\">Add a review for this restaurant</label><br>"
                    //le stelline
                    +"<div name=\"rating\" class=\" acidjs-rating-stars\">"+
                        ""+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-0\" value=\"5\" /><label for=\"group-2-0\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-1\" value=\"4\" /><label for=\"group-2-1\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-2\" value=\"3\" /><label for=\"group-2-2\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-3\" value=\"2\" /><label for=\"group-2-3\"></label>"+
                            "<input type=\"radio\" name=\"group-2\" id=\"group-2-4\"  value=\"1\" /><label for=\"group-2-4\"></label>"+
                        "</div>"
                    
                    + "Title: <input type=\"text\" id=\"title\" class=\"form-control\" name=\"title\"/><br>"
                    + "<textarea class=\"form-control\" rows=\"4\" cols=\"50\" type=\"text\" name=\"description\"></textarea>"
                    + "<input type=\"file\" name=\"image\" accept=\"image/*\">"
                    + "<button type=\"submit\">Add review</button>");
            out.println("</form>");
            HttpSession session = request.getSession(false);
            session.setAttribute("RestName", restName);
        }
        else{
                //ciaobello
            out.println("<label for=\"comment\">Add a comment for this restaurant</label><br>"
                    + "<a href=\"login_page.jsp\">You must be logged in to add a review</a>"
                    + "<br>");
        
        }
        out.println("</div></div>");
        //devo prendere una lista di tutte le review per il dato ristorante
        
        
        //elenco review per ristoranti
        ArrayList<Review> reviews = manager.getReviewPerRestaurant(restName);

        out.println("<div class=\"row col-md-8 col-md-offset-2\">");
        int i =0;
        for(i=0;i<reviews.size();i++){
            out.println("<hr><h4>"+reviews.get(i).getTitle()+"</h4><br>");
            out.println(reviews.get(i).getDescription());
            out.println("<br>Rating: "+reviews.get(i).getRating());
            out.println("<br>Review by: "+reviews.get(i).getUser());
            out.println("<br>Do you like this review?"
                    + "<a href=\"ValuateReview?value=1&revId="+reviews.get(i).getId()+"\">Yes</a>"
                    + "<a href=\"ValuateReview?value=0&revId="+reviews.get(i).getId()+"\">No</a>");
        }
        out.println("</div>");
        
        out.println("<script src=\"media/js/scripts.js\"></script>");
        
        out.println("</body></html>");

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
