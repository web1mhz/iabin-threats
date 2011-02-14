function menu(){
if (window.XMLHttpRequest)
  {// codigo para IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// codigo para IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.open("GET","menu.xml",false);
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
  document.write("<div id=both>");
  if(x[i].attributes.getNamedItem("name").nodeValue=="Species"){
		
	document.write("<div id=button><input type=button value=Kingdom></div><br><br><br>");
	document.write("<h5><label> Example specie</label></h5>");
	document.write("<h5><label> class : mammalia</label></h5>");	
	document.write("<h5><label> family : Muridae</label></h5>");	
	document.write("<h5><label> genus : Nectomys</label></h5>");	
	document.write("<h5><label> specie : Nectomys apicalis</label></h5>");	
	document.write("<h5><input type=checkbox name= check1 onclick= validar('8000967');toggleKML('0');> Ocurrence records</h5>");	
	document.write("<h5><input type=checkbox name= check2 onclick= validar('8000967');toggleKML('1');> Convex hull buffer</h5>");
	document.write("<h5><input type=checkbox name= check3 onclick= validar('8000967');toggleKML('2')> Species distribution</h5>");
	document.write("<div id=popupContact>");
	document.write("<a id=popupContactClose>x</a>");	
	document.write("<h1>Taxononomic Species Menu</h1>");
	document.write("<p id=contactArea>");
	document.write("<h2><input type=text name=search /><input type=button value=Search></h2>");	
	document.write("</p></div>");
	document.write("<div id=backgroundPopup></div>");
	
		
	}
  for (j=0;j<x1.length;j++){
	
	
	if(x[i].attributes.getNamedItem("name").nodeValue==x1[j].attributes.getNamedItem("id").nodeValue){
	document.write("<h5><li ><label>");
	document.write("<input type="+x1[j].attributes.getNamedItem("type").nodeValue+" "
					+"name="+x1[j].attributes.getNamedItem("type").nodeValue+" "
					+"class="+x1[j].attributes.getNamedItem("class").nodeValue+" "
					+"id="+x1[j].attributes.getNamedItem("id").nodeValue+"-"+j+" "+"onClick="+x1[j].attributes.getNamedItem("click").nodeValue+">"+" ");
	document.write(x1[j].childNodes[0].nodeValue);
	document.write("</label></li></h5><br />");
							}
			}
	document.write("</div></ul></li>");	
 }
 
 document.write("</ul>");
}