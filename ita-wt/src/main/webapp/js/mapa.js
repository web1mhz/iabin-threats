$(document).ready(function() {
    var w;
	var h;
	w=120;
	h=100
	xWidth ('ampliacion',w + 6)
	xHeight ('ampliacion',h + 6 + 20)
	xWidth ('c1',w)
	xHeight ('c1',h)
	xWidth ('cerrarampliacion',w)
			
$(".threats").click(function(event) {
        var $target = $(event.target);
		layer=$target.attr("id").split("-")[0]
        index=$target.attr("id").split("-")[1]
                              
        xInnerHtml('c1','<img src="http://gisweb.ciat.cgiar.org/ita/'+layer+'/'+layer+index+'/'+layer+index+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('ampliacion');
		$("#botonMostrarInfo").css("visibility", "hidden");
		
		 });

$(".bioclim").click(function(event) {
        var $target = $(event.target);
		layer=$target.attr("id").split("-")[0]
        index=$target.attr("id").split("-")[1]
        xInnerHtml('c1','<img src="http://gisweb.ciat.cgiar.org/ita/'+layer+'/p'+(index-7)+'/p'+(index-7)+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('ampliacion');
		$("#botonMostrarInfo").css("visibility", "hidden");
			              
        });

});
 
function cerrar_ampliacion(){
	xHide('ampliacion');

}
 
 function abrir_ampliacion(){
	xShow('ampliacion');
	
}

  var cont=0; 
  var map;
  var so=4;
  var so1=0;
  var theme;  
  var toggleArray = [];
  var kmlArray = [];
  var cont;

  function initialize() {
  
 cont=0;


    var myOptions = {
    navigationControl: true,
      navigationControlOptions: {
          style: google.maps.NavigationControlStyle.SMALL,
          position: google.maps.ControlPosition.RIGHT_CENTER
      }
	}
    map = new google.maps.Map(document.getElementById("map"),myOptions);
    map.setCenter(new google.maps.LatLng(-13.2,-59.6));
    map.setZoom(so);
    map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
	
  	    }
//var archivo;
		
function validar(archivo){
	
	if(cont==0){
		so1= map.getZoom();
		c = map.getCenter();
		x = c.lng(),
		y = c.lat();
		initialize2(archivo);
		cont=1;
	}

}


function initialize2(archivo) {
  c = map.getCenter();
  x = c.lng(),
  y = c.lat();
  kml_layer1 = "http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/"+archivo+"-point.kml";

  kml_layer2 = "http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/"+archivo+"-pol.kml";

  kml_layer3 = "http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/imagen.kml";


	 var myOptions = {
    navigationControl: true,
      navigationControlOptions: {
          style: google.maps.NavigationControlStyle.SMALL,
          position: google.maps.ControlPosition.RIGHT_CENTER
      }
	}
    map = new google.maps.Map(document.getElementById("map"),myOptions);
    map.setCenter(new google.maps.LatLng(y,x));
    map.setZoom(so1);
    map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
	 google.maps.event.addListener(map, 'zoom_changed', function() {
		if (map.getZoom() > 10) {
		    map.setZoom(10);
		 }
		});
		
			
		
  
  	geoxml1 = new google.maps.KmlLayer(kml_layer1, {preserveViewport:true}); 

	geoxml2 = new google.maps.KmlLayer(kml_layer2, {preserveViewport:true}); 

	geoxml3 = new google.maps.KmlLayer(kml_layer3, {preserveViewport:true});	

	kmlArray.push(geoxml1,geoxml2,geoxml3);

	toggleArray.push(0,0,0);
  }
  
  toggleKML = function(x) { 
		
		c = map.getCenter();
		x1 = c.lng(),
		y = c.lat();
		i = x;
		
		if (toggleArray[i] == 1) {

		kmlArray[i].setMap(null);

		map.setCenter(new google.maps.LatLng(y,x1));

		toggleArray.splice(i,1,'0');                  

		}

		else {

		kmlArray[i].setMap(map);

		map.setCenter(new google.maps.LatLng(y,x1));		

		toggleArray.splice(i,1,'1');		

		}

}
  
   
   function mapa() {
	so1= map.getZoom();
	c = map.getCenter();
	x = c.lng(),
	y = c.lat();
		
	 var o = parseFloat(document.getElementById("opac").value);
	   //alert('opacidad ='+o);
	  		var opac;
		if(o>100)
			{
				opac=100;
				opac=opac/100;
				document.getElementById('opac').value=100;
				alert("no");
			}
			else
				if(o<0)
				{
					opac=100;
					opac=opac/100;
					document.getElementById('opac').value=100;
					alert("no");
				}
				else
					opac=o/100;

  var tiles = new google.maps.ImageMapType({
	 
  //aqui obtengo la ruta de los tiles que he seleccionado en el radiobutton
    getTileUrl: function(point, zoom) {
	var X = point.x % (1 << zoom); // para repetir los tiles alrededor del mundo
	
	if(layer=="Threats" ){
	return  "http://gisweb.ciat.cgiar.org/ita/"+layer+"/"+layer+index+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	}
	else{
	return  "http://gisweb.ciat.cgiar.org/ita/"+layer+"/p"+(index-7)+"/"+zoom + "/x" + X + "_y" + point.y + ".png";	
		
	}
  },																																																												
   tileSize: new google.maps.Size(256, 256),
   isPng: true,
   opacity:opac
  });
  
  
 //map.overlayMapTypes.insertAt(0, traffic);	
 initialize2(archivo);
 
 //ctaLayer.setMap(map);
  map.overlayMapTypes.push(null);	
  map.overlayMapTypes.push(tiles);	
 
   }