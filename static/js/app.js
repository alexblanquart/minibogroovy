$(document).ready(function () {

	// initialize masonry
	var $container = $('.grid');
	$container.imagesLoaded(function(){
      $container.masonry({
        itemSelector: '.grid-item',
        columnWidth: 1
      });
    });

	// use infinite scroll coupled to jekyll pagination
    $container.infinitescroll({
      navSelector  : '.pagination',    // selector for the paged navigation 
      nextSelector : '.pagination a',  // selector for the NEXT link : to next items to display
      itemSelector : '.grid-item',     // selector for all items you'll retrieve
      loading: {
          finishedMsg: 'Plus rien à charger.',
          img: 'http://i.imgur.com/6RMhx.gif'
        }
      },
      // trigger Masonry as a callback
      function( newElements ) {
        // hide new items while they are loading
        var $newElems = $( newElements ).css({ opacity: 0 });
        // ensure that images load before adding to masonry layout
        $newElems.imagesLoaded(function(){
          // show elems now they're ready
          $newElems.animate({ opacity: 1 });
          $container.masonry( 'appended', $newElems, true ); 
        });
      }
    );

	$('.description').on('show.bs.collapse', function () {
		// scroll to top to see content
	  	$('body').animate({scrollTop: 0},'slow');
	});

  $('#carousel-blog').slick({
    dots: true,
    infinite: true,
    speed: 300,
    slidesToShow: 1,
    centerMode: true,
    variableWidth: true
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

