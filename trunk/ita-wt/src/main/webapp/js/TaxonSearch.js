$(document).ready(function() {
	$("#search").autocomplete({
		source: "taxonSearch.do",
		minLength: 2,
		select: function( event, ui ) {
			//alert(ui.item ?	"Selected: " + ui.item.value + " aka " + ui.item.id : "Nothing selected, input was " + this.value);
		}
	});
});


/*
 *
 * source: "search.php",
			minLength: 2,
			select: function( event, ui ) {
				log( ui.item ?
					"Selected: " + ui.item.value + " aka " + ui.item.id :
					"Nothing selected, input was " + this.value );
			}

 * 
 */
