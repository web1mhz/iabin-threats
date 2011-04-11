function menu() {
	if (window.XMLHttpRequest) { // codigo para IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	} else {// codigo para IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET","templates/menu.xml",false);
	xmlhttp.send();
	xmlDoc=xmlhttp.responseXML;
	var categories=xmlDoc.getElementsByTagName("category");	
	document.write("<ul id=navigation>");
	var i;
	for (i = 0; i < categories.length; i++){
		document.write("<li><a href='' id="+categories[i].attributes.getNamedItem("id").nodeValue+" "+"class='head'><span>");
		document.write(categories[i].attributes.getNamedItem("name").nodeValue);
		document.write("</span></a>");
		document.write("<ul id="+categories[i].attributes.getNamedItem("name").nodeValue+" class=submenuContent>");
		document.write("<div id=both>");
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Threats"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<div class='submenuButtons'>");
			document.write("<a href='' class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a>");
			document.write("<div id='opacityThreats' class='opacityBar'></div>");
			document.write("</div>");
			document.write("<hr class='submenuDivision'>");
			document.write("<div class=submenuLayout>");			
			for(j = 0; j < items.length; j++) {			
				document.write("<li>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</li><br>");
			}
			document.write("</div>");	
		}
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Bioclim"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<div class='submenuButtons'>");
			document.write("<a href='' class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a>");
			document.write("<div id='opacityBioclim' class='opacityBar'></div>");
			document.write("</div>");
			document.write("<hr class='submenuDivision'>");
			document.write("<div class=submenuLayout>");			
			for(j = 0; j < items.length; j++) {			
				document.write("<li>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</li><br>");
			}
			document.write("</div>");
		}
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Summaries"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<a href='' class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a>");
			document.write("<div class=submenuLayout>");			
			for(j = 0; j < items.length; j++) {
				document.write("<li><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li><br>");
			}
			document.write("</div>");			
		} 
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Species"){
			// Buttons: Search, InfoSpecie, Clear
			document.write("<div id=specieButtons>");
			document.write("<a href='' id=searchSpeciePopupButton class=utilityButtons>Search</a>");			
			document.write("<div id=showSpeciesInfo style=display:none><a href='' class=utilityButtons>Show Info</a></div>");			
			document.write("<a href='' class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+" style=display:none> Clear </a>");
			document.write("<div id='opacitySpecie' class='opacityBar' style='display:none;'></div>");
			document.write("</div>");
			document.write("<br /><hr class='submenuDivision'>");
			document.write("<div id=infoSpecieOptions class=submenuLayout style='display:none;'>");			
	        document.write("<input type=checkbox name='occurrences' key='' class=specieData> Occurrences records<br>");        
	        document.write("<input type=checkbox name='convex' key='' class=specieData> Convex hull<br>");
	        document.write("<input type=checkbox name='convexHull' key='' class=specieData> Convex hull buffer<br>");
	        document.write("<input type=checkbox id='0' name='distributionLimitedtoconvex' key='' class=specieDistribution> Species distribution limited to convex hull (probabilities)<br>");
	        document.write("<input type=checkbox id='1' name='distribution' key='' class=specieDistribution >Species distribution (probabilities)<br>");
	        document.write("<input type=checkbox id='2' name='threshold' key='' class=specieDistribution >Thresholded species distribution<br>");
	        document.write("</div>");
		}
		document.write("</div></ul></li>");
	}
	document.write("</ul>");	
}

function infoPopupSpecies() {	
	if (window.XMLHttpRequest) { // codigo para IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	} else {// codigo para IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET","templates/menu.xml",false);
	xmlhttp.send();
	xmlDoc=xmlhttp.responseXML;
	var categories=xmlDoc.getElementsByTagName("category");
	for (var i = 0; i < categories.length; i++) {
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Species"){
			document.write("<div id='treeStyleTemp' style='display:none'>");				
		    document.write("<ul class='general'>");
		    var species = categories[i].getElementsByTagName("specie");
		    for(j = 0; j < species.length; j++) {
		    	document.write("<li id='"+species[j].attributes.getNamedItem("id").nodeValue+"' rank='"+species[j].attributes.getNamedItem("rank").nodeValue+"' class='element'><a href=''> "+species[j].childNodes[0].nodeValue+"</a></li>");
		    }
		    document.write("</ul>");
		    document.write("</div>");
		}
	}    
}