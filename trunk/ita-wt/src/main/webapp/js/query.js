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

		$("#botonMostrarInfo").css("visibility", "hidden");
		$("#botonMostrarInfo").click(function(){
			abrir_ampliacion();
			$("#botonMostrarInfo").css("visibility", "hidden");
		});
		
		$("#none").click(function() {
			$("#botonMostrarInfo").css("visibility", "hidden");	
			$("#ampliacion").css("visibility", "hidden");
				document.getElementById("Threats0").checked=false;
				document.getElementById("Threats1").checked=false;
				document.getElementById("Threats2").checked=false;
				document.getElementById("Threats3").checked=false;
				document.getElementById("Threats4").checked=false;
				document.getElementById("Threats5").checked=false;
				document.getElementById("Threats6").checked=false;
				document.getElementById("Threats7").checked=false;
				document.getElementById("Bioclim8").checked=false;
				document.getElementById("Bioclim9").checked=false;
				document.getElementById("Bioclim10").checked=false;
				document.getElementById("Bioclim11").checked=false;
				document.getElementById("Bioclim12").checked=false;
				document.getElementById("Bioclim13").checked=false;
				document.getElementById("Bioclim14").checked=false;
				document.getElementById("Bioclim15").checked=false;
				document.getElementById("Bioclim16").checked=false;
				document.getElementById("Bioclim17").checked=false;
				document.getElementById("Bioclim18").checked=false;
				document.getElementById("Bioclim19").checked=false;
				document.getElementById("Bioclim20").checked=false;
				document.getElementById("Bioclim21").checked=false;
				document.getElementById("Bioclim22").checked=false;
				document.getElementById("Bioclim23").checked=false;
				document.getElementById("Bioclim24").checked=false;
				document.getElementById("Bioclim25").checked=false;
				document.getElementById("Bioclim26").checked=false;
				document.getElementById("summaries27").checked=0;
				document.getElementById("summaries28").checked=0;
		});
		$("#linkCerrar").click(function() {
			$("#botonMostrarInfo").css("visibility", "");			
		});
			
     });
   
