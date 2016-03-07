$(document).ready(function () {

  // initialize masonry
	var $container = $('.grid');
	$container.imagesLoaded(function(){
      $container.masonry({
        itemSelector: '.grid-item',
        columnWidth: 1
      });
    });

  var posts = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('title'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    identify: function(obj) { return obj.title; },
    prefetch: '/posts.json'
  });

  $('#prefetch .typeahead').typeahead(null, {
    name: 'posts',
    display: 'title',
    source: posts,
    templates: {
        suggestion: function(data) {
            return "<div><a href="+data.url+">"+data.title+"</a></div>";
        }
    }
  });

});

