function menu(){
if (window.XMLHttpRequest)
  {// codigo para IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// codigo para IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.open("GET","templates/menu.xml",false);
xmlhttp.send();
xmlDoc=xmlhttp.responseXML; 
var x=xmlDoc.getElementsByTagName("category");
var x1=xmlDoc.getElementsByTagName("item");
var contador;

document.write("<ul id=navigation>"); 
for (i=0;i<x.length;i++)
  { 
  document.write("<li><a id="+x[i].attributes.getNamedItem("id").nodeValue+" "+"class='head'>");
  document.write(x[i].attributes.getNamedItem("name").nodeValue);
  document.write("</a>");
  document.write("<ul id="+x[i].attributes.getNamedItem("name").nodeValue+">");
  document.write("<h5><a id=clear"+x[i].attributes.getNamedItem("id").nodeValue+"><img src=images/boton-clear.png border=0 align=left /></a></h5><br>");
  document.write("<div id=both>");
   if(x[i].attributes.getNamedItem("name").nodeValue=="Species"){		
	document.write("<div id=button><input type=button value='Search Species' ></div>");
	document.write("<h5><label> Example specie</label></h5>");
	document.write("<h5><label> class : mammalia</label></h5>");	
	document.write("<h5><label> family : Muridae</label></h5>");	
	document.write("<h5><label> genus : Nectomys</label></h5>");	
	document.write("<h5><label> specie : Nectomys apicalis</label></h5>");	
	//alert($target.attr('id'));
	document.write("<h5><input type=checkbox name='occurrences' key='' class=specieData> Occurrences records</h5>");	
	document.write("<h5><input type=checkbox name='convex' key='' class=specieData> Convex hull</h5>");
	document.write("<h5><input type=checkbox name='convexHull' key='' class=specieData> Convex hull buffer</h5>");
	document.write("<h5><input type=checkbox id='0' name='distributionLimitedtoconvex' key='' class=specieDistribution> Species distribution limited to convex hull (probabilities)</h5>");
	document.write("<h5><input type=checkbox id='1' name='distribution' key='' class=specieDistribution >Species distribution (probabilities)</h5>");
	document.write("<h5><input type=checkbox id='2' name='threshold' key='' class=specieDistribution >Thresholded species distribution</h5>");
	document.write("<div id=popupContact>");
	document.write("<a id=popupContactClose>x</a>");	
	document.write("<h1>Taxononomic Species Menu</h1>");
	document.write("<p id=contactArea>");
	document.write("<div class='searchSpecie'>");
	document.write("<input id='search' class='ui-widget' /><input type=button value=Search />");
	document.write("</div>");
	document.write("<div id='treeStyle'>");
	document.write("<ul class='general'>");
	document.write("<li id='13140804' rank='1000' class='element'><a href='#'> Plants</a></li>");
	document.write("<br>");
	document.write("<li id='13140952' rank='3000' class='element'><a href='#'> Amphibians</a></li>");
	document.write("<br>");
	document.write("<li id='13140955' rank='3000' class='element'><a href='#'> Birds</a></li>");
	document.write("<br>");
	document.write("<li id='13140937' rank='3000' class='element'><a href='#'> Insects</a></li>");
	document.write("<br>");
	document.write("<li id='13140957' rank='3000' class='element'><a href='#'> Mammals</a></li>");
	document.write("<br>");
	document.write("<li id='13140958' rank='3000' class='element'><a href='#'> Reptiles</a></li>");
	document.write("</ul>");
	document.write("</div>");
	document.write("</p></div>");
	document.write("<div id=backgroundPopup></div>");
		}
   
  for (j=0;j<x1.length;j++){
	if(x[i].attributes.getNamedItem("name").nodeValue==x1[j].attributes.getNamedItem("id").nodeValue){
	document.write("<h5><li ><label>");
	document.write("<input type="+x1[j].attributes.getNamedItem("type").nodeValue+" "
					+"name="+x1[j].attributes.getNamedItem("type").nodeValue+" "
					+"class="+x1[j].attributes.getNamedItem("class").nodeValue+" "
					+"id="+x1[j].attributes.getNamedItem("id").nodeValue+"-"+j+" "+">");
	document.write(x1[j].childNodes[0].nodeValue);
	document.write("</label></li></h5><br />");
							}
			}
	document.write("</div></ul></li>");	
 }
 
 document.write("</ul>");
}