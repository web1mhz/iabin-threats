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
		document.write("<li><a id="+categories[i].attributes.getNamedItem("id").nodeValue+" "+"class='head'>");
		document.write(categories[i].attributes.getNamedItem("name").nodeValue);
		document.write("</a>");
		document.write("<ul id="+categories[i].attributes.getNamedItem("name").nodeValue+">");
		document.write("<div id=both>");
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Threats"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<h5><a  class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a></h5>");
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		}
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Bioclim"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<h5><a  class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a></h5>");
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		}		
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Summaries"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			document.write("<h5><a  class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"> Clear </a></h5>");
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		} 
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Species"){
			// Buttons: Search, InfoSpecie, Clear
			document.write("<div id=specieButtons>");
			document.write("<a id=button class=utilityButtons>Search</a>");		
			document.write("<div id=showSpeciesInfo style=display:none><a class=utilityButtons>Show Info</a></div>");
			document.write("<a  class=utilityButtons id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+" style=display:none> Clear </a>");
			document.write("</div><br>");
			document.write("<div id=infoSpecieOptions style='display:none;'>");
	        document.write("<h5><input type=checkbox name='occurrences' key='' class=specieData> Occurrences records</h5>");        
	        document.write("<h5><input type=checkbox name='convex' key='' class=specieData> Convex hull</h5>");
	        document.write("<h5><input type=checkbox name='convexHull' key='' class=specieData> Convex hull buffer</h5>");
	        document.write("<h5><input type=checkbox id='0' name='distributionLimitedtoconvex' key='' class=specieDistribution> Species distribution limited to convex hull (probabilities)</h5>");
	        document.write("<h5><input type=checkbox id='1' name='distribution' key='' class=specieDistribution >Species distribution (probabilities)</h5>");
	        document.write("<h5><input type=checkbox id='2' name='threshold' key='' class=specieDistribution >Thresholded species distribution</h5>");
	        document.write("</div>");
			document.write("<div id=popupContact>");
	        document.write("<a id=popupContactClose><img src=images/close-icon.png border=0 align=middle /></a>");        
	        document.write("<h1>Taxononomic Species Menu</h1>");
	        document.write("<p id=contactArea>");
	        document.write("<div id='searchSpecie' class='searchSpecie'>");
	        document.write("<input id='search' class='ui-widget' /><input id='searchButton' type=button value='Search' />");
	        document.write("</div>");
	        // Here goes the code about species popup
	        document.write("</p></div>");
	        document.write("<div id=backgroundPopup></div>");
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
		    	document.write("<br>");
		    }
		    document.write("</ul>");
		    document.write("</div>");
		}
	}    
}