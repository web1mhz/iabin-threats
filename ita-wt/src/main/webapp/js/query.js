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
			document.getElementById("Threats-0").checked=false;
			document.getElementById("Threats-1").checked=false;
			document.getElementById("Threats-2").checked=false;
			document.getElementById("Threats-3").checked=false;
			document.getElementById("Threats-4").checked=false;
			document.getElementById("Threats-5").checked=false;
			document.getElementById("Threats-6").checked=false;
			document.getElementById("Threats-7").checked=false;
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
			document.getElementById("Bioclim-26").checked=false;
		});
		$("#linkCerrar").click(function() {
			$("#botonMostrarInfo").css("visibility", "");			
		});
			
     });
   
