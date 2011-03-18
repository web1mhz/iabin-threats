$(document).ready(function() {
	var $searchSpecie = $("#searchSpecie");
	var $popupContext = $("#popupContact");	
	$("#search", $searchSpecie).autocomplete({
		source: "taxonSearch.do",
		minLength: 2,
		select: function( event, ui ) {
			//alert(ui.item ?	"Selected: " + ui.item.value + " aka " + ui.item.id : "Nothing selected, input was " + this.value);
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
					/*if($taxon.rankID != $rankID) {
						$rankID = $taxon.rankID;
						var $rank = "Family:";
						if($taxon.rankID==6000){
							$rank="Genus:";
						} else if($taxon.rankID==7000) {
								$rank="Specie: ";
						}						
						$html += "<li class='rankElement'><strong>"+$rank+"</strong></li>";
					}*/
					$html += "<li id='"+$taxon.id+"' class='element' rank='"+$taxon.rankID+"'><a>"+$taxon.canonical+"</a></li><br>";
				}
				
				$("#treeStyle", $popupContext).append($html+"</ul>");
				
				
				/*
				var temp = "";					
				$.each(data, function(i, taxon) {
					if(i == 0) {
						rank = "Family: ";
						if(taxon.rankID==6000){
							rank="Genus: ";
						} else {
							if(taxon.rankID==7000) {
								rank="Specie: ";
							}
						}							
					}
					temp += "<li id="+taxon.id+" rank="+taxon.rankID+
							" class=element><a>"+taxon.canonical+"</a></li>";
				});
				temp = "<li class=rankElement><Strong>"+rank+"</Strong></li>" + temp;
				temp = "<ul style=display:none>"+temp+"</ul>";
				$target.append(temp);	*/					
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