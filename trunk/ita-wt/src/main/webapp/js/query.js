$(document).ready(function() {
	 			   $("#showMenu").hide(10);
	 	           $("#Threats").slideUp("fast");
				   $("#Bioclim").slideUp("fast");
				   $("#Species").slideUp("fast");
				   $("#Summaries").slideUp("fast");	
				   var contador = true;
				   var contador1 = true;		
        $("#hideMenu").click(function(event) {
           event.preventDefault();
            if(contador == true) {
				$("#showMenu").show(10);
                $("#menu").slideUp("slow");
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
				$("#menu").slideDown("slow");
				$("#opacity").slideDown("slow");
				$("#hideMenu").slideDown("fast");
                contador1 = true;
            }                      
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
		
		$("#buttonShowScaleInfo").click(function(){
			event.preventDefault();
			abrir_ampliacion();
			$("#buttonShowScaleInfo").css("display", "none");
		});		
		$("#clearthreat-menu").click(function(event) {
			event.preventDefault();
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");			
			($(".Threats").attr("checked", false));	    
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
			($(".specieData").attr("checked", false));
			($(".specieDistribution").attr("checked", false));
			$("#specieButtons #showSpeciesInfo").css("display", "none");
			$("#specieButtons [id^='clear']").css("display", "none");
			$("#both #infoSpecieOptions").css("display", "none");
			occurencesLayer.setMap(null);
			convexLayer.setMap(null);
			poligonLayer.setMap(null);
			map.overlayMapTypes.setAt(1,null);
			map.overlayMapTypes.setAt(2,null);
			map.overlayMapTypes.setAt(3,null);
		});		
		$("#clearsummaries-menu").click(function(event) {
			event.preventDefault();
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");	
			($(".richness").attr("checked", false));
			($(".Summaries").attr("checked", false));
			map.overlayMapTypes.setAt(4,null);
		    map.overlayMapTypes.setAt(5,null);
		    map.overlayMapTypes.setAt(6,null);
		    map.overlayMapTypes.setAt(7,null);
		    map.overlayMapTypes.setAt(8,null);
		    map.overlayMapTypes.setAt(9,null);
		    map.overlayMapTypes.setAt(10,null);
		    map.overlayMapTypes.setAt(11,null);
		    map.overlayMapTypes.setAt(12,null);
		    map.overlayMapTypes.setAt(13,null);
		    map.overlayMapTypes.setAt(14,null);
		    map.overlayMapTypes.setAt(15,null);
		    paLayer.setMap(null);
		    paLayer1.setMap(null);
		    paLayer2.setMap(null);
		    paLayer3.setMap(null);
		    
		});	
	 $("#closeLink").click(function() {
			$("#buttonShowScaleInfo").fadeIn();			
		});			
     });
   
     $(function() {
    	 $("#opacityThreats").slider({
    		 range: "min",
    		 value: 80,
    		 min: 1,
    		 max: 100,
    		 slide: function( event, ui ) {
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
 		/*$( "#slider-range-min" ).slider({
 			range: "min",
 			value: 50,
 			min: 1,
 			max: 100,
 			slide: function( event, ui ) {
 				$( "#opacityLayer" ).val(ui.value+"%" );
 			}
 		});*/
 		//$( "#opacityLayer" ).val($( "#slider-range-min" ).slider( "value" )+ "%"  );
 	});
