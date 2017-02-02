/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import db_classes.DBManager;
import db_classes.Restaurant;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Web application lifecycle listener.
 *
 * @author gianma
 */
@WebListener()
public class EatItHereAppListener implements ServletContextListener {
    
    private DBManager manager;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dburl = sce.getServletContext().getInitParameter("dburl");
        String dbuser = sce.getServletContext().getInitParameter("dbuser");
        String dbpassword = sce.getServletContext().getInitParameter("dbpassword");
        try{
            manager = new DBManager(dburl, dbuser, dbpassword);
            sce.getServletContext().setAttribute("dbmanager", manager);
        } catch (SQLException ex){
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBManager.shutdown();
    }
}