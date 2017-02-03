/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db_classes.DBManager;
import db_classes.User;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
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
    private String dirName;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
        
        //reading the upload directory from web.xml parameters
        this.dirName = getInitParameter("uploadDir");
        if (dirName == null){
            throw new ServletException("Please provide uploadDir parameter");
        }
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
        
        User user = (User) request.getSession().getAttribute("user");

        String title = null;
        String description = null;
        Calendar calendar = Calendar.getInstance();
        

        
        String restName = (String)request.getSession().getAttribute("RestName");
        int restaurantId = manager.getRestaurantId(restName);
        int restaurantOwner = manager.getOwnerId(restaurantId);
        
        String photoPath = null;
        //Adding photo if present
        try{
            MultipartRequest multi = new MultipartRequest(request, dirName, 10*1024*1024,
                        "ISO-8859-1", new DefaultFileRenamePolicy());
            int rating = 0;
            title = multi.getParameter("title");
            description = multi.getParameter("description");
            rating = Integer.parseInt(multi.getParameter("group-rev"));

            Enumeration files = multi.getFileNames();
            while(files.hasMoreElements()){
                String name = (String)files.nextElement();
                File f = multi.getFile(name);
                if(f !=  null && name.equals("image") && f.exists()){
                    photoPath = f.toString();
                }
            }
            
            int photoId = 0;
            if(photoPath != null){
                photoId = manager.addPhoto(photoPath, restaurantId, user.getId());
            }

            java.util.Date now = calendar.getTime();
            java.sql.Timestamp ora = new java.sql.Timestamp(now.getTime());


            //se non c'è l' immagine mando notifica senza immagine
            if(photoPath == null){
                int idReview = this.manager.addReviewPerRestaurant(user.getId(), restaurantId, rating, ora, title, description, 0);
                manager.notifyUser(user.getId(), restaurantOwner, idReview , 0);
            }else{
                int idReview = this.manager.addReviewPerRestaurant(user.getId(), restaurantId, rating, ora, title, description, photoId);
                manager.notifyUser(user.getId(), restaurantOwner, idReview , 1);
            }

            response.sendRedirect("Profile?name="+restName);

        }catch(IOException ex){
            this.getServletContext().log(ex, "Error reading or saving file");
        }
        

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
