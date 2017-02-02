<%-- 
    Document   : pwchange_page
    Created on : 21-ott-2016, 13.01.40
    Author     : gianma
--%>

<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="com.nimbusds.jose.JOSEException"%>
<%@page import="com.nimbusds.jose.crypto.MACVerifier"%>
<%@page import="java.util.logging.Level"%>
<%@page import="com.nimbusds.jose.JWSVerifier"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="com.nimbusds.jwt.SignedJWT"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>    
    <head>
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
    </head>
    
    <%!
    public String parseJWT(String jwt) throws NoSuchAlgorithmException, ParseException, JOSEException, UnsupportedEncodingException{
        String username = null;
        
        SignedJWT sJWT = null;
        
        sJWT = SignedJWT.parse(jwt);
            
        JWSVerifier hmacVerifier = null;
        
        byte [] bytes = new byte[32];
        bytes = MessageDigest.getInstance("SHA-256").digest("secret".getBytes("UTF-8"));
        hmacVerifier = new MACVerifier(bytes);
        
        if (sJWT.verify(hmacVerifier)){
            username = sJWT.getJWTClaimsSet().getSubject();
        }
                
        return username;
    } 
    %>
    <% String token = request.getParameter("token"); %>
    
    <body>
        <nav id="nav-lato">
            <c:if test="${sessionScope.user == null}">
                <ul class="menu">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="login_page.jsp">Sign in</a></li>
                    <li><a href="signup_page.jsp">Sign up</a></li>
                    <li><a href="about_page.jsp">About</a></li>
                </ul>
            </c:if>
            <c:if test="${sessionScope.user != null}">
                <ul class="menu">
                    <li><p> Welcome back <c:out value="${sessionScope.user.firstname}"></c:out><p></li>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="UserProfile">My profile</a></li>
                    <li><a href="#">My restaurants</a></li>
                    <li><a href="addRestaurant_page.jsp">Add restaurant</a></li>
                    <li><a href="about_page.jsp">About</a></li>
                    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a></li>
                </ul>
            </c:if>
        </nav>

        
        <nav class="navbar navbar-custom navbar-fixed-top">
            
            <div class="col-md-2 col-sm-1 col-xs-2">
                <div id="mobile-nav"></div>
            </div>
            <div class="col-md-8 col-sm-4 col-xs-8">
                <form action="ShowResults" type="post">
                    <div class="input-group" id="barra-ricerca">
                        <input type="text" id="search_bar" name="search_bar" class="form-control" placeholder="Search">

                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">Search by <span class="caret"></span></button>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li><a><input type="checkbox" checked="true" name="search_names" value="true" id="search_names"> Names</a></li>
                                <li><a><input type="checkbox" name="search_places" value="true" id="search_places"> Places</a></li>
                                <li><a><input type="checkbox" name="search_cuisines" value="true" id="search_cuisines"> Cuisine types</a></li>
                            </ul>
                        </div>

                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit" formmethod="post">Search!</button>
                        </span>
                    </div>
                </form>
            </div>
            <div class="col-md-2"><a href="index.jsp"><img id="icon" src="<%=request.getContextPath()%>/media/images/MealBoss_icon.png" alt="icon"></a></div>
            
        </nav>
        
        
        <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-4">
                <div class="container-fluid">
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h3 class="panel-heading">Password Reset</h3>
                        </div>
                        <div class="panel-body">
                            <form action="PwChangeServlet" method="POST">
                                <div class="container-fluid">
                                    
                                            <c:if test="${sessionScope.user == null}">
                                                <input type="hidden" id="username" name="username" class="form-control" value="<%=parseJWT(token)%>">
                                            </c:if>
                                            <c:if test="${sessionScope.user != null}">
                                                <input type="hidden" id="username" name="username" class="form-control" value="<c:out value="${sessionScope.user.username}"></c:out>">
                                            </c:if>    
                                    
                                    <div class="form-group">
                                        <div class="row">
                                            <label for="password1">Nuova password:</label>
                                            <input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
                                               title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
                                               id="password1" name="password1" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <label for="password2">Conferma password:</label>
                                            <input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
                                               title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
                                               id="password2" name="password2" class="form-control">
                                        </div>
                                        <c:out value="${requestScope.message}"></c:out>
                                    </div>
                                    <br>
                                    <div class="form-group">
                                        <div class="row">
                                            <button class="btn btn-danger" type="submit">Submit</button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4"></div>
        </div>
        

        <script src="media/js/scripts.js"></script>

    </body>
</html>