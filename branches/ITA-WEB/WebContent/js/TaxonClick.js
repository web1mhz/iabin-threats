$(document).ready(function() {				
	$(".element").click(function(event) {
		event.stopPropagation();
		$target = $(event.target).parent();
		if($("#" + $target.attr("id")+" ul").attr("id") == undefined) {
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
					$.each(data, function(i, taxon) {
						var title="Family";
						if(taxon.rankID==6000){
							title="Genus";
						} else {
							if(taxon.rankID==7000) {
								title="Specie";
							}
						}									
						$target.append("<ul style='display:none;'><li id="+taxon.id+" rank="+taxon.rankID+
							" class=element><a><Strong>"+title+"</Strong>"+" "+taxon.canonical+"</a></li></ul>");
					});					
				},
				complete: function(data, code) {
					if(code == "success") {
						$("#"+$target.attr("id")+" #loaderGift").remove();
						$("#" + $target.attr("id")).children("a").css("background", "url(../images/minus.gif) no-repeat 10px center");
						$("#" + $target.attr("id")).children("ul").slideDown('fast');
					}
				},
				error: function(data, error, objectError) {
					// Aun falta implementar lo que sucederá cuando hay un error
					alert("error: "+error);
					$("#"+$target.attr("id")+" #loaderGift").remove();
				}
			}); // END ajax
		} else {
			// content already exist
			// is visible?
			if ($("#" + $target.attr("id")).children("ul").is(":visible")) {
				// if is visible slide up and hide the content.							
				$("#" + $target.attr("id")).children("a").css("background", "url(../images/plus.gif) no-repeat 10px center");
				$("#" + $target.attr("id")).children("ul").slideUp('fast');
			} else {
				// if is not visible slide down and show the content.
				$("#" + $target.attr("id")).children("ul").slideDown('fast');
				$("#" + $target.attr("id")).children("a").css("background", "url(../images/minus.gif) no-repeat 10px center");			
			}
		}					
	});	// END click 
}); //END ready