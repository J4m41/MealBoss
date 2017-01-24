$('#mobile-nav').click(function(event) {
  $('nav').toggleClass('active');
});

var names = ["Ristorante1","Ristorante2","Ristorante3"];

$('#search_bar').autocomplete({
    source: names,
    minLength: 2
});