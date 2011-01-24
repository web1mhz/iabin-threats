<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".element").click(function(event) {			
			var $target = $(event.target);
			$.ajax({
				  url: 'servicio.do',
				  data: "id="+$target.attr("id"),
				  success: function(info) {					  
				    alert('Mensaje de Regreso:\n'+info);
				  }
			});
			//alert("ID = "+$target.attr("id")+"\nVALUE = "+$target.text());
		});
	});
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Menu</title>
</head>
<body>
TAXON LIST
<ul>
	<li id="10" class="element">Animalia</li>
	<ul>
		<li id="20" class="element">Puma con color</li>
		<li id="30" class="element">Elefantus</li>
	</ul>
	<li id="40" class="element">Plantae</li>
	<ul>
		<li id="50" class="element">Cajanus</li>
		<li id="60" class="element">Plantus</li>
	</ul>
</ul>
</body>
</html>