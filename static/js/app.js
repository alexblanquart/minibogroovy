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

  // Blog carousel on index page
  $('#carousel-blog .carousel-inner .item:first').addClass('active')
  $('#carousel-blog').carousel({interval: 10000})

  $('#carousel-blog .item').each(function(){
    var next = $(this).next();
    if (!next.length) {
      next = $(this).siblings(':first');
    }
    next.children(':first-child').clone().appendTo($(this));
    
    if (next.next().length>0) {
      next.next().children(':first-child').clone().appendTo($(this));
    }
    else {
      $(this).siblings(':first').children(':first-child').clone().appendTo($(this));
    }
  });

  var index = lunr(function () {
    this.field('title', {boost: 10})
  })

});

