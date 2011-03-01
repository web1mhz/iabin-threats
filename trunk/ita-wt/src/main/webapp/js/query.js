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
		$("#buttonShowInfo").click(function(){
			abrir_ampliacion();
			$("#buttonShowInfo").css("visibility", "hidden");
		});
		
		$("#clearThreats").click(function() {
			$("#buttonShowInfo").css("visibility", "hidden");	
			$("#showScale").css("visibility", "hidden");			
		    ($("#Threats-0").attr("checked", false));
		    ($("#Threats-1").attr("checked", false));
		    ($("#Threats-2").attr("checked", false));
		    ($("#Threats-3").attr("checked", false));
		    ($("#Threats-4").attr("checked", false));
		    ($("#Threats-5").attr("checked", false));
		    ($("#Threats-6").attr("checked", false));
		    ($("#Threats-7").attr("checked", false));
		    ($("#Bioclim-8").attr("checked", false));
		    ($("#Bioclim-9").attr("checked", false));
		    ($("#Bioclim-10").attr("checked", false));
		    ($("#Bioclim-11").attr("checked", false));
		    ($("#Bioclim-12").attr("checked", false));
		    ($("#Bioclim-13").attr("checked", false));
		    ($("#Bioclim-14").attr("checked", false));
		    ($("#Bioclim-15").attr("checked", false));
		    ($("#Bioclim-16").attr("checked", false));
		    ($("#Bioclim-17").attr("checked", false));
		    ($("#Bioclim-18").attr("checked", false));
		    ($("#Bioclim-19").attr("checked", false));
		    ($("#Bioclim-20").attr("checked", false));
		    ($("#Bioclim-21").attr("checked", false));
		    ($("#Bioclim-22").attr("checked", false));
		    ($("#Bioclim-23").attr("checked", false));
		    ($("#Bioclim-24").attr("checked", false));
		    ($("#Bioclim-25").attr("checked", false));
		    ($("#Bioclim-26").attr("checked", false));
		    
		    for(i = 0; i < map.overlayMapTypes.length; i++) {
		    	  map.overlayMapTypes.setAt(i, null);
		    }	
		    
		    
						
	/*
			document.getElementById("Bioclim-8").checked=false;
			document.getElementById("Bioclim-9").checked=false;
			document.getElementById("Bioclim-10").checked=false;
			document.getElementById("Bioclim-11").checked=false;
			document.getElementById("Bioclim-12").checked=false;
			document.getElementById("Bioclim-13").checked=false;
			document.getElementById("Bioclim-14").checked=false;
			document.getElementById("Bioclim-15").checked=false;
			document.getElementById("Bioclim-16").checked=false;
			document.getElementById("Bioclim-17").checked=false;
			document.getElementById("Bioclim-18").checked=false;
			document.getElementById("Bioclim-19").checked=false;
			document.getElementById("Bioclim-20").checked=false;
			document.getElementById("Bioclim-21").checked=false;
			document.getElementById("Bioclim-22").checked=false;
			document.getElementById("Bioclim-23").checked=false;
			document.getElementById("Bioclim-24").checked=false;
			document.getElementById("Bioclim-25").checked=false;
			document.getElementById("Bioclim-26").checked=false;*/
			//TODO
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
