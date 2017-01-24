<%-- 
    Document   : index
    Created on : 26-set-2016, 13.05.16
    Author     : leonardo
--%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Statement"%>
<%@page import="db_classes.DBManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%!User userin; %>
        <%!String fullname; %>
        <%@page import="db_classes.User" %>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="media/js/jquery-3.1.1.js"></script>
        <script src="http://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30=" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
            integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">        
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" 
            integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" 
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="media/css/styles.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            #map {
                height: 400px;
                width: 100%;
            }
        </style>
    
    </head>
    <body>
        <% 
        Class.forName("org.postgresql.Driver", true, getClass().getClassLoader());
        DBManager manager = new DBManager( "jdbc:postgresql://localhost:5432/DBFoodSite","postgres","000117gm");
         manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
        java.sql.Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DBFoodSite","postgres","000117gm");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM COORDINATES");
        ArrayList<Double> latx = new ArrayList();
        ArrayList<Double> longx = new ArrayList();
        int dim = 0;
        while (rs.next()) {
         Double latz = rs.getDouble("latitude");
         Double longz = rs.getDouble("longitude");
         latx.add(latz);
         longx.add(longz);
         dim++;
         }
        rs = st.executeQuery("SELECT * FROM RESTAURANTS");
        ArrayList<String> nomi = new ArrayList();
        ArrayList<String> desc = new ArrayList();
        ArrayList<String> url = new ArrayList();
        while(rs.next()){
            String nome = rs.getString("name");
            String descx = rs.getString("description");
            String urlx = rs.getString("web_site_url");
            nomi.add(nome);
            desc.add(descx);
            url.add(urlx);
        }
        session.setAttribute("latx", latx);
        session.setAttribute("longx", longx);
        session.setAttribute("dim", dim);
        session.setAttribute("nomi", nomi);
        session.setAttribute("desc", desc);
        session.setAttribute("url", url);
        %>
        <nav id="nav-lato">
            <c:if test="${sessionScope.user == null}">
                <ul class="menu">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="login_page.jsp">Sign in</a></li>
                    <li><a href="signup_page.jsp">Sign up</a></li>
                    <li><a href="#">Services</a></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Contact</a></li>
                </ul>
            </c:if>
            <c:if test="${sessionScope.user != null}">
                <ul class="menu">
                    <li>Welcome back <c:out value="${sessionScope.user.firstname}"></c:out></li>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="profile_page.jsp">My profile</a></li>
                    <li><a href="#">My notifications</a></li>
                    <li><a href="#">My restaurants</a></li>
                    <li><a href="addRestaurant_page.jsp">Add restaurant</a></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Contact</a></li>
                    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a></li>
                </ul>
            </c:if>
        </nav>
        
        
        <nav class="navbar navbar-custom navbar-fixed-top">
            
            <div class="col-md-2">
                <div id="mobile-nav"></div>
            </div>
            <div class="col-md-8">
                <form action="ShowResults" type="post">
                    <div class="input-group" id="barra-ricerca">
                        <input type="text" id="search_bar" name="search_bar" class="form-control" placeholder="Search">

                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">Advanced <span class="caret"></span></button>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </div>

                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit" formmethod="post">Search!</button>
                        </span>
                    </div>
                </form>
            </div>
            <div class="col-md-2"></div>
            
        </nav>
        <img id="bg_image" src="media/images/home_restaurant.jpg">
            <div class="jumbotron" >
                <h1>The best way to eat</h1>
            </div>
        <div id="map"></div>
         <script type="text/javascript" src="https://www.google.com/jsapi"></script>  
         
    <script>
        function getCoords(geocoder, address)
        {
         alert("ENTRATI2");
        geocoder.geocode({'address': address}, function(results, status) {
          if (status === 'OK') {
            alert("POS: " + results[0].geometry.location);
          } else {
            alert('Geocode was not successful for the following reason: ' + status);
          }
        });
        }
    
       function initMap() {
        var address = "104 Via della Cervara TN Italy, 38121";
        var geocoder = new google.maps.Geocoder();
        getCoords(geocoder,address);
        var desc = [
        <c:forEach var="p" items="${sessionScope.desc}" varStatus="status">
          ${status.first ? '' : ','} "${p}"
           </c:forEach>
        ];
        var url = [
        <c:forEach var="p" items="${sessionScope.url}" varStatus="status">
          ${status.first ? '' : ','} "${p}"
           </c:forEach>
        ];
        var latt = [
        <c:forEach var="p" items="${sessionScope.latx}" varStatus="status">
          ${status.first ? '' : ','} "${p}"
           </c:forEach>
        ];
        var logg = [
        <c:forEach var="p" items="${sessionScope.longx}" varStatus="status">
          ${status.first ? '' : ','} "${p}"
           </c:forEach>
        ];
        var dim = '<%=dim%>';
        //pos utente !! MANCANTE !!
        var posx = {lat: 46.06941967  , lng: 11.12015963};

        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 10,
          center: posx,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        });
         
     var infowindow = new google.maps.InfoWindow();
     
     for(var i=0; i<dim; i++) { 
      var marker = new google.maps.Marker({
        position: new google.maps.LatLng(latt[i], logg[i]),
        map: map
      });
      google.maps.event.addListener(marker, 'click', (function(marker, i) {
        return function() {
  
          infowindow.setContent(desc[i] + "\n" + url[i]);
          infowindow.open(map, marker);
         };
      })(marker, i));
    }
         
        
    }
    
    </script>
          <script async defer type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCLIV2YvvxU-PmpT9MBrApqPx8oDmqcpXs&callback=initMap"></script>
        

        <script src="media/js/scripts.js"></script>
    </body>
</html>
