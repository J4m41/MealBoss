$('#mobile-nav').click(function(event) {
  $('nav').toggleClass('active');
});

$('#search_bar').autocomplete({
    source : function (request, response) {
        $.ajax({
            dataType: "json",
            type: 'GET',
            url: 'http://localhost:8080/MealBoss/media/js/suggestions.json',
            data: {
                term : request.term
            },
            success : function (data) {
                var suggestions = [];
                if(document.getElementById('search_names').checked === true){
                    for (var i = 0; i < data.restaurants.length; i++){
                        suggestions.push(data.restaurants[i].name);
                    }
                }
                if(document.getElementById('search_places').checked === true){
                    for (var i = 0; i < data.restaurants.length; i++){
                        suggestions.push(data.restaurants[i].place);
                    }
                }
                if(document.getElementById('search_cuisines').checked === true){
                    for (var i = 0; i < data.cuisineTypes.length; i++){
                        suggestions.push(data.cuisineTypes[i]);
                    }
                }
                response($.ui.autocomplete.filter(suggestions, request.term).slice(0,5));
           }
        });
    }
});

var map;

function initializeMap() {
    var userLat, userLng;
    
    navigator.geolocation.getCurrentPosition(function(data){
        userLat = data.coords.latitude;
        userLng = data.coords.longitude;
        map.setCenter(new google.maps.LatLng(userLat,userLng));
    });
    
    var mapOptions = {
        zoom: 15,
        disableDefaultUI: true
    };

    map = new google.maps.Map(document.querySelector('#map-div'), mapOptions);
    
    var infowindow = new google.maps.InfoWindow();
    
    $.getJSON("http://localhost:8080/MealBoss/media/js/suggestions.json", function(data) {
        $.each(data.restaurants, function(key, val) {
            
            var name = val.name;
            var descr = val.descr;
            var place = val.place;
            
            $.each(val, function(key, val){
            
                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(val.lat, val.lng),
                    map: map
                });
                    
                google.maps.event.addListener(marker, 'click', (function(marker,name, place, descr) {
                    return function() {
                        infowindow.setContent("<h1>"+name+"</h1>"
                                +"<p>"+place+"</p>"
                                +"<p>"+descr+"</p>");
                        infowindow.open(map, marker);
                    };
                })(marker, name, place, descr));
                    
            });
                
        });
    });

    
    // Sets the boundaries of the map based on pin locations
    window.mapBounds = new google.maps.LatLngBounds();

}

// Vanilla JS way to listen for resizing of the window
// and adjust map bounds
window.addEventListener('resize', function(e) {
    //Make sure the map bounds get updated on page resize
    map.fitBounds(mapBounds);
});

$("#map-div").append(map);


function initSuggested(){

    var suggested = 0;
    var suggestions = [];
    
    var options = {
        enableHighAccuracy: true,
        timeout: 5000,
        maximumAge: 0
    };

    function success(pos) {
        var crd = pos.coords;
        
        $.getJSON("http://localhost:8080/MealBoss/media/js/suggestions.json", function(data) {
        
            $.each(data.restaurants, function(key, val) {

                var name = val.name;
                var descr = val.descr;
                var place = val.place;
                var photo = val.photo;
                $.each(val, function(key, val){
                    var lat = val.lat;
                    var lng = val.lng;
                    var distance = getDistanceFromLatLonInKm(lat, lng, crd.latitude, crd.longitude);
                    var restaurant = {
                        name: name,
                        descr: descr,
                        place: place,
                        photo: photo,
                        distance: distance
                    };
                            
                    if(distance < 4){
                        suggested++;
                        suggestions.push(restaurant);
                    }
                    
                });
                
            });
            var windowWidth = $(window).width();
            $(document).width(windowWidth);
            console.log(windowWidth);
            suggestions.sort(function(a,b){
                                return a.distance - b.distance;
                            });
            if (windowWidth > 500){
                for(var k = 0; k < 3; k++){
                    var formattedImg = "<td><img id=\"suggested-img\" src=\""+suggestions[k].photo+"\"></td>";
                    var formattedData = "<td id=\"sugg-data\"><h1><a href=\"#\">"+suggestions[k].name+"</a></h1>"
                                        +"<p>"+suggestions[k].place+"</p>"
                                        +"<p>"+suggestions[k].descr+"</p>"
                                        +"<p><b>Distance:</b> "+suggestions[k].distance.toFixed(1)+" km</p></td>";
                    $("#suggested-img").append(formattedImg);
                    $("#suggested-data").append(formattedData);
                }
            }else{
                var formattedImg = "<td><img id=\"suggested-img\" src=\""+suggestions[0].photo+"\"></td>";
                var formattedData = "<td id=\"sugg-data\"><h1><a href=\"#\">"+suggestions[0].name+"</a></h1>"
                                        +"<p>"+suggestions[0].place+"</p>"
                                        +"<p>"+suggestions[0].descr+"</p>"
                                        +"<p><b>Distance:</b> "+suggestions[0].distance.toFixed(1)+" km</p></td>";
                    $("#suggested-img").append(formattedImg);
                    $("#suggested-data").append(formattedData);
            }
        });
    };

    function error(err) {
        console.warn("ERROR: "+err.message);
    };

    navigator.geolocation.getCurrentPosition(success, error, options);
    
    function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
        var R = 6371; // Radius of the earth in km
        var dLat = deg2rad(lat2-lat1);  // deg2rad below
        var dLon = deg2rad(lon2-lon1); 
        var a = 
            Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
            Math.sin(dLon/2) * Math.sin(dLon/2)
            ; 
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        var d = R * c; // Distance in km
        return d;
    }

    function deg2rad(deg) {
        return deg * (Math.PI/180);
    }
    
}

initSuggested();