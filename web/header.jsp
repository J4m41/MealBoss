
        
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
                    <li><a href="#">Services</a></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Contact</a></li>
                    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a></li>
                </ul>
            </c:if>
        </nav>
        
        
        <nav class="navbar navbar-custom navbar-fixed-top">
            
            <div class="col-md-2 col-sm-1 col-xs-2">
                <div id="mobile-nav"></div>
            </div>
            <div class="col-md-8">
                <form action="ShowResults" type="post">
                    <div class="input-group" id="barra-ricerca">
                        <input type="text" id="search_bar" name="search_bar" autocomplete="true" class="form-control" placeholder="Search">

                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">Search by <span class="caret"></span></button>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li><a><input type="checkbox" checked="true" id="search_names"> Names</a></li>
                                <li><a><input type="checkbox" id="search_places"> Places</a></li>
                                <li><a><input type="checkbox" id="search_cuisines"> Cuisine types</a></li>
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