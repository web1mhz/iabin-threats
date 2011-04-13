$(document).ready(function() {
	 			   $("#showMenu").hide(10);
	 	           $("#Threats").slideUp("fast");
				   $("#Bioclim").slideUp("fast");
				   $("#Species").slideUp("fast");
				   $("#Summaries").slideUp("fast");
				   $("#hideMenu").show();
				   var contador = true;
				   var contador1 = true;		
        $("#hideMenu").click(function(event) {
           event.preventDefault();
            if(contador == true) {
				$("#showMenu").show(10);
                $("#menu").fadeOut();
				$("#opacity").slideUp("slow");
				$("#hideMenu").hide(10);
                contador = true;
            }                      
        });
		 $("#showMenu").click(function(event) {
           event.preventDefault();
            if(contador1 == true) {
			   $("#showMenu").hide(10);
				$("#hideMenu").show(10);
				$("#menu").fadeIn();
				$("#opacity").slideDown("slow");
				$("#hideMenu").slideDown("fast");
                contador1 = true;
            }                    
        });
		 
		$("#div").draggable({handle: "#moveIcon"});		
		$(".submenuContent").resizable({
				alsoResize: ".submenuContent .submenuLayout",
				maxWidth: 180,
				minWidth: 180,
				minHeight: 100
		});
        
		$("#threat-menu").click(function(event) {
			event.preventDefault();						
			if($("#Threats").attr("style").indexOf("none") > -1) {
				$("#Threats").slideDown("slow");
				$("#Bioclim").slideUp("slow");
				$("#Species").slideUp("slow");
				$("#Summaries").slideUp("slow");					
			} else {$("#Threats").slideUp("slow");
				   $("#Bioclim").slideUp("slow");
				   $("#Species").slideUp("slow");
				   $("#Summaries").slideUp("slow");	}
		});		
		$("#bioclim-menu").click(function(event) {
			event.preventDefault();
			if($("#Bioclim").attr("style").indexOf("none") > -1) {
				$("#Bioclim").slideDown("slow");
				$("#Threats").slideUp("slow");	
				$("#Species").slideUp("slow");
				$("#Summaries").slideUp("slow")			
			} else {$("#Bioclim").slideUp("slow");
					$("#Threats").slideUp("slow");	
					$("#Species").slideUp("slow");
					$("#Summaries").slideUp("slow");}
		});		
		$("#taxon-menu").click(function(event) {
			event.preventDefault();
			if($("#Species").attr("style").indexOf("none") > -1) {
				$("#Species").slideDown("slow");
				$("#Bioclim").slideUp("slow");
				$("#Threats").slideUp("slow");	
				$("#Summaries").slideUp("slow");					
			} else {$("#Species").slideUp("slow");
				   $("#Bioclim").slideUp("slow");
				   $("#Threats").slideUp("slow");	
				   $("#Summaries").slideUp("slow");}					
		});            
		$("#summaries-menu").click(function(event) {
			event.preventDefault();
			if($("#Summaries").attr("style").indexOf("none") > -1) {
				$("#Summaries").slideDown("slow");
				$("#Species").slideUp("slow");
				$("#Bioclim").slideUp("slow");
				$("#Threats").slideUp("slow");	
						
			} else {$("#Summaries").slideUp("slow");
				   $("#Species").slideUp("slow");
				   $("#Bioclim").slideUp("slow");
				   $("#Threats").slideUp("slow");}
		});
		
		$("#buttonShowScaleInfo").click(function(event){
			event.preventDefault();
			abrir_ampliacion();
			$("#buttonShowScaleInfo").css("display", "none");
		});		
		$("#clearthreat-menu").click(function(event) {
			event.preventDefault();
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");			
			$(".Threats").attr("checked", false);
			$(event.target).parents("#both").find(".divScaleOpacity").remove();					
			$(event.target).parents("#both").find(".showScaleOpacity").css("display", "none");
		    map.overlayMapTypes.setAt(0,null);
		});		
		$("#clearbioclim-menu").click(function(event) {
			event.preventDefault();
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");			
			($(".Bioclim").attr("checked", false));	    
		    map.overlayMapTypes.setAt(0,null);
		});
		$("#cleartaxon-menu").click(function(event) {			
			event.preventDefault();				
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
			$("#infoSpecieOptions").css("display", "none");			
			$("#specieButtons #showSpeciesInfo").css("display", "none");
			$("#specieButtons [id^='clear']").css("display", "none");
			$("#opacitySpecie").css("display", "none");
			$("#showScale").css("display", "none");
	        $("#buttonShowScaleInfo").css("display", "none");
			
		});		
		$("#clearsummaries-menu").click(function(event) {
			event.preventDefault();
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");	
			($(".richness").attr("checked", false));
			($(".Summaries").attr("checked", false));			
			$(event.target).parent().find(".divScaleOpacity").remove();					
			$(event.target).parent().find(".showScaleOpacity").css("display", "none");			
			for(i=4;i<=15;i++){
				if(map.overlayMapTypes.getAt(i) != null) {
					map.overlayMapTypes.setAt(i,null);
				}
	    	}			
			for(i = 0 ; i < paLayers.length; i++) {				
				paLayers[i].setMap(null);
			}		    
		});
		
	 $("#closeLink").click(function() {
			$("#buttonShowScaleInfo").fadeIn();			
     });
     
     $(".showScaleOpacity").click(function(event) {
		 event.preventDefault();
		 divScaleOpacity = $(event.target).next();		 
		 divScaleOpacity.toggle("slow", function() {
			 if( $(event.target).attr("src") == "images/plus.gif") {
				 $(event.target).attr("src", "images/minus.gif");				 
			 } else {
				 $(event.target).attr("src", "images/plus.gif");
			 }
		 });		 
	 });	 
	 $("#opacityThreats").slider({
    		 range: "min",
    		 value: 80,
    		 min: 1,
    		 max: 100,
    		 slide: function( event, ui ) {
    		 	event.stopPropagation();
  				$( "#opacityLayer" ).val(ui.value+"%" );  				
    	 	}
    	 });
	$("#opacityBioclim").slider({
    		 range: "min",
    		 value: 80,
    		 min: 1,
    		 max: 100,
    		 slide: function( event, ui ) {
  				$( "#opacityLayer" ).val(ui.value+"%" );
    	 	}
    	 });
 	$("#opacitySpecie").slider({
    		 range: "min",
    		 value: 80,
    		 min: 1,
    		 max: 100,
    		 slide: function( event, ui ) {
  				$( "#opacityLayer" ).val(ui.value+"%" );
    	 	}
    	 });
}); 