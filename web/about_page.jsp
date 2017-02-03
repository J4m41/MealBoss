<%-- 
    Document   : index
    Created on : 26-set-2016, 13.05.16
    Author     : leonardo
--%>

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
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
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
                <form action="ShowResults" type="post">
                    <div class="input-group" id="barra-ricerca">
                        <input type="text" id="search_bar" name="search_bar" class="form-control" placeholder="Search">

                        <div class="input-group-btn">
                            <button type="button" id="searchby-btn" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">Search by <span class="caret"></span></button>
                            <ul class="dropdown-menu dropdown-menu-right">
                                <li><a><input type="checkbox" checked="true" id="search_names" name="search_names" value="true"> Names</a></li>
                                <li><a><input type="checkbox" id="search_places" name="search_places" value="true"> Places</a></li>
                                <li><a><input type="checkbox" id="search_cuisines" name="search_cuisines" value="true"> Cuisine types</a></li>
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
            <div class="col-md-2"><a href="index.jsp"><img id="icon" src="<%=request.getContextPath()%>/media/images/MealBoss_icon.png" alt="icon"></a></div>
            
        </nav>

        <div class="jumbotron" id="home-jumbo">
            <br>
            <h1>Trova qui i migliori ristoranti</h1>
            <p>Cerca una città, un ristorante o una tipologia di cucina</p>
            <p>Controlliamo i risultati e li ordiniamo per te, in base al punteggio</p>
        </div>
        
            
            <div class="col-md-1"></div>
            <div class="col-md-10">
                <h3><b>About MealBoss</b></h3>
                <p>MealBoss è una piattaforma che aiuta l'utente a cercare ed a selezionare il locale che più gli aggrada.</p>

                <p>Questo grazie all'opzione di una ricerca mirata nelle zone circostanti all'utente o semplicemente in una zona selezionata, 
                    trammite una mappa che facilità la localizzazione dei ristoranti. Inoltre c'è la possibilità di cercare dei locali in base al tipo di cucina cercato 
                    (africana, indiana, messicana, e molte altre) ed in base agli orari di apertura e di chiusura degli stessi.</p>

                <p>Per ogni locale ci sarà la possibilità di guardare il profilo, per ricavare maggiori informazioni come dei semplici dati quali la via ed il sito ufficiale o anche la possibilità 
                    di guardare foto del ristorante, recensioni di altri utenti che sono stati in quel locale e la valutazione media, indicata da delle stelline.</p>

                <p>C'è la possibilità di registrarsi nel sito, questo permetterebbe di valutare il ristorante o la pizzeria in cui si ha mangiato, utile per dare un resoconto più completo 
                    di un tipo di locale ed aiutare altri utenti nella selezione. Inoltre un proprietario di uno o più ristoranti può selezionare l'opzione "Claim" 
                    in modo tale da ricevere un feedback da parte di altri utenti tramite delle recensioni.</p>
            </div>
            <div class="col-md-1"></div>
            
            <div class="row"></div>
        
            <div class="col-md-1 col-sm-1 col-xs-0"></div>
            <div class="col-md-10">  
                <hr>
                <p id="footer">@2016-2017 Programmazione per il Web | 404 Group Not Found</p>
            </div>
            <div class="col-md-1 col-sm-1 col-xs-0"></div>
            <script src="media/js/scripts.js"></script>
    </body>
</html>
