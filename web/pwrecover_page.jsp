<%-- 
    Document   : pwrecover_page
    Created on : 11-ott-2016, 14.34.43
    Author     : gianma
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
                    <li><a href="MyRestaurants">My restaurants</a></li>
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
                <form action="showResults" type="post">
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
        
        <div class="col-md-3"></div>
        
        <div class="col-md-6">
            <div class="container-fluid">
                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-heading">Password Recovery</h3>
                    </div>
                    <div class="panel-body">
                        <form action="PwRecoveryServlet" method="POST">
                            <h5>Follow these steps to easily recover your password:</h5>
                            <ol>
                                <li>Insert your email;</li>
                                <li>On submit, a verification email will be sent to your address;</li>
                                <li>Click on the link you received to change your password.</li>
                            </ol>
                            <div class="container-fluid">
                                <div class="form-group">
                                    <div class="row">
                                        <label for="email">Email:</label>
                                        <input type="email" id="email" name="username" class="form-control" placeholder="email">
                                    </div>
                                    <p><c:out value="${requestScope.message}"></c:out></p>
                                </div>
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
        
        <div class="col-md-3"></div>
        
        <script src="media/js/scripts.js"></script>
    </body>
</html>