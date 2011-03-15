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
		document.write("<h5><a id=clear"+categories[i].attributes.getNamedItem("id").nodeValue+"><img src=images/boton-clear.png border=0 align=left /></a></h5><br>");
		document.write("<div id=both>");
		if(categories[i].attributes.getNamedItem("name").nodeValue=="Threats"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		}else if(categories[i].attributes.getNamedItem("name").nodeValue=="Bioclim"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		}else if(categories[i].attributes.getNamedItem("name").nodeValue=="Species"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
			for(j = 0; j < items.length; j++) {			
				document.write("<h5><li ><label>");
				document.write("<input type="+items[j].attributes.getNamedItem("type").nodeValue+" "
						+"name="+items[j].attributes.getNamedItem("name").nodeValue+" "
						+"class="+items[j].attributes.getNamedItem("class").nodeValue+" "
						+"id="+items[j].attributes.getNamedItem("id").nodeValue+"-"+j+" >");
				document.write(items[j].childNodes[0].nodeValue);
				document.write("</label></li></h5><br />");
			}
		}if(categories[i].attributes.getNamedItem("name").nodeValue=="Summaries"){	
			var items = categories[i].getElementsByTagName("item");
			var j;
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
		document.write("</div></ul></li>");
	}
	document.write("</ul>");	
}