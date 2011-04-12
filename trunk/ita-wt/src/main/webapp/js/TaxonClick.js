$(document).ready(function() {		
	$(".specieElement").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$target = $(event.target).parent();
		// It suppose all elements here have rank value 7000
		disablePopup();
		$(".specieData").each(function () {
			 if($(this).attr("checked")==true && $(this).attr("id")=="ocurrences"){
				 $(this).attr("checked", false);
				 occurencesLayer.setMap(null);					 
			 }else if($(this).attr("checked")==true && $(this).attr("id")=="convex"){					 
				 $(this).attr("checked", false);
				 convexLayer.setMap(null);					 
			 }else if($(this).attr("checked")==true && $(this).attr("id")=="convexHull"){					 
				 $(this).attr("checked", false);
				 poligonLayer.setMap(null);
			 }		
			 
		 });
		
		 $(".specieDistribution").each(function () {
			 if($(this).attr("checked")==true && $(this).attr("id")=="0"){
				 $(this).attr("checked", false);
				 map.overlayMapTypes.setAt(1,null);					 
			 }else if($(this).attr("checked")==true && $(this).attr("id")=="1"){					 
				 $(this).attr("checked", false);
				 map.overlayMapTypes.setAt(2,null);					 
			 }else if($(this).attr("checked")==true && $(this).attr("id")=="2"){					 
				 $(this).attr("checked", false);
				 map.overlayMapTypes.setAt(3,null);
			 }
		 });				
		$("#showScale").css("display", "none");		
        $("#buttonShowScaleInfo").css("display", "none");
		$("#showSpeciesInfo a").attr("id", $target.attr("id"));
		$('input.specieData').attr("key", $target.attr("id"));
		$('input.specieDistribution').attr("key", $target.attr("id"));
		$('#infoSpecieOptions').css({'display' : 'block'});
		$("#specieButtons #showSpeciesInfo").css("display", "inline");
		$("#specieButtons [id^='clear']").css("display", "inline");
		$("#opacitySpecie").css("display", "block");
	});
	$(".element").live("click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		$target = $(event.target).parent();		
		if($("#" + $target.attr("id")+" ul").attr("id") == undefined) {
			if($target.attr("rank") != 7000) {				
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
							if(taxon.rankID==7000) {
								temp += "<li id="+taxon.id+" rank="+taxon.rankID+
									" class=specieElement><a href=''>"+taxon.canonical+"</a></li>";
							}else{
								temp += "<li id="+taxon.id+" rank="+taxon.rankID+
								" class=element><a href=''>"+taxon.canonical+"</a></li>";
							}
							
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