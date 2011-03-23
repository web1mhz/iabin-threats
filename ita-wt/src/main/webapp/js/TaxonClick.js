 	var archivo;
$(document).ready(function() {		
	$(".element").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$target = $(event.target).parent();
		if($("#" + $target.attr("id")+" ul").attr("id") == undefined) {
			if($target.attr("rank")==7000) {
				disablePopup();
				$("#showSpeciesInfo input").attr("id", $target.attr("id"));
				$('input.specieData').attr("key", $target.attr("id"));
				$('input.specieDistribution').attr("key", $target.attr("id"));
				$('#infoEspecie').css({'display' : 'block'});
			} else {
				// run ajax
				$.ajax({
					url: "service.do",
					dataType: "json",
					data: "id="+$target.attr("id")+"&rank="+$target.attr("rank"),
					beforeSend: function(data) {
						$loaderGift = $("#loaderGift").clone().show();
						$target.append($loaderGift);
					},
					success: function(data) {
						var rank = "";
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
						$target.append(temp);
						
					},
					complete: function(data, code) {
						if(code == "success") {
							$("#"+$target.attr("id")+" #loaderGift").remove();
							$("#" + $target.attr("id")).children("a").css("background", "url(images/minus.gif) no-repeat 10px center");
							$("#" + $target.attr("id")).children("ul").slideDown('fast');
						}
					},
					error: function(data, error, objectError) {
						// Aun falta implementar lo que sucederá cuando hay un error
						alert("error: "+objectError);
						$("#"+$target.attr("id")+" #loaderGift").remove();
					}
				}); // END ajax
		   } 
		}else {
			// content already exist
			// is visible?
			if ($("#" + $target.attr("id")).children("ul").is(":visible")) {
				// if is visible slide up and hide the content.							
				$("#" + $target.attr("id")).children("a").css("background", "url(images/plus.gif) no-repeat 10px center");
				$("#" + $target.attr("id")).children("ul").slideUp('fast');
				
			} else {
				// if is not visible slide down and show the content.
				$("#" + $target.attr("id")).children("ul").slideDown('fast');
				$("#" + $target.attr("id")).children("a").css("background", "url(images/minus.gif) no-repeat 10px center");			
			}
		}	
	}); // END click 	
}); //END ready