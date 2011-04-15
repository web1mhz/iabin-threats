$(document).ready(function() {
	var $searchSpecie = $("#searchSpecie");
	var $popupContext = $("#popupContact");	
	$("#search", $searchSpecie).autocomplete({
		source: "taxonSearch.do",
		minLength: 2,
		select: function( event, ui ) {			
		}
	});
	$("#searchButton", $searchSpecie).click(function(event) {
		var $target = $(event.target);
		$.ajax({
			url: "taxonSearch.do",
			dataType: "json",
			data: "canonic="+$("#searchSpecie #search").val(),
			beforeSend: function(data) {							
				$("#treeStyle", $popupContext).html("<img id='loaderGift-big' src='images/ajax-loader-big.gif' />");				
			},
			success: function(data) {
				var $rankID = 1000;
				var $taxon;
				var $html = "<ul class='search'>";
				for(var i = 0; i < data.length; i++) {					
					$taxon = data[i];
					if($taxon.rankID != $rankID) {
						$rankID = $taxon.rankID;
						var $rank = "Families:";
						if($taxon.rankID==6000){
							$rank="Genuses:";
						} else if($taxon.rankID==7000) {
								$rank="Species: ";
						}						
						$html += "<li class='rankElement'><strong>"+$rank+"</strong></li>";
					}
					if($taxon.rankID==7000){
						$html += "<li id='"+$taxon.id+"' class='specieElement' rank='"+$taxon.rankID+"'><a href=''>"+$taxon.canonical+"</a></li>";
					}else{
						$html += "<li id='"+$taxon.id+"' class='element' rank='"+$taxon.rankID+"'><a href=''>"+$taxon.canonical+"</a ></li>";	
					}
				}				
				$("#treeStyle", $popupContext).append($html+"</ul>");				
			},
			complete: function(data, code) {
				if(code == "success") {					
					$("#loaderGift-big", $popupContext).remove();
				}
			},
			error: function(data, error, objectError) {
				// Aun falta implementar lo que sucederá cuando hay un error
				if (error == "timeout") {
					$("#treeStyle", $popupContext).html("The request timed out, please resubmit");
				} else if(error == "parsererror") {
					$("#treeStyle", $popupContext).html("<div id='error'>No Taxons Found!</div>");
				}
			}
		}); // END ajax
	});
	
	
	
});