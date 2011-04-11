	  var path= "http://gisweb.ciat.cgiar.org/iabin-threats/ITA/generated-files/";
	  var map;  
	  var zoomMap=4;
	  var poligonLayer; 
	  var convexLayer;
	  var ocurrencesLayer;
	  var paLayers =[];	  
$(document).ready(function() {	
    var w = 170;
	var h = 30;
	var path= "http://gisweb.ciat.cgiar.org/iabin-threats/ITA/generated-files/";
	xWidth ('showScale',w + 6)
	xHeight ('showScale',h + 6 + 20)
	xWidth ('scale',w)
	xHeight ('scale',h)
	xWidth ('closeScale',w)
	/*
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
		*/	
$(".Threats").click(function(event) {
	    var $target = $(event.target);
		var layerName=$target.attr("id").split("-")[0];
        var layerId=$target.attr("id").split("-")[1];
        var overlayMaps = [
                           {
                        	   getTileUrl: function(point, zoom){
                               var X = point.x % (1 << zoom);
                               return path+layerName+"/"+layerName+layerId+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
                               },tileSize: new google.maps.Size(256,256),
                               isPng:true,
                               opacity:0.8
                           }
                           ]; 
		if ($target.attr('checked')){
   		 var overlayMap = new google.maps.ImageMapType(overlayMaps[0]);	    	
	    	 map.overlayMapTypes.setAt(0,overlayMap);	  
	    } else {
	    	map.overlayMapTypes.setAt(0,null);
	    }   
		
        xInnerHtml('scale','<img src="'+path+layerName+'/'+layerName+layerId+'/'+layerName+layerId+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('showScale');
        $("#showScale").css("display", "block");
		$("#buttonShowScaleInfo").css("display", "none");
});
$(".Bioclim").click(function(event) {
        var $target = $(event.target);
		var layerName=$target.attr("id").split("-")[0];
        var layerId=parseInt($target.attr("id").split("-")[1]);
        var overlayMaps = [
                           {
                        	   getTileUrl: function(point, zoom){
                               var X = point.x % (1 << zoom);
                               return path+layerName+"/p"+(layerId+1)+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
                               },tileSize: new google.maps.Size(256,256),
                               isPng:true,
                               opacity:0.8
                            }
                           ];
		if ($target.attr('checked')){
   		 var overlayMap = new google.maps.ImageMapType(overlayMaps[0]);	    	
	    	 map.overlayMapTypes.setAt(0,overlayMap);	  
	    } else {
	    	map.overlayMapTypes.setAt(0,null);
	    }                                                           
                                                  
        xInnerHtml('scale','<img src="'+path+layerName+'/p'+(layerId+1)+'/p'+(layerId+1)+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
        xShow('showScale');
        $("#showScale").css("display", "block");
        $("#buttonShowScaleInfo").css("display", "none");
});


$(".specieData").click(function(event) {
    var $target = $(event.target);
    key = $target.attr("key");
    if($target.attr("name") == "occurrences") {
    	if(key == "") {
    		$target.attr('checked',false);
    	} else {
    	  if ($target.attr('checked')){
    		  occurencesLayer = new google.maps.KmlLayer(path+"species/"+key+"/"+key+"-point.kml?date="+(new Date()).getTime(), {preserveViewport:true, suppressInfoWindows: true});
    		  occurencesLayer.setMap(map);
    	  } else {
    		  occurencesLayer.setMap(null);
    	  }
    	}
    }else if($target.attr("name") == "convex") {
    	if(key == "") {
    		$target.attr('checked',false);
    	} else {
    	  if ($target.attr('checked')){
    		  convexLayer = new google.maps.KmlLayer(path+"species/"+key+"/"+key+"-chull.kml", {preserveViewport:true, suppressInfoWindows: true});
    		  convexLayer.setMap(map);
    	  } else {
    		  convexLayer.setMap(null);
    	  }
    	}
    		
    }else if($target.attr("name") == "convexHull") {
    	if(key == "") {
    		$target.attr('checked',false);
    	} else {
    	  if ($target.attr('checked')){
    		  poligonLayer = new google.maps.KmlLayer(path+"species/"+key+"/"+key+"-chullbuff.kml", {preserveViewport:true, suppressInfoWindows: true});
    		  poligonLayer.setMap(map);
    	  } else {
    		  poligonLayer.setMap(null);
    	  }
    	}
    }
});	
$(".specieDistribution").click(function(event) {
	var $target = $(event.target);
    var layerName=$target.attr("name");
    var layerId=$target.attr("key");
    var layerSpecieId=parseInt($target.attr("id"));
    var overlayMaps = [
	                   {
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"species/"+layerId+"/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	           },tileSize: new google.maps.Size(256,256),
	    	 	           	 isPng:true,
	    	 	           	 opacity:0.7
	    	 	        } , {
	    	 	    	    getTileUrl: function(point, zoom){
	    	 	            	var X = point.x % (1 << zoom);
	    	 	            	return path+"species/"+layerId+"/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	            },tileSize: new google.maps.Size(256,256),
	    	 	           	 isPng:true,
	    	 	           	 opacity:0.6
	    	 	          },{
	    	 	           	 getTileUrl: function(point, zoom){
	    	 	           	 	var X = point.x % (1 << zoom);
	    	 	           	 	return path+"species/"+layerId+"/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	           	 },tileSize: new google.maps.Size(256,256),
	    	 	           	   isPng:true,
	    	 	           	   opacity:0.5
	    	 	           }
	    	 	         ];
   
    	if ($target.attr('checked')){
    		var overlayMap = new google.maps.ImageMapType(overlayMaps[layerSpecieId]);			
			map.overlayMapTypes.setAt((layerSpecieId+1),overlayMap);			
		} else {
			map.overlayMapTypes.setAt((layerSpecieId+1),null);
			$("#showScale").css("display", "none");
	        $("#buttonShowScaleInfo").css("display", "none");
		}    	
    	if($target.attr("id")==0){
    		xInnerHtml('scale','<img src="'+path+'5000009'+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
	        xShow('showScale');
	        $("#showScale").css("display", "block");
	        $("#buttonShowScaleInfo").css("display", "none");
    	}else if($target.attr("id")==1 ||$target.attr("id")==2){
    		xInnerHtml('scale','<img src="'+path+'5000009'+layerName+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
	        xShow('showScale');
	        $("#showScale").css("display", "block");
	        $("#buttonShowScaleInfo").css("display", "none");    		
    	}
});

$(".richness").click(function(event) {
	
    var $target = $(event.target);
    var layerName=$target.attr("name");
    var layerRichnessId=parseInt($target.attr("id").split("-")[1]); 
    var overlayMaps = [
	                   {
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
   	 	           				var X = point.x % (1 << zoom);
   	 	           				return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
   	 	       				},tileSize: new google.maps.Size(256,256),
   	 	       				  isPng:true,
   	 	       				  opacity:0.7
	                   },{
		               	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
		               	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
   	 	           				var X = point.x % (1 << zoom);
   	 	           				return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
   	 	       				},tileSize: new google.maps.Size(256,256),
   	 	       				  isPng:true,
   	 	       				  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
	 	           				var X = point.x % (1 << zoom);
	 	           				return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	 	       				},tileSize: new google.maps.Size(256,256),
	 	       				  isPng:true,
	 	           	          opacity:0.7
	                    },{
		                    getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"summaries/"+layerName+"/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },
	                   
	    	 	      ];
    	var imageMore = $("#"+$target.attr("id")+" + .showScaleOpacity");
       	if ($target.attr('checked')){
       		var overlayMap = new google.maps.ImageMapType(overlayMaps[layerRichnessId-1]);
			map.overlayMapTypes.setAt((layerRichnessId+4),overlayMap);
			//xInnerHtml('scale','<img src="'+path+'summaries/'+layerName+'/'+layerName+'scaleTestImage.png"'+'width="'+w+'" height="'+h+'" border="0">') 
	        //xShow('showScale');
			$("#showScale").css("display", "block");
			$("#buttnShowScaleInfo").css("display", "none");
			if(imageMore != undefined) {
				imageMore.attr("src", "images/minus.gif");
				imageMore.show();
				var scaleOpacity = $("#divScaleOpacityTemp").clone(true);				
				scaleOpacity.attr("id", $target.attr("id")+"-ScOp"); //ScOp --> Scale Opacity				
				scaleOpacity.children("img").attr("src", path+"summaries/"+layerName+'/'+layerName+"scaleImage.png");
				scaleOpacity.children("#opacityDivTemp").attr("id", "opacityDiv").slider({
		    		 range: "min",
		    		 value: 80,
		    		 min: 1,
		    		 max: 100,
		    		 slide: function( event, ui ) {
		    		 	event.stopPropagation();
		  				$( "#opacityLayer" ).val(ui.value+"%" );
		  				//alert(typeof map.overlayMapTypes.getAt(0));
		    	 	}
		    	 });
				$target.parent().append(scaleOpacity);
				//scaleOpacity.slideDown("slow");
				scaleOpacity.toggle("slow");
			}
		} else {
			map.overlayMapTypes.setAt((layerRichnessId+4),null);
			$("#buttonShowScaleInfo").css("display", "none");	
			$("#showScale").css("display", "none");
			$("#"+$target.attr("id")+" + .showScaleOpacity").hide();
			$("#"+$target.attr("id")+"-ScOp").slideUp("slow", function() { 
				$(this).remove();
			});
			if(imageMore != undefined) {
				imageMore.hide();
			}
		}   
});

$(".Summaries").click(function(event) {
	var $target = $(event.target);
    var layerName=$target.attr("name");
    var layerSummariesId=parseInt($target.attr("id").split("-")[1]);
    var overlayMaps = [
	                   {
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"/"+layerName+"/pa/paQ4/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"/"+layerName+"/pa/paQ3/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"/"+layerName+"/pa/paQ2/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   },{
	                	   getTileUrl: function(point, zoom){
	    	 	           		var X = point.x % (1 << zoom);
	    	 	           		return path+"/"+layerName+"/pa/paQ1/"+zoom + "/x" + X + "_y" + point.y + ".png";
	    	 	       		},tileSize: new google.maps.Size(256,256),
	    	 	           	  isPng:true,
	    	 	           	  opacity:0.7
	                   }
	                   ];    
    if ($target.attr('checked')==true){
    	for(i = 4; i > 0 ; i--) {
    		paLayers[4-i] = new google.maps.KmlLayer("http://gisweb.ciat.cgiar.org/iabin-threats/ITA/generated-files/summaries/pa/total-info"+i+".kml?date="+(new Date()).getTime(),{preserveViewport:true});
    	}
    	var imageMapTypes = [];
    	for(i = 0; i < 4; i++) {
    		imageMapTypes[i] = new google.maps.ImageMapType(overlayMaps[layerSummariesId+i]);
    		map.overlayMapTypes.setAt((layerSummariesId+12+i),imageMapTypes[i]);
    	}    	
		if (map.getZoom() > 4) {
    		paLayers[0].setMap(map);
    		paLayers[1].setMap(map);
    		paLayers[2].setMap(map);	   		
    	}else{
    		paLayers[0].setMap(null);
    		paLayers[1].setMap(null);
    		paLayers[2].setMap(null);    		
    	}
    	if (map.getZoom() > 5) {
    		paLayers[3].setMap(map);	   		
    	}else{
    		paLayers[3].setMap(null);    		
    	}
    } else if ($target.attr('checked')==false){
    	for(i=12;i<=15;i++){
    		map.overlayMapTypes.setAt((layerSummariesId+i),null);		
    	}	
    	for(i = 0 ; i < paLayers.length; i++) {    		
			paLayers[i].setMap(null);
		}
	}   
    
});
		
$("#showSpeciesInfo a").click(function(event){
	event.preventDefault();
	var options = "height=450,width=700,scrollTo,resizable=1,scrollbars=1,toolbar=0,statusbar=0";
	var $target = $(event.target);	
	var link = "infoSpecie.html?id="+$target.attr("id");
    newWindow=window.open(link, 'Popup', options);  
    return false; 
});

}); //END JQUERY
function cerrar_ampliacion(){
	xHide('showScale');

}
 
 function abrir_ampliacion(){
	xShow('showScale');
	
}
 
 
 // ------------------------------------ JavaScript -------------------------------------

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
    map.setZoom(zoomMap);
    map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
    google.maps.event.addListener(map, 'zoom_changed', function() {
    				zoomChange();
    });
    
    function zoomChange(){
    	if (map.getZoom() > 7) {
        	map.setZoom(7);    		
    	}
    	if (document.form1.Summaries.checked==true && map.getZoom() > 4) {
    		paLayers[0].setMap(map);
     		paLayers[1].setMap(map);
    		paLayers[2].setMap(map);	   		
    	}else{
    		paLayers[0].setMap(null);
     		paLayers[1].setMap(null);  
    		paLayers[2].setMap(null);    		
    	}
    	if (document.form1.Summaries.checked==true && map.getZoom() > 5) {
       		paLayers[3].setMap(map);	   		
    	}else{
     		paLayers[3].setMap(null);
    		
    	}
    }
    for(i=0;i<15;i++) {
    	map.overlayMapTypes.push(null);   	
    }
 }  
