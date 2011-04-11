/***************************/
//@Author: Adrian "yEnS" Mato Gondelle
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!

// adapted by CIAT Developer Team (DAPA Project) - johanse & htobon
/***************************/

//SETTING UP OUR POPUP
//0 means disabled; 1 means enabled;
var popupStatus = 0;

//loading popup with jQuery magic!
function loadPopup(){
	//loads popup only if it is disabled
	if(popupStatus==0){
		$("#backgroundPopup").css({
			"opacity": "0.7"
		});
		$("#backgroundPopup").fadeIn("fast");
		$("#popupContact").fadeIn("fast");
		var $tree = $("#treeStyleTemp").clone(true);
		$tree.attr("id", "treeStyle");
		$tree.css("display", "");
		$("#searchSpecie").after($tree);		
		popupStatus = 1;
	}
}

//disabling popup with jQuery magic!
function disablePopup(){
	//disables popup only if it is enabled
	if(popupStatus==1){
		$("#backgroundPopup").fadeOut("slow");
		$("#popupContact").fadeOut("slow");		
		$("#treeStyle").remove();
		popupStatus = 0;
	}
}

//centering popup
function centerPopup(){
	//request data for centering
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $("#popupContact").height();
	var popupWidth = $("#popupContact").width();	
	//centering
	$("#popupContact").css({
		"position": "absolute",
		"top": windowHeight/2-popupHeight/2,
		"left": windowWidth/2-popupWidth/2
	});
	//only need force for IE6
	
	$("#backgroundPopup").css({
		"height": windowHeight
	});
	
}


//CONTROLLING EVENTS IN jQuery
$(document).ready(function(){
	
	//LOADING POPUP
	//Click the button event!
	$("#searchSpeciePopupButton").click(function(event){
		event.preventDefault();
		//centering with css
		centerPopup();
		//load popup
		loadPopup();
		occurencesLayer.setMap(null);
		convexLayer.setMap(null);
		poligonLayer.setMap(null);
		$("#infoSpecieOptions").css({"display" : "none"});
		($(".specieData").attr("checked", false));
		($(".specieDistribution").attr("checked", false));
		$("#showScale").css("display", "none");
		$("#opacitySpecie").css("display", "none");
        $("#buttonShowScaleInfo").css("display", "none");
        map.overlayMapTypes.setAt(1,null);
        map.overlayMapTypes.setAt(2,null);
        map.overlayMapTypes.setAt(3,null);
	});
					
	//CLOSING POPUP
	//Click the x event!
	$("#popupContactClose").click(function(event){
		event.preventDefault();
		disablePopup();
	});
	//Click out event!
	$("#backgroundPopup").click(function(){
		disablePopup();
	});
	//Press Escape event!
	$(document).keypress(function(e){
		if(e.keyCode==27 && popupStatus==1){
			disablePopup();
		}
	});

});