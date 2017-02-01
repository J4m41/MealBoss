/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_classes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import info.debatty.java.stringsimilarity.*;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gianma
 */
public class DBManager implements Serializable {
    
    private transient Connection con;
    
    public DBManager(String dburl, String dbuser, String dbpassword) throws SQLException{
        try{
            Class.forName("org.postgresql.Driver", true, getClass().getClassLoader());
        } catch(Exception e){
            throw new RuntimeException(e.toString(), e);
        }
        
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);
        this.con = con;
    }
    
    public static void shutdown(){
        try{
            DriverManager.getConnection("jdbc:postgresql:;shutdown=true");
        } catch (SQLException ex){
            Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }
    
    /**
    *Autentica un utente in base ad un nome utente e una password
    *@param username: il nome utente
    *@param password: la password
    *@return: null se l'utente non è autenticato, un oggetto User se l'utente esiste ed è autenticato
     * @throws java.sql.SQLException
    */
    
    public User authenticate(String username, String password) throws SQLException{
        
        PreparedStatement stm = con.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
        
        try{
            stm.setString(1, username);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            
            try{
                if (rs.next()){
                    User user = new User();
                    user.setUsername(username);
                    user.setId(rs.getInt("id"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    user.setUsertype(rs.getString("user_type"));
                    
                    return user;
                    
                }else{
                    return null;
                }
            } finally{
                //Ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally
                rs.close();
            }
        } finally{
            //Ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally
            stm.close();
        }
    }
    /**
     * 
     * @param firstname dati registrazione
     * @param lastname
     * @param username
     * @param password
     * @return true se registrazione andata a buon fine, false altrimenti
     * @throws SQLException 
     */
    public boolean register(String firstname, String lastname, String username, String password) throws SQLException{
        
        
        int next_id = 0;
        String query1 = "SELECT MAX(id) FROM Users";
        PreparedStatement ps1 = con.prepareStatement(query1);
        ResultSet rs1 = ps1.executeQuery();
        while(rs1.next()){
            next_id = rs1.getInt(1) + 1;
        }
        
        String query = "INSERT INTO Users VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, next_id);
        ps.setString(2, firstname);
        ps.setString(3, lastname);
        ps.setString(4, "U");
        ps.setString(5, username);
        ps.setString(6, password);
        
        try{
            ps.executeUpdate();
        }finally{
            ps.close();
        }
        return true;
    }
    /**
     * 
     * @param username nome da verificare se già presente
     * @return User se presente, null altrimenti
     * @throws SQLException 
     */
    public User existingUser(String username) throws SQLException{
        
        String query = "SELECT * FROM Users WHERE username = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, username);
        
        
        
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setUsername(username);
                user.setId(rs.getInt("id"));
                user.setUsertype(rs.getString("user_type"));
                return user;
            }
            rs.close();
        }finally{
            
            ps.close();
        }
        
        return null;
        
        
    }
    
    public boolean badPassword(String pw1, String pw2){
        
        if(!pw1.equals(pw2)){
            return true;
        }
        
        return false;  
    }
    
    /**
     * @param search il nome del ristorante da cercare
     * @return Un singolo ristorante che corrisponde al nome cercato
     * @throws java.sql.SQLException
     */
    public Restaurant getRestaurant(String search) throws SQLException{
        
        Restaurant tmp = new Restaurant();
        
        String query = "SELECT * FROM restaurants WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, search);
        
                
        try{
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                
                tmp.setId(rs.getInt("id"));
                //setto il nome
                tmp.setName(rs.getString("name"));
                //setto indirizzo, civico e città
                tmp.setAddress(rs.getString("address"));
                tmp.setCivicNumber(rs.getInt("civic"));
                tmp.setCity(rs.getString("city"));
                //setto owner
                tmp.setId_owner(rs.getInt("id_owner"));
                //setto la descrizione
                tmp.setDescription(rs.getString("description"));
                //setto il website
                tmp.setWebSiteUrl(rs.getString("web_site_url"));
                //setto price range
                tmp.setPrice(rs.getInt("price_range"));
                //qua la query per prendere il tipo di cucina
                PreparedStatement pst = con.prepareStatement("SELECT c.name "
                    + "FROM cuisines AS c INNER JOIN restaurant_cuisine AS rc "
                    + "ON c.id = rc.id_cuisine WHERE rc.id_restaurant = ?");
                pst.setInt(1, tmp.getId());
                ResultSet rst = pst.executeQuery();
                ArrayList <String> cuisines = new ArrayList<>();
                while(rst.next()){
                    cuisines.add(rst.getString("name"));
                }
                
                String [] restaurantCuisines = new String[cuisines.size()];
                for(int h = 0; h < cuisines.size(); h++){
                    restaurantCuisines[h] = cuisines.get(h);
                }
                tmp.setCuisineTypes(restaurantCuisines);
                
                //orari ristorante
                PreparedStatement psh = con.prepareStatement("SELECT * FROM opening_hours_restaurants "
                        + "WHERE id_restaurant = ?");
                psh.setInt(1, tmp.getId());
                ResultSet rsh = psh.executeQuery();
                WeekSchedule tmp_week = new WeekSchedule();
                while(rsh.next()){
                    if(rsh.getInt("day_of_the_week") == 1){
                        tmp_week.setMonday(true);
                        tmp_week.setMonday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setMonday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setMonday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setMonday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 2){
                        tmp_week.setTuesday(true);
                        tmp_week.setTuesday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setTuesday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setTuesday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setTuesday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 3){
                        tmp_week.setWednesday(true);
                        tmp_week.setWednesday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setWednesday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setWednesday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setWednesday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 4){
                        tmp_week.setThursday(true);
                        tmp_week.setThursday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setThursday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setThursday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setThursday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 5){
                        tmp_week.setFriday(true);
                        tmp_week.setFriday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setFriday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setFriday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setFriday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 6){
                        tmp_week.setSaturday(true);
                        tmp_week.setSaturday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setSaturday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setSaturday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setSaturday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                    if(rsh.getInt("day_of_the_week") == 7){
                        tmp_week.setSunday(true);
                        tmp_week.setSunday_l_op(rsh.getTime("start_hour_lunch"));
                        tmp_week.setSunday_l_cl(rsh.getTime("end_hour_lunch"));
                        tmp_week.setSunday_d_op(rsh.getTime("start_hour_dinner"));
                        tmp_week.setSunday_d_cl(rsh.getTime("end_hour_dinner"));
                    }
                }
                tmp.setWeek(tmp_week);
                
                //Foto ristorante
                query = "SELECT path FROM photos WHERE id_restaurant = ?";
                PreparedStatement psp = con.prepareStatement(query);
                psp.setInt(1, rs.getInt("id"));
                ResultSet rsp = psp.executeQuery();
                if(rsp.next()){
                    tmp.setSinglePhotoPath(rsp.getString("path"));
                }
                rst.close();
                pst.close();  
                
            }
            rs.close();
        }finally{
            
            ps.close();
        }
        
        
        return tmp;
        
    }
    
        //chiamata quando si effettua un commento
    public int addReviewPerRestaurant(int user,int restaurant,int rating,Timestamp ora,String title,String description,int img) throws SQLException{
        System.out.println("sono entrato nela query");
        String query = "INSERT INTO reviews(id,title,global_value,description,date_creation,id_restaurant,id_creator,id_photo,likes) "
                + "VALUES (?,?,?,?,?,?,?,?,?);";
        PreparedStatement ps = con.prepareStatement(query);
        int next_id = 0;
        try{
            //query to get the next free id for restaurant
            
            String query1 = "SELECT MAX(id) FROM REVIEWS";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                next_id = rs1.getInt(1) + 1;
            }
            rs1.close();
            System.out.println("sto settando query");
            
            ps.setInt(1,next_id);
            ps.setString(2,title);
            ps.setInt(3,3);
            ps.setString(4,description);

            ps.setTimestamp(5, ora);

            ps.setInt(6,restaurant);
            ps.setInt(7,user);
            ps.setInt(8,1);
            ps.setInt(9,0);
            try{
            ResultSet result = ps.executeQuery();
            }catch(SQLException e){
                System.out.println(e.toString());
            }
            ps.close();
        }catch(Exception e){
        }
        return next_id;
        
    }
    
    //ritorna recensioni
    public ArrayList<Review> getReviewPerRestaurant(String restaurant) throws SQLException{
        
        ArrayList<Review> restaurantReviews = new ArrayList<>();
        
        String query1 = "SELECT id FROM RESTAURANTS WHERE name = ?";
        PreparedStatement ps1 = con.prepareStatement(query1);
        ps1.setString(1, restaurant);
        ResultSet rs1 = ps1.executeQuery();
        rs1.next();
        int restId = rs1.getInt(1);
        rs1.close();
        ps1.close();
        
        String query = "SELECT * FROM REVIEWS WHERE id_restaurant = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, restId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Review rev = new Review();
            rev.setId(rs.getInt(1));
            rev.setData(rs.getDate(5));
            rev.setDescription(rs.getString(4));
            rev.setRating(rs.getInt(3));
            rev.setRestaurant(rs.getInt(6));
            rev.setTitle(rs.getString(2));
            rev.setUser(rs.getInt(7));
            rev.setImg(rs.getInt(8));
            restaurantReviews.add(rev);
            //rs.next();
        }
        
        return restaurantReviews;
        
    }
    public void updateReviewLikes(int revId, int value) throws SQLException{
        
        String qquery = "SELECT likes FROM reviews WHERE id = ?";
        PreparedStatement psdb = con.prepareStatement(qquery);
        psdb.setInt(1,revId);
        System.out.println("debug +++++ "+value);
        ResultSet rss = psdb.executeQuery();
        int like = 0;
        if(rss.next())
             like = rss.getInt(1);
        
        if(value==1)
            like+=1;
        else
            like-=1;
        System.out.println("likes attuali: "+like);
        String query = "UPDATE reviews SET likes = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, like);
        ps.setInt(2, revId);
        
        try{
            int rs = ps.executeUpdate();
            System.out.print(rs);
        }catch(SQLException e){
            System.out.println(e.toString());
        }
        
    }
    //l' user ha fatto un azione, idNotified è colui che riceve la notifica
    public void notifyUser(int user,int idNotified,int idReview,int type) throws SQLException{
        
        String insertQuery = "INSERT INTO notifications(id,id_notifier,id_notified,type,description,id_review,id_photo) "+
                "VALUES (?,?,?,?,?,?,?);";
        PreparedStatement ps = con.prepareStatement(insertQuery);
        
        int next_id = 0;
        String query1 = "SELECT MAX(id) FROM NOTIFICATIONS";
        PreparedStatement ps1 = con.prepareStatement(query1);
        ResultSet rs1 = ps1.executeQuery();
        while(rs1.next()){
            next_id = rs1.getInt(1) + 1;
        }
        rs1.close();
        
        ps.setInt(1, next_id);
        ps.setInt(2, user);
        ps.setInt(3, idNotified);
        ps.setInt(4, type);
        ps.setInt(6,idReview);

        switch(type){
                case 0 :    //Recensione
                    ps.setString(5, "a user reviewed your restaurant, id review: "+idReview);
                    ps.setInt(7, 0);
                    break;
                case 1:     //Recensione + foto
                    ps.setString(5, "a user reviewed your restaurant with a photo, id review: "+idReview);
                    String queryPhoto = "SELECT id_photo FROM reviews WHERE id = ?";
                    PreparedStatement pss = con.prepareStatement(queryPhoto);
                    pss.setInt(1, idReview);
                    ResultSet rs = pss.executeQuery();
                    if(rs.next()){
                        ps.setInt(7, rs.getInt(1));
                    }
                    break;
                case 2:     //segnalazione foto
                    ps.setString(5, "a restaurant's owner disliker you photo, id review: "+idReview);
                    String queryPhoto1 = "SELECT id_photo FROM reviews WHERE id = ?";
                    PreparedStatement pss1 = con.prepareStatement(queryPhoto1);
                    pss1.setInt(1, idReview);
                    ResultSet rs11 = pss1.executeQuery();
                    if(rs11.next()){
                        ps.setInt(7, rs11.getInt(1));
                    }
                    break;
                case 3:     //Risposta a recensione
                    ps.setString(5, "a restourant's owner replied to your review n°: "+idReview);
                    ps.setInt(7, 0);
                    break;
                case 4:     //like a recensione
                    
                    ps.setString(5, "your review recived a like: "+idReview);
                    ps.setInt(7, 0);
                    break;
        }
        try{
        ResultSet rs = ps.executeQuery();
        }catch(SQLException e){
            System.out.println("wow   "+e.toString());
        }
    };
    
    public int getOwnerId(int restaurant) throws SQLException{
        int id = 0;
        String query = "SELECT id_owner FROM restaurants WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, restaurant);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            id = rs.getInt(1);
        return id;
    };
    
    public int getReviewrId(int revId) throws SQLException{
        int id = 0;
        String query = "SELECT id_creator FROM reviews WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, revId);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            id = rs.getInt(1);
        return id;
    };
    
    public ArrayList<Notification> getNotificationPerUser(int user) throws SQLException{
        
        ArrayList<Notification> notifiche = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE id_notified = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Notification tmp = new Notification();
            tmp.setId(rs.getInt(1));
            tmp.setNotifier_id(rs.getInt(2));
            tmp.setNotified_id(rs.getInt(3));
            System.out.println("getNotificationPerUser: notifierID = "+rs.getInt(2)+ "notifiedID = "+rs.getInt(3));
            System.out.println("getNotificationPerUser**: notifierID = "+tmp.getNotifier_id()+ "notifiedID = "+tmp.getNotified_id());
            tmp.setType(rs.getInt(4));
            tmp.setDescription(rs.getString(5));
            tmp.setReview_id(rs.getInt(6));
            tmp.setPhoto_id(rs.getInt(7));
            notifiche.add(tmp);
        }
        return notifiche;
    
    }
    public String getUsernameFromId(int id) throws SQLException{
        String username = null;
        String query = "SELECT username FROM users WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            username = rs.getString(1);
        return username;
    }
    public void validateNotification(int id) throws SQLException{
        String query = "UPDATE notifications SET validated = true WHERE id= ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        int rs = ps.executeUpdate();
        System.out.println("Righe affette: "+rs);
    }
    /**
     *
     * @param target
     * @param byName
     * @param byPlace
     * @param byCuisine
     * @return
     * @throws java.sql.SQLException
     */
    public ArrayList <Restaurant> searchRestaurants(String target, boolean byName, boolean byPlace, boolean byCuisine) throws SQLException{
        
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        ArrayList <Restaurant> allResults = new ArrayList<>();
        
        //Query to get all restaurants and their data
        PreparedStatement ps1 = con.prepareStatement("SELECT * FROM restaurants");
        ResultSet rs1 = ps1.executeQuery();
        
        while(rs1.next()){
            Restaurant restaurant = new Restaurant();
            //Restaurant data
            restaurant.setId(rs1.getInt("id"));
            restaurant.setName(rs1.getString("name"));
            restaurant.setAddress(rs1.getString("address"));
            restaurant.setCity(rs1.getString("city"));
            restaurant.setCivicNumber(rs1.getInt("civic"));
            restaurant.setDescription(rs1.getString("description"));
            restaurant.setWebSiteUrl(rs1.getString("web_site_url"));
            restaurant.setGlobal_value(rs1.getDouble("global_value"));
            restaurant.setPrice(rs1.getInt("price_range"));
            restaurant.setId_owner(rs1.getInt("id_owner"));
            
            //Restaurant cuisines
            PreparedStatement ps2 = con.prepareStatement("SELECT c.name "
                    + "FROM cuisines AS c INNER JOIN restaurant_cuisine AS rc "
                    + "ON c.id = rc.id_cuisine WHERE rc.id_restaurant = ?");
            ps2.setInt(1, rs1.getInt("id"));
            ResultSet rs2 = ps2.executeQuery();
            ArrayList <String> cuisines = new ArrayList<>();
            while(rs2.next()){
                cuisines.add(rs2.getString("name"));
            }
            
            String [] restaurantCuisines = new String[cuisines.size()];
            for(int h = 0; h < cuisines.size(); h++){
                restaurantCuisines[h] = cuisines.get(h);
            }
            restaurant.setCuisineTypes(restaurantCuisines);
            
            
            //The data collected is enough since user can search by names, places and cuisines
            //Need to add also photo paths for the results
            PreparedStatement ps3 = con.prepareStatement("SELECT path FROM photos WHERE id_restaurant = ?");
            ps3.setInt(1, rs1.getInt("id"));
            ResultSet rs3 = ps3.executeQuery();
            ArrayList <String> paths = new ArrayList<>();
            while(rs3.next()){
                paths.add(rs3.getString("path"));
            }
            String [] restaurantPaths = new String [paths.size()];
            for(int n = 0; n < paths.size(); n++){
                restaurantPaths[n] = paths.get(n);
            }
            restaurant.setPhotoPath(restaurantPaths);
            
            allResults.add(restaurant);
        }
        
        ArrayList <Restaurant> finalResults = new ArrayList<>();
        
        //From here we select the restaurants based on the user search params
        if(byName){
            for(int i = 0; i < allResults.size(); i++){
                int distance = (int)(lcs.distance(target, allResults.get(i).getName()))/2;
                if(distance <= 2){
                    if(!finalResults.contains(allResults.get(i))){
                        finalResults.add(allResults.get(i));
                    }
                }
                
            }
        }
        if(byPlace){
            for(int j = 0; j < allResults.size(); j++){
                int distance_city = (int)(lcs.distance(target, allResults.get(j).getCity()));
                int distance_address = (int)(lcs.distance(target, allResults.get(j).getAddress()+" "+allResults.get(j).getCivicNumber()+" "+allResults.get(j).getCity()))/2;
                if(distance_address <= 2 || distance_city <= 2){
                    System.out.println(j);
                    if(!finalResults.contains(allResults.get(j))){
                        finalResults.add(allResults.get(j));
                    }
                }
                
            }
        }
        if(byCuisine){
            for(int k = 0; k < allResults.size(); k++){
                String cuisines [] = allResults.get(k).getCuisineTypes();
                for(int l = 0; l < cuisines.length; l++){
                    int distance = (int)(lcs.distance(target, cuisines[l]))/2;
                    if( distance <= 2){
                        if(!finalResults.contains(allResults.get(k))){
                            finalResults.add(allResults.get(k));
                        }
                    }
                }
            }
        }
        
        return finalResults;
    }
    
    public int getRestaurantId(String name) throws SQLException{
        int id = 0;
        String query = "SELECT id FROM restaurants WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            id = rs.getInt(1);
        return id;
    }
    
    /**
     *
     * @param restaurant that needs to be added to DB
     * @param creator_id
     * @param isOwner
     * @return
     */
    public boolean addRestaurant(Restaurant restaurant, int creator_id, boolean isOwner){
        
        int next_id = 0;
        float [] coordinates = null;
        
        try {
            //query to get the next free id for restaurant
            String query1 = "SELECT MAX(id) FROM Restaurants";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                next_id = rs1.getInt(1) + 1;
            }
            
            //query to add the restaurant to DB
            String query = "INSERT INTO Restaurants VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            
            ps.setInt(1, next_id);
            ps.setString(2, restaurant.getName());
            ps.setString(3, restaurant.getAddress());
            ps.setInt(4, restaurant.getCivicNumber());
            ps.setString(5, restaurant.getCity());
            ps.setString(6, restaurant.getDescription());
            ps.setString(7, restaurant.getWebSiteUrl());
            ps.setInt(8, 0);
            ps.setInt(9, restaurant.getPrice());
            if(isOwner){
                ps.setInt(10, creator_id);
            }else{
                ps.setInt(10, 0);
            }
            ps.setInt(11, creator_id);
            
            int update = ps.executeUpdate();
            
            //query to add cuisine types to a restaurant_id
            PreparedStatement psk = con.prepareStatement("INSERT INTO restaurant_cuisine VALUES (?,?)");
            for(String s : restaurant.getCuisineTypes()){
                switch (s){
                    case "Italiana":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 1);
                        psk.executeUpdate();
                        break;
                    case "Asiatica":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 2);
                        psk.executeUpdate();
                        break;
                    case "NordAmericana":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 3);
                        psk.executeUpdate();
                        break;
                    case "Africana":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 4);
                        psk.executeUpdate();
                        break;
                    case "Caraibica":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 5);
                        psk.executeUpdate();
                        break;
                    case "SudAmericana":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 6);
                        psk.executeUpdate();
                        break;
                    case "NordEuropea":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 7);
                        psk.executeUpdate();
                        break;
                    case "Mediterranea":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 8);
                        psk.executeUpdate();
                        break;
                    case "MedioOrientale":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 9);
                        psk.executeUpdate();
                        break;
                    case "Vegana":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 10);
                        psk.executeUpdate();
                        break;
                    case "FastFood":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 11);
                        psk.executeUpdate();
                        break;
                    case "Pizzeria":
                        psk.setInt(1, next_id);
                        psk.setInt(2, 12);
                        psk.executeUpdate();
                        break;
                }
            }
            
            //query to add hours_ranges
            PreparedStatement psw = con.prepareStatement("INSERT INTO opening_hours_restaurants VALUES (?,?,?,?,?,?)");
            WeekSchedule rest_week = restaurant.getWeek();
            if(rest_week.isMonday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 1);
                psw.setTime(3, rest_week.getMonday_l_op());
                psw.setTime(4, rest_week.getMonday_l_cl());
                psw.setTime(5, rest_week.getMonday_d_op());
                psw.setTime(6, rest_week.getMonday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isTuesday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 2);
                psw.setTime(3, rest_week.getTuesday_l_op());
                psw.setTime(4, rest_week.getTuesday_l_cl());
                psw.setTime(5, rest_week.getTuesday_d_op());
                psw.setTime(6, rest_week.getTuesday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isWednesday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 3);
                psw.setTime(3, rest_week.getWednesday_l_op());
                psw.setTime(4, rest_week.getWednesday_l_cl());
                psw.setTime(5, rest_week.getWednesday_d_op());
                psw.setTime(6, rest_week.getWednesday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isThursday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 4);
                psw.setTime(3, rest_week.getThursday_l_op());
                psw.setTime(4, rest_week.getThursday_l_cl());
                psw.setTime(5, rest_week.getThursday_d_op());
                psw.setTime(6, rest_week.getThursday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isFriday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 5);
                psw.setTime(3, rest_week.getFriday_l_op());
                psw.setTime(4, rest_week.getFriday_l_cl());
                psw.setTime(5, rest_week.getFriday_d_op());
                psw.setTime(6, rest_week.getFriday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isSaturday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 6);
                psw.setTime(3, rest_week.getSaturday_l_op());
                psw.setTime(4, rest_week.getSaturday_l_cl());
                psw.setTime(5, rest_week.getSaturday_d_op());
                psw.setTime(6, rest_week.getSaturday_d_cl());
                psw.executeUpdate();
            }
            if(rest_week.isSunday()){
                psw.setInt(1, next_id);
                psw.setInt(2, 7);
                psw.setTime(3, rest_week.getSunday_l_op());
                psw.setTime(4, rest_week.getSunday_l_cl());
                psw.setTime(5, rest_week.getSunday_d_op());
                psw.setTime(6, rest_week.getSunday_d_cl());
                psw.executeUpdate();
            }
            
            //query to get the next photo id
            int next_photo_id = 0;
            PreparedStatement psn = con.prepareStatement("SELECT MAX(id) FROM photos");
            ResultSet rsp = psn.executeQuery();
            while(rsp.next()){
                next_photo_id = rsp.getInt(1)+1;
            }
            
            //query to insert photo
            PreparedStatement psp = con.prepareStatement("INSERT INTO photos VALUES (?,?,?,?)");
            psp.setInt(1, next_photo_id);
            psp.setString(2, restaurant.getSinglePhotoPath());
            psp.setInt(3, next_id);
            psp.setInt(4, creator_id);
            
            psp.executeUpdate();
            
            //query to insert coordinates
            try {
                coordinates = getCoordinates(restaurant.getAddress(), restaurant.getCivicNumber(), restaurant.getCity());
            } catch (IOException | ParseException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            PreparedStatement psc = con.prepareStatement("INSERT INTO coordinates VALUES (?,?,?)");
            psc.setInt(1, next_id);
            psc.setFloat(2, coordinates[0]);
            psc.setFloat(3, coordinates[1]);
            psc.executeUpdate();
            
            if(update == 0){
                return false;
            }
            
            ps1.close();
            rs1.close();
            ps.close();
            psk.close();
            psw.close();
            psp.close();
            psc.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JSONParser parser = new JSONParser();
        try {
            //Reading suggestions.json and gettin restaurants array
            JSONObject json = (JSONObject) parser.parse(new FileReader("/home/gianma/NetBeansProjects/MealBoss/web/media/js/suggestions.json"));
            JSONArray restaurants = (JSONArray) json.get("restaurants");
            
            JSONObject tba_restaurant = new JSONObject();
            tba_restaurant.put("name", restaurant.getName());
            tba_restaurant.put("place", restaurant.getCity());
            JSONObject tba_coords = new JSONObject();
            tba_coords.put("lat", coordinates[0]);
            tba_coords.put("lng", coordinates[1]);
            tba_restaurant.put("coords", tba_coords);
            
            restaurants.add(tba_restaurant);
            
            FileWriter file = new FileWriter("/home/gianma/NetBeansProjects/MealBoss/web/media/js/suggestions.json");
            file.write(json.toJSONString());
            file.flush();
            file.close();
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return true;
    }
    
    /**
     *
     * @param username
     * @param password the new password
     * @return true/false depending on success/fail
     */
    public boolean changePWQuery(String username, String password){
        int update = 0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
            
            ps.setString(1, password);
            ps.setString(2, username);
            
            update = ps.executeUpdate();
            
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (update != 0);
    }
    
    /**
     *
     * @param address
     * @param civic
     * @param city
     * @return
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     */
    public float[] getCoordinates(String address, int civic, String city) throws IOException, ParseException{
        String url1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String apikey = "&key=AIzaSyDORr3b9qWZfsw4Scy6BFUpkk1EXgw_DJw";
        float [] coordinates = new float [2];
        String fulladdress = address+" "+civic+" "+city;
        
        URL url = new URL(url1 + URLEncoder.encode(fulladdress, "UTF-8") + apikey + "&sensor=false");
        URLConnection conn = url.openConnection();
        
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        IOUtils.copy(conn.getInputStream(), output);
        
        String json = output.toString();
        
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject root = (JSONObject) obj;
        
        JSONArray results = (JSONArray) root.get("results");
        JSONObject obj1 = (JSONObject) results.get(0);
        JSONObject geometry = (JSONObject) obj1.get("geometry");
        JSONObject location = (JSONObject) geometry.get("location");
        
        Double latitude = (double) location.get("lat");
        Double longitude = (double) location.get("lng");
        
        coordinates[0] = latitude.floatValue();
        coordinates[1] = longitude.floatValue();
        
        return coordinates;
    }
    
}
