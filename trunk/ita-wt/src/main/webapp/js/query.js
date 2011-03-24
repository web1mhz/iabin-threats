     $(document).ready(function() {
	 			   $("#boton2").hide(10);
	 	           $("#Threats").slideUp("fast");
				   $("#Bioclim").slideUp("fast");
				   $("#Species").slideUp("fast");
				   $("#Summaries").slideUp("fast");	
				   var contador = true;
				   var contador1 = true;		
        $("#boton1").click(function(event) {
           event.preventDefault();
            if(contador == true) {
				$("#boton2").show(10);
                $("#ingresarAqui").slideUp("slow");
				$("#opacity").slideUp("slow");
				$("#boton1").hide(10);
                contador = true;
            }                      
        });
		 $("#boton2").click(function(event) {
           event.preventDefault();
            if(contador1 == true) {
			   $("#boton2").hide(10);
				$("#boton1").show(10);
				$("#ingresarAqui").slideDown("slow");
				$("#opacity").slideDown("slow");
				$("#boton1").slideDown("fast");
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
		$("#buttonShowInfo").css("visibility", "hidden");
		$("#showSpeciesInfo").css("visibility", "hidden");
		$("#buttonShowInfo").click(function(){
			abrir_ampliacion();
			$("#buttonShowInfo").css("visibility", "hidden");
		});		
		$("#clearthreat-menu").click(function() {
			$("#buttonShowInfo").css("visibility", "hidden");	
			$("#showScale").css("visibility", "hidden");			
			($(".Threats").attr("checked", false));	    
		    map.overlayMapTypes.setAt(0,null);
		});		
		$("#clearbioclim-menu").click(function() {
			$("#buttonShowInfo").css("visibility", "hidden");	
			$("#showScale").css("visibility", "hidden");			
			($(".Bioclim").attr("checked", false));	    
		    map.overlayMapTypes.setAt(0,null);
		});
		$("#cleartaxon-menu").click(function() {
			($(".specieData").attr("checked", false));
			($(".specieDistribution").attr("checked", false));
			$("#showSpeciesInfo").css("visibility", "hidden");
			occurencesLayer.setMap(null);
			convexLayer.setMap(null);
			poligonLayer.setMap(null);
		    map.overlayMapTypes.setAt(1,null);
		    map.overlayMapTypes.setAt(2,null);
		    map.overlayMapTypes.setAt(3,null);
		});		
		$("#clearsummaries-menu").click(function() {
			$("#buttonShowInfo").css("visibility", "hidden");	
			$("#showScale").css("visibility", "hidden");	
			($(".richness").attr("checked", false));
			map.overlayMapTypes.setAt(4,null);
		    map.overlayMapTypes.setAt(5,null);
		    map.overlayMapTypes.setAt(6,null);
		    map.overlayMapTypes.setAt(7,null);
		    map.overlayMapTypes.setAt(8,null);
		    map.overlayMapTypes.setAt(9,null);
		    map.overlayMapTypes.setAt(10,null);
		    map.overlayMapTypes.setAt(11,null);
		});	
	 $("#closeLink").click(function() {
			$("#buttonShowInfo").css("visibility", "");			
		});			
     });
   
     $(function() {
 		$( "#slider-range-min" ).slider({
 			range: "min",
 			value: 50,
 			min: 1,
 			max: 100,
 			slide: function( event, ui ) {
 				$( "#opacityLayer" ).val(ui.value+"%" );
 			}
 		});
 		$( "#opacityLayer" ).val($( "#slider-range-min" ).slider( "value" )+ "%"  );
 	});
