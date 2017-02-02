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
import java.net.URLEncoder;
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
     * @throws java.sql.SQLException
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
        out.println("<div class=\"col-md-1\"></div><div class=\"col-md-10\"><div class=\"container-fluid\"><div class=\"jumbotron\" id=\"jumbo-res\" style=\"background-image: url(../"+request.getContextPath()+"/"+res_tmp.getSinglePhotoPath()+");\">");
        out.println("<h1 id=\"profile-res-title\">"+res_tmp.getName().replace('*', '\'')+"</h1>");
        
        //stelline
        int stars = (int) res_tmp.getGlobal_value();
        out.println("<br><div name=\"rating\" class=\"acidjs-rating-stars acidjs-rating-disabled\">"+
                    "<form>");
        switch(stars){
            case 1:
                out.println("<input type=\"radio\" name=\"group-rest\" id=\"group-rest-0\" value=\"5\" /><label for=\"group-rest-0\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-1\" value=\"4\" /><label for=\"group-rest-1\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-2\" value=\"3\" /><label for=\"group-rest-2\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-3\" value=\"2\" /><label for=\"group-rest-3\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-rest\" id=\"group-rest-4\"  value=\"1\" /><label for=\"group-rest-4\"></label>");
                break;
            case 2:
                out.println("<input type=\"radio\" name=\"group-rest\" id=\"group-rest-0\" value=\"5\" /><label for=\"group-rest-0\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-1\" value=\"4\" /><label for=\"group-rest-1\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-2\" value=\"3\" /><label for=\"group-rest-2\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-rest\" id=\"group-rest-3\" value=\"2\" /><label for=\"group-rest-3\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-4\"  value=\"1\" /><label for=\"group-rest-4\"></label>");
                break;
            case 3:
                out.println("<input type=\"radio\" name=\"group-rest\" id=\"group-rest-0\" value=\"5\" /><label for=\"group-rest-0\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-1\" value=\"4\" /><label for=\"group-rest-1\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-rest\" id=\"group-rest-2\" value=\"3\" /><label for=\"group-rest-2\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-3\" value=\"2\" /><label for=\"group-rest-3\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-4\"  value=\"1\" /><label for=\"group-rest-4\"></label>");
                break;
            case 4:
                out.println("<input type=\"radio\" name=\"group-rest\" id=\"group-rest-0\" value=\"5\" /><label for=\"group-rest-0\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-rest\" id=\"group-rest-1\" value=\"4\" /><label for=\"group-rest-1\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-2\" value=\"3\" /><label for=\"group-rest-2\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-3\" value=\"2\" /><label for=\"group-rest-3\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-4\"  value=\"1\" /><label for=\"group-rest-4\"></label>");
                break;
            case 5:
                out.println("<input type=\"radio\" checked=\"true\" name=\"group-rest\" id=\"group-rest-0\" value=\"5\" /><label for=\"group-rest-0\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-1\" value=\"4\" /><label for=\"group-rest-1\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-2\" value=\"3\" /><label for=\"group-rest-2\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-3\" value=\"2\" /><label for=\"group-rest-3\"></label>"+
                    "<input type=\"radio\" name=\"group-rest\" id=\"group-rest-4\"  value=\"1\" /><label for=\"group-rest-4\"></label>");
                break;
        }
        
        out.println("</form></div>");
        
        out.println("<br><br><form target=\"_blank\" action=\"QR\" type=\"GET\"><input type=\"hidden\" name=\"address\" value=\""+res_tmp.getAddress()+"_"+res_tmp.getCivicNumber()
                +"_"+res_tmp.getCity()+"\">"
                + "<input type=\"hidden\" name=\"name\" value=\""+res_tmp.getName()+"\">"
                + "<button type=\"submit\" class=\"btn btn-danger\">QRCode</button></form>");
        
        if(res_tmp.getId_owner() == 0 && request.getSession().getAttribute("user")!=null){
            out.println("<form action=\"ClaimRestaurantServlet\" type=\"POST\">"
                    + "<input type=\"hidden\" name=\"restid\" value=\""+res_tmp.getId()+"\">"
                    + "<input type=\"hidden\" name=\"name\" value=\""+res_tmp.getName()+"\">"
                    + "<button type=\"submit\" class=\"btn btn-danger\">Claim </button>"
                    + "</form>");
        }
        out.println("</div></div></div><div class=\"col-md-1\"></div>");
        
        //Dati ristorante
        out.println("<div class=\"container\"><div class=col-md-2></div>");
        out.println("<div class=col-md-5>");
        out.println("<p>"+res_tmp.getDescription()+"</p>"
        +"<br><h3>Oradi di apertura:</h3>");
        if(res_tmp.getWeek().isMonday()){
            out.println("<p>Monday: "+res_tmp.getWeek().getMonday_l_op()+" to "+res_tmp.getWeek().getMonday_l_cl()+""
                    + " - "+res_tmp.getWeek().getMonday_d_op()+" to "+res_tmp.getWeek().getMonday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isTuesday()){
            out.println("<p>Tuesday: "+res_tmp.getWeek().getTuesday_l_op()+" to "+res_tmp.getWeek().getTuesday_l_cl()+""
                    + " - "+res_tmp.getWeek().getTuesday_d_op()+" to "+res_tmp.getWeek().getTuesday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isWednesday()){
            out.println("<p>Wednesday: "+res_tmp.getWeek().getWednesday_l_op()+" to "+res_tmp.getWeek().getWednesday_l_cl()+""
                    + " - "+res_tmp.getWeek().getWednesday_d_op()+" to "+res_tmp.getWeek().getWednesday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isThursday()){
            out.println("<p>Thursday: "+res_tmp.getWeek().getThursday_l_op()+" to "+res_tmp.getWeek().getThursday_l_cl()+""
                    + " - "+res_tmp.getWeek().getThursday_d_op()+" to "+res_tmp.getWeek().getThursday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isFriday()){
            out.println("<p>Friday: "+res_tmp.getWeek().getFriday_l_op()+" to "+res_tmp.getWeek().getFriday_l_cl()+""
                    + " - "+res_tmp.getWeek().getFriday_d_op()+" to "+res_tmp.getWeek().getFriday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isSaturday()){
            out.println("<p>Saturday: "+res_tmp.getWeek().getSaturday_l_op()+" to "+res_tmp.getWeek().getSaturday_l_cl()+""
                    + " - "+res_tmp.getWeek().getSaturday_d_op()+" to "+res_tmp.getWeek().getSaturday_d_cl()+"</p>");
        }
        if(res_tmp.getWeek().isSunday()){
            out.println("<p>Sunday: "+res_tmp.getWeek().getSunday_l_op()+" to "+res_tmp.getWeek().getSunday_l_cl()+""
                    + " - "+res_tmp.getWeek().getSunday_d_op()+" to "+res_tmp.getWeek().getSunday_d_cl()+"</p>");
        }
        out.println("</div><div class=\"col-md-4\">");
        out.println("<br><h3>Cuisines:</h3>");
        for(int i = 0; i < res_tmp.getCuisineTypes().length; i++){
            out.println(res_tmp.getCuisineTypes()[i]);
            if(i < res_tmp.getCuisineTypes().length - 1 ){
                out.println(", ");
            }
        }
        
        out.println("<br><h3>Prices:</h3>");
        if(res_tmp.getPrice() == 1){
            out.println("<p>Low (<10&euro;)</p>");
        }else if(res_tmp.getPrice() == 2){
            out.println("<p>Medium (~20&euro;)</p>");
        }else if(res_tmp.getPrice() == 3){
            out.println("<p>High (>30&euro;)</p>");
        }
        
        out.println("<br><h3>Where are we:<br></h3>");
        String address = res_tmp.getAddress()+" "+res_tmp.getCivicNumber()+" "+res_tmp.getCity();
        out.println("<p><a href=\"https://www.google.it/maps/?q="+URLEncoder.encode(address, "utf-8")+"\" target=\"_blank\">"
                +res_tmp.getAddress()+", "+res_tmp.getCivicNumber()+", "+res_tmp.getCity()+ "</a></p>");
        out.println("</div><div class=col-md-1></div></div>");   
        
        out.println("<hr><div class=\"container\"><div class=\"col-md-2\"></div>"
                + "<div class=\"col-md-8\">");
        if(request.getSession().getAttribute("user")!=null){
            //se è loggato allora ha l' opportunità di commentare e mettere le stelline

            //qua invece è il commento vero e proprio
            out.println("<form enctype=\"multipart/form-data\" action=\"AddComment\" method=\"post\">"
                    + "<label for=\"comment\">Add a review for this restaurant</label><br>"
                    //le stelline
                    +"<div name=\"rating\" class=\" acidjs-rating-stars\">"+
                        ""+
                            "<input type=\"radio\" name=\"group-rev\" id=\"group-rev-0\" value=\"5\" /><label for=\"group-rev-0\"></label>"+
                            "<input type=\"radio\" name=\"group-rev\" id=\"group-rev-1\" value=\"4\" /><label for=\"group-rev-1\"></label>"+
                            "<input type=\"radio\" name=\"group-rev\" id=\"group-rev-2\" value=\"3\" /><label for=\"group-rev-2\"></label>"+
                            "<input type=\"radio\" name=\"group-rev\" id=\"group-rev-3\" value=\"2\" /><label for=\"group-rev-3\"></label>"+
                            "<input type=\"radio\" name=\"group-rev\" id=\"group-rev-4\"  value=\"1\" /><label for=\"group-rev-4\"></label>"+
                        "</div><br><br>"
                    
                    + "Title: <input type=\"text\" id=\"title\" class=\"form-control\" name=\"title\"/><br>"
                    + "Description: <textarea class=\"form-control\" rows=\"4\" cols=\"50\" type=\"text\" name=\"description\"></textarea><br>"
                    + "Photo: <input class=\"form-control\" type=\"file\" name=\"image\"><br>"
                    + "<button class=\"btn btn-submit\" type=\"submit\">Add review</button>");
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

        out.println("<div class=\"container\"><div class=\"col-md-2\"></div><div class=\"col-md-8\">");
        
        for(int i = 0; i < reviews.size(); i++){
            int reviewer_id = manager.getReviewrId(reviews.get(i).getId());
            int review_rating = reviews.get(i).getRating();
            String reviewer_name = manager.getName(reviewer_id);
            out.println("<hr><h4>"+reviewer_name+" reviewed this restaurant:</h4>");
            int photoId = reviews.get(i).getImg();
            if(photoId != 0){
                String path = manager.getPhoto(photoId);
                out.println("<div id=\"rev-img\"><img id=\"rev-img\" src=\""+request.getContextPath()+"/"+path+"\"></div>");
                System.out.println(request.getContextPath()+"/"+path);
            }
            
            out.println("<div name=\"rating\" class=\"acidjs-rating-stars acidjs-rating-disabled\">"+
                    "<form>");
            switch (review_rating){
                case 1:
                    out.println("<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-0\" value=\"5\" /><label for=\"group-"+i+"-0\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-1\" value=\"4\" /><label for=\"group-"+i+"-1\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-2\" value=\"3\" /><label for=\"group-"+i+"-2\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-3\" value=\"2\" /><label for=\"group-"+i+"-3\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-"+i+"\" id=\"group-"+i+"-4\"  value=\"1\" /><label for=\"group-"+i+"-4\"></label>");
                    break;
                case 2:
                    out.println("<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-0\" value=\"5\" /><label for=\"group-"+i+"-0\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-1\" value=\"4\" /><label for=\"group-"+i+"-1\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-2\" value=\"3\" /><label for=\"group-"+i+"-2\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-"+i+"\" id=\"group-"+i+"-3\" value=\"2\" /><label for=\"group-"+i+"-3\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-4\"  value=\"1\" /><label for=\"group-"+i+"-4\"></label>");
                    break;
                case 3:
                    out.println("<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-0\" value=\"5\" /><label for=\"group-"+i+"-0\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-1\" value=\"4\" /><label for=\"group-"+i+"-1\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-"+i+"\" id=\"group-"+i+"-2\" value=\"3\" /><label for=\"group-"+i+"-2\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-3\" value=\"2\" /><label for=\"group-"+i+"-3\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-4\"  value=\"1\" /><label for=\"group-"+i+"-4\"></label>");
                    break;
                case 4:
                    out.println("<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-0\" value=\"5\" /><label for=\"group-"+i+"-0\"></label>"+
                    "<input type=\"radio\" checked=\"checked\" name=\"group-"+i+"\" id=\"group-"+i+"-1\" value=\"4\" /><label for=\"group-"+i+"-1\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-2\" value=\"3\" /><label for=\"group-"+i+"-2\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-3\" value=\"2\" /><label for=\"group-"+i+"-3\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-4\"  value=\"1\" /><label for=\"group-"+i+"-4\"></label>");
                    break;
                case 5:
                    out.println("<input type=\"radio\" checked=\"checked\" name=\"group-"+i+"\" id=\"group-"+i+"-0\" value=\"5\" /><label for=\"group-"+i+"-0\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-1\" value=\"4\" /><label for=\"group-"+i+"-1\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-2\" value=\"3\" /><label for=\"group-"+i+"-2\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-3\" value=\"2\" /><label for=\"group-"+i+"-3\"></label>"+
                    "<input type=\"radio\" name=\"group-"+i+"\" id=\"group-"+i+"-4\"  value=\"1\" /><label for=\"group-"+i+"-4\"></label>");
                    break;
            }
            out.println("</form></div>");
            out.println("<br><p><b>"+reviews.get(i).getTitle()+"</b></p>");
            out.println("<p>"+reviews.get(i).getDescription()+"</p>");
            String reply = manager.getReplyPerReview(reviews.get(i).getId());
            if (reply != null){
                out.println("<br><h5 id=\"reply-txt\"><b>Restaurant owner replied:</b></h5>");
                out.println("<p id=\"reply-txt\">"+reply+"</p>");
            }
            
            
        }
        out.println("</div><div class=\"col-md-2\"></div></div>");
        
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
