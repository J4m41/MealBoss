<%-- 
    Document   : photo_segnalation
    Created on : 2-feb-2017, 16.56.35
    Author     : leonardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <%!User userin; %>
        <%!String fullname; %>
        <%@page import="db_classes.User" %>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="media/css/styles.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="media/js/autoCompelte.js"></script>
        
        <title>Photo Report</title>
    </head>
    <body>
        <div class="row">
            <div class="col-md-4 col-md-offspan-2">
                <h2>Do you like the photo ?</h2>
                <%request.getSession().setAttribute("id", request.getParameter("id") );%>
                <h3><a href="UserProfile">Yes</a>
                    <a href="SegnalaFoto">No</a></h3>
                
            </div>
        </div>
    </body>
</html>
