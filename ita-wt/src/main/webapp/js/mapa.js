$(document).ready(function(){	
	var path = getPath();
	var zoomMap = 4;
	var paLayers = [];	
	initialize();
	
	function getPath() {	
		return $.ajax({type:"GET",url:"info.do", async: false}).responseText;
	}
	
	function initialize() {
	    var myOptions = {
	        navigationControl: true,
	        navigationControlOptions: {
	            style: google.maps.NavigationControlStyle.SMALL,
	            position: google.maps.ControlPosition.RIGHT_CENTER
	        }
	    }
	    map = new google.maps.Map(document.getElementById("map"), myOptions);
	    map.setCenter(new google.maps.LatLng(-13.2, -59.6));
	    map.setZoom(zoomMap);
	    map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
	    google.maps.event.addListener(map, 'zoom_changed', function(){
	        zoomChange();
	    });    
	    for (i = 0; i < 15; i++) {
	        map.overlayMapTypes.push(null);
	    }
	}

	function zoomChange() {
	    if (map.getZoom() > 7) {
	        map.setZoom(7);
	    }
	    if (document.form1.Summaries.checked == true && map.getZoom() > 4) {
	        paLayers[0].setMap(map);
	        paLayers[1].setMap(map);
	        paLayers[2].setMap(map);
	    }
	    else {
	        paLayers[0].setMap(null);
	        paLayers[1].setMap(null);
	        paLayers[2].setMap(null);
	    }
	    if (document.form1.Summaries.checked == true && map.getZoom() > 5) {
	        paLayers[3].setMap(map);
	    }
	    else {
	        paLayers[3].setMap(null);
	        
	    }
	}
	
	function getOverlayMapOptions(layerName, opacityValue, layerId, paN, typeName){
	    var paths = {
	        "richness": path + "summaries/" + layerName,
	        "summaries": path + "/" + layerName + "/paQ" + paN,
	        "species": path + "species/" + layerId + "/" + layerName,
	        "bioclim": path + layerName + "/p" + (layerId + 1),
	        "threats": path + layerName + "/" + layerName + layerId
	    }
	    var overlay = {
	        getTileUrl: function(point, zoom){
	            var X = point.x % (1 << zoom);
	            return paths[typeName] + "/" + zoom + "/x" + X + "_y" + point.y + ".png";
	        },
	        tileSize: new google.maps.Size(256, 256),
	        isPng: true,
	        opacity: opacityValue,
	        setOpacity: function(opacValue){
	            this.opacity = opacValue;
	        }
	    };
	    return overlay;
	}
	
	function showHideScaleOpacity($context, layerName, overlayMapOptions, scaleSrc, layerMapPosition) {		 
		 if ($context.attr('checked')) {
			 var imageMore = $("#" + $context.attr("id") + " + .showScaleOpacity");
			 var layerId = parseInt($context.attr("id").split("-")[1]);
		 	 if (imageMore != undefined && $context.parent().children(".divScaleOpacity").attr("id") == undefined) {
		 		imageMore.attr("src", "images/minus.gif");
		 		imageMore.show();
		 		var scaleOpacity = $("#divScaleOpacityTemp").clone(true);
		 		scaleOpacity.attr("id", $context.attr("id") + "-ScOp"); //ScOp --> Scale Opacity				
				scaleOpacity.children("img").attr("src", scaleSrc);
				var labelOpacity = scaleOpacity.children(".opacityPercentage");
				labelOpacity.attr("id",$context.attr("id") + "-Percentage");				
				scaleOpacity.children("#opacityDivTemp").attr("id", "opacityDiv").slider({
						range: "min",
						value: 50,
						min: 0,
						max: 100,
						slide: function(event, ui){
							event.stopPropagation();
							labelOpacity.text(ui.value + "%");
							overlayMapOptions.setOpacity(ui.value / 100.0);
							map.overlayMapTypes.setAt(layerMapPosition, new google.maps.ImageMapType(overlayMapOptions));
						}
				});
				$context.parent().append(scaleOpacity);
				
				scaleOpacity.toggle("fast");
			}
		}
		else {
	    	$("#" + $context.attr("id") + " + .showScaleOpacity").hide();
	        $("#" + $context.attr("id") + "-ScOp").slideUp("fast", function(){
	        	$(this).remove();
			});
			if (imageMore != undefined) {
	        	imageMore.hide();
	        }
		}
	}
    
    $(".Threats").click(function(event){
        var $target = $(event.target);
        var layerName = $target.attr("id").split("-")[0];
        var layerId = $target.attr("id").split("-")[1];		
        var overlayMapsOptions = getOverlayMapOptions(layerName, 0.5, layerId, null, "threats");
		var scaleImageSource = path + layerName + "/" + layerName + layerId + "/" + layerName + layerId + "scaleImage.png";
        var overlayMap = new google.maps.ImageMapType(overlayMapsOptions);       
        map.overlayMapTypes.setAt(0, overlayMap);		 
		$("#navigation [name='radio']").each(function() {
			showHideScaleOpacity($(this), layerName, overlayMapsOptions, scaleImageSource, 0);			
		});       
    });
    
    $(".Bioclim").click(function(event){
        var $target = $(event.target);
        var layerName = $target.attr("id").split("-")[0];
        var layerId = parseInt($target.attr("id").split("-")[1]);
        var overlayMapsOptions = getOverlayMapOptions(layerName, 0.5, layerId, null, "bioclim"); 
		var scaleImageSource = path + layerName + '/p' + (layerId + 1) + '/p' + (layerId + 1) + "scaleImage.png";       
        var overlayMap = new google.maps.ImageMapType(overlayMapsOptions);
        map.overlayMapTypes.setAt(0, overlayMap);
        $("#navigation [name='radio']").each(function() {
			showHideScaleOpacity($(this), layerName, overlayMapsOptions, scaleImageSource, 0);
		});
    });
    
    
    $(".specieData").click(function(event){
        var $target = $(event.target);
        key = $target.attr("key");
        if ($target.attr("name") == "occurrences") {
            if (key == "") {
                $target.attr('checked', false);
            }
            else {
                if ($target.attr('checked')) {
                    occurencesLayer = new google.maps.KmlLayer(path + "species/" + key + "/" + key + "-point.kml?date=" + (new Date()).getTime(), {
                        preserveViewport: true,
                        suppressInfoWindows: true
                    });
                    occurencesLayer.setMap(map);
                }
                else {
                    occurencesLayer.setMap(null);
                }
            }
        }
        else 
            if ($target.attr("name") == "convex") {
                if (key == "") {
                    $target.attr('checked', false);
                }
                else {
                    if ($target.attr('checked')) {
                        convexLayer = new google.maps.KmlLayer(path + "species/" + key + "/" + key + "-chull.kml", {
                            preserveViewport: true,
                            suppressInfoWindows: true
                        });
                        convexLayer.setMap(map);
                    }
                    else {
                        convexLayer.setMap(null);
                    }
                }
            }
            else 
                if ($target.attr("name") == "convexHull") {
                    if (key == "") {
                        $target.attr('checked', false);
                    }
                    else {
                        if ($target.attr('checked')) {
                            poligonLayer = new google.maps.KmlLayer(path + "species/" + key + "/" + key + "-chullbuff.kml", {
                                preserveViewport: true,
                                suppressInfoWindows: true
                            });
                            poligonLayer.setMap(map);
                        }
                        else {
                            poligonLayer.setMap(null);
                        }
                    }
                }
    });
    
    $(".specieDistribution").click(function(event){
        var $target = $(event.target);
        var layerName = $target.attr("name");
        var layerId = $target.attr("key");
        var layerSpecieId = parseInt($target.attr("id"));
        var overlayMapOptions = getOverlayMapOptions(layerName, 0.5, layerId, null, "species");
		var layerMapPosition = (layerSpecieId + 1);
		var scaleImageSource = path + "5000009scaleImage.png";
		
		showHideScaleOpacity($target, layerName, overlayMapOptions, scaleImageSource, layerMapPosition);
        if ($target.attr('checked')) {
            var overlayMap = new google.maps.ImageMapType(overlayMapOptions);
            map.overlayMapTypes.setAt((layerSpecieId + 1), overlayMap);         
        }
        else {
            map.overlayMapTypes.setAt(layerMapPosition, null);           
        }
    });	
	
    
    $(".richness").click(function(event){
        var $target = $(event.target);
        var layerName = $target.attr("name");
        var layerId = parseInt($target.attr("id").split("-")[1]);
        var overlayMapOptions = getOverlayMapOptions(layerName, 0.5, null, null, "richness");
		var scaleImageSource =  path + "summaries/" + layerName + '/' + layerName + "scaleImage.png";
		var layerMapPosition = layerId + 4;
		showHideScaleOpacity($target, layerName, overlayMapOptions, scaleImageSource, layerMapPosition);
        if ($target.attr('checked')) {
            var overlayMap = new google.maps.ImageMapType(overlayMapOptions);
            map.overlayMapTypes.setAt(layerMapPosition, overlayMap);           
        }
        else {
            map.overlayMapTypes.setAt(layerMapPosition, null);              
        }
    });
    
    

    $(".Summaries").click(function(event){
        var $target = $(event.target);
        var layerName = $target.attr("name");
        var layerSummariesId = parseInt($target.attr("id").split("-")[1]);        
        if ($target.attr('checked') == true) {
            for (i = 4; i > 0; i--) {
                paLayers[4 - i] = new google.maps.KmlLayer(path + "summaries/paQ" + i + "/"+"total-info.kml?date=" + (new Date()).getTime(), {
                    preserveViewport: true
                });
            }
            var imageMapTypes = [];
            for (i = 0; i < 4; i++) {
                imageMapTypes[i] = new google.maps.ImageMapType(getOverlayMapOptions(layerName, 0.5, null, 4-i, "summaries"));
                map.overlayMapTypes.setAt((layerSummariesId + 12 + i), imageMapTypes[i]);
            }
            if (map.getZoom() > 4) {
                paLayers[0].setMap(map);
                paLayers[1].setMap(map);
                paLayers[2].setMap(map);
            }
            else {
                paLayers[0].setMap(null);
                paLayers[1].setMap(null);
                paLayers[2].setMap(null);
            }
            if (map.getZoom() > 5) {
                paLayers[3].setMap(map);
            }
            else {
                paLayers[3].setMap(null);
            }
        }
        else 
            if ($target.attr('checked') == false) {
                for (i = 12; i <= 15; i++) {
                    map.overlayMapTypes.setAt((layerSummariesId + i), null);
                }
                for (i = 0; i < paLayers.length; i++) {
                    paLayers[i].setMap(null);
                }
            }
    });
    
    $("#showSpeciesInfo a").click(function(event){
        event.preventDefault();
        var options = "height=600,width=700,scrollTo,resizable=1,scrollbars=1,toolbar=0,statusbar=0";
        var $target = $(event.target);
        var link = "infoSpecie.html?id=" + $target.attr("id");
        newWindow = window.open(link, 'Popup', options);
        return false;
    });
}); //END JQUERY