package org.ciat.ita.maps.shape2kml.kml;

	import java.io.File;
	import java.io.IOException;
	import java.util.HashMap;
	import org.jdom.Document;
	import org.jdom.Element;
	import org.jdom.output.Format;
	import org.jdom.output.XMLOutputter;
	import org.opengis.feature.simple.SimpleFeature;
	import com.vividsolutions.jts.geom.Geometry;
	import com.vividsolutions.jts.geom.MultiPolygon;

	public class XmlPolygonCreator {

		/*
		 *  ruta de los kmls
		 */
		private String path;
		
		/*
		 * se recibe index, nombre de las columnas a mostrar
		 */
		private HashMap<Integer, String> atributos = new HashMap<Integer, String>();// 


		public XmlPolygonCreator(String path, HashMap<Integer, String> atributos) {
			this.path = path;
			File dir=new File(path);
			dir.mkdirs();
			this.atributos  = atributos;
		}
		
		public Document createXML(SimpleFeature sf) throws IOException {

			Element states = new Element("states");
			Element state = new Element("state");
			Element point = new Element("point");

			MultiPolygon polygons = (MultiPolygon) sf.getAttribute(0);
			for (int i = 0; i < polygons.getNumGeometries(); i++) {
				Geometry geo = polygons.getGeometryN(i);
				com.vividsolutions.jts.geom.Coordinate[] shapeCoords = geo.getCoordinates();
							
				state.setAttribute("name", atributos.get(1));
				state.setAttribute("country", atributos.get(2));
				state.setAttribute("uncat", atributos.get(3));
				state.setAttribute("state", atributos.get(4));
				state.setAttribute("colour", "#ff0000");

				for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords){
					point.setAttribute("lat", Double.toString(coord.x) );
					point.setAttribute("lng", Double.toString(coord.y) );
					state.addContent(point);
				}
					
				states.addContent(state);
			}

			Document doc = new Document(states);
			return doc;


		
		}

	}



