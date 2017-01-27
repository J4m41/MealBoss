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
                    for (var i = 0; i < data.places.length; i++){
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

    var locations;

    var mapOptions = {
      disableDefaultUI: true
    };


    map = new google.maps.Map(document.querySelector('#map'), mapOptions);

    function locationFinder() {

        // initializes an empty array
        var locations = [];
        var names = [];

        $.getJSON("http://localhost:8080/MealBoss/media/js/suggestions.json", function(data) {
            for (var i = 0; i < data.restaurants.length; i++){
                locations.push(data.restaurants.coords);
                names.push(data.restaurants.name);
            }
        });

        return locations;
    }
    
    function addMarkers(locations, names){
        
        var infowindow = new google.maps.InfoWindow();
        
        for(var i=0; i<locations.length; i++) {
            
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(locations.lat, locations.lng),
                map: map
            });
            
            google.maps.event.addListener(marker, 'click', (function(marker, i) {
                return function() {
                    infowindow.setContent(names[i]);
                    infowindow.open(map, marker);
                };
            })(marker, i));
        }
    }
    
    // Sets the boundaries of the map based on pin locations
    window.mapBounds = new google.maps.LatLngBounds();

    // locations is an array of location strings returned from locationFinder()
    locations = locationFinder();

    // pinPoster(locations) creates pins on the map for each location in
    // the locations array
    addMarkers(locations);
}

// Calls the initializeMap() function when the page loads
window.addEventListener('load', initializeMap);

// Vanilla JS way to listen for resizing of the window
// and adjust map bounds
window.addEventListener('resize', function(e) {
    //Make sure the map bounds get updated on page resize
    map.fitBounds(mapBounds);
});

$("#map-div").append(map);