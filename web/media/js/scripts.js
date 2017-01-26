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
                    for (var i = 0; i < data.names.length; i++){
                        suggestions.push(data.names[i]);
                    }
                }
                if(document.getElementById('search_places').checked === true){
                    for (var i = 0; i < data.places.length; i++){
                        suggestions.push(data.places[i]);
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