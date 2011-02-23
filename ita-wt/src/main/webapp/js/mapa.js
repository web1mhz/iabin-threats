$(document).ready(function() {
    var w = 120;
	var h = 100;
	xWidth ('showScale',w + 6)
	xHeight ('showScale',h + 6 + 20)
	xWidth ('c1',w)
	xHeight ('c1',h)
	xWidth ('closeScale',w)
			
$(".threats").click(function(event) {
        var $target = $(event.target);
		var layerName=$target.attr("id").split("-")[0]
        var layerId=$target.attr("id").split("-")[1]
        xInnerHtml('c1','<img src="http://gisweb.ciat.cgiar.org/ita/'+layerName+'/'+layerName+layerId+'/'+layerName+layerId+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('showScale');
		$("#botonMostrarInfo").css("visibility", "hidden");
		showLayer(layerName, layerId);
		setActualPosition();
});

$(".bioclim").click(function(event) {
        var $target = $(event.target);
		var layerName=$target.attr("id").split("-")[0]
        var layerId=$target.attr("id").split("-")[1]
        xInnerHtml('c1','<img src="http://gisweb.ciat.cgiar.org/ita/'+layerName+'/p'+(layerId-7)+'/p'+(layerId-7)+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('showScale');
		$("#botonMostrarInfo").css("visibility", "hidden");
		showLayer(layerName, layerId); 
		setActualPosition();
});


$(".specieData").click(function(event) {
    var $target = $(event.target);
    key = $target.attr("key");
    if($target.attr("name") == "occurrences") {
    	showOccurences(key);
    } else if($target.attr("name") == "convex") {
    	showConvex(key);
    }
  //  setActualPosition();
});

});
 
function cerrar_ampliacion(){
	xHide('ampliacion');

}
 
 function abrir_ampliacion(){
	xShow('ampliacion');
	
}

 // ------------------------------------ JavaScript -------------------------------------
 
  var cont=0; 
  var map;  
  var so=4;
  var so1=0;
  var theme;  
  var toggleArray = [];
  var kmlArray = [];
  var cont;
  var toggle=0;
  var ctaLayer;
  var poligonLayer; 

function initialize() {
	var myOptions = {
			navigationControl: true,
			navigationControlOptions: {
		  		style: google.maps.NavigationControlStyle.SMALL,
		  		position: google.maps.ControlPosition.RIGHT_CENTER
		  	}
	}
    map = new google.maps.Map(document.getElementById("map"), myOptions);
    map.setCenter(new google.maps.LatLng(-13.2,-59.6));
    map.setZoom(so);
    map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
    google.maps.event.addListener(map, 'zoom_changed', function() {
		if (map.getZoom() > 10) {
			map.setZoom(10);
		}
	});	
 }  
 
function setActualPosition() {
	c = map.getCenter();
	x = c.lng();
	y = c.lat();   
	so1= map.getZoom();
	map.setCenter(new google.maps.LatLng(y,x));
	map.setZoom(so1);
	map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
}

function showOccurences(key) {
	if(key == "") {
		document.form1.occurrences.checked = false;
	} else {
	  if (document.form1.occurrences.checked){
		  kml_layer1 = "http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/"+key+"-point.kml";
		  ctaLayer = new google.maps.KmlLayer(kml_layer1, {preserveViewport:false});
		  ctaLayer.setMap(map);
	  } else {
		ctaLayer.setMap(null);
	  }
  }
}

function showConvex(key) {	
	if(key == "") {
		document.form1.convex.checked = false;
	} else {
		if (document.form1.convex.checked){
			poligonLayer = new google.maps.KmlLayer("http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/"+key+"-pol.kml", {preserveViewport:false})
			poligonLayer.setMap(map);
		} else {
			poligonLayer.setMap(null);			
		}
	}
} 
   
function showLayer(layerName, layerId) {
	var o = parseFloat(document.getElementById("opac").value);
	var opac;
	if(o > 100){
		opac = 100;
		opac = opac/100;
		document.getElementById('opac').value = 100;
	} else if(o < 0) {
		opac = 100;
		opac = opac / 100;
		document.getElementById('opac').value = 100;
	} else {
		opac = o/100;
	}
    var tiles = new google.maps.ImageMapType({
    	//aqui obtengo la ruta de los tiles que he seleccionado en el radiobutton
    	getTileUrl: function(point, zoom) {
    		var X = point.x % (1 << zoom); // para repetir los tiles alrededor del mundo
    		if(layerName == "Threats" ) {
    			return  "http://gisweb.ciat.cgiar.org/ita/"+layerName+"/"+layerName+layerId+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
    		} else {
    			return  "http://gisweb.ciat.cgiar.org/ita/"+layerName+"/p"+(layerId-7)+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
    		}
      	},																																																			
      	tileSize: new google.maps.Size(256, 256),
      	isPng: true,
      	opacity:opac
    });
    for(i = 0; i < map.overlayMapTypes.length; i++) {
    	  map.overlayMapTypes.setAt(i, null);
    }	
    map.overlayMapTypes.push(tiles); 
 }