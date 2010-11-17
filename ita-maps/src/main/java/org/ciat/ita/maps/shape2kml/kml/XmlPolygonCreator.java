package org.ciat.ita.maps.shape2kml.kml;

	import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
	import java.io.IOException;
	import java.util.HashMap;

import org.geotools.feature.FeatureIterator;
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
		
		public void createXML(FeatureIterator<SimpleFeature> fi) throws IOException {

			Element states = new Element("states");
			
			

			
			int count = 3000;
			SimpleFeature sf = null;
			MultiPolygon polygons;
			int contad=0;
			int contad2=0;
			while (fi.hasNext() && count-- > 0) {
				sf = fi.next();
	
				
				polygons = (MultiPolygon) sf.getAttribute(0);
				
				for (int i = 0; i < polygons.getNumGeometries(); i++) {//recorre cada objeto del sf, el primer atributo es poligono
					Geometry geo = polygons.getGeometryN(i);
					com.vividsolutions.jts.geom.Coordinate[] shapeCoords = geo.getCoordinates();
					Element state = new Element("state");
								
					state.setAttribute("name", atributos.get(2));
					state.setAttribute("country", atributos.get(4));
					state.setAttribute("uncat", atributos.get(5));
					state.setAttribute("state", atributos.get(20));
					state.setAttribute("colour", "#ff0000");

					for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords){ //recorre los puntos de cada poligono
						
						Element point = new Element("point");
						point.setAttribute("lat", Double.toString(coord.x) );
						point.setAttribute("lng", Double.toString(coord.y) );
						state.addContent(point);
						System.out.println("punto : "+coord.x+" "+coord.y);
						System.out.println(contad++);
					}
					System.out.println("state "+sf.getAttribute(6)+" "+contad2++);
					states.addContent(state);
				}
				
				

				System.out.print(sf.getAttribute(4) + " ");
				System.out.println(sf.getAttribute(5));

			}
			
			System.out.println("0:"+sf.getAttribute(0));
			System.out.println("1:"+sf.getAttribute(1));
			System.out.println("2:"+sf.getAttribute(2));
			System.out.println("3:"+sf.getAttribute(3));
			System.out.println("4:"+sf.getAttribute(4));
			System.out.println("5:"+sf.getAttribute(5));
			System.out.println("6:"+sf.getAttribute(6));
			System.out.println("7:"+sf.getAttribute(7));
			System.out.println("8:"+sf.getAttribute(8));
			System.out.println("9:"+sf.getAttribute(9));
			System.out.println("10:"+sf.getAttribute(10));
			System.out.println("11:"+sf.getAttribute(11));
			System.out.println("12:"+sf.getAttribute(12));
			System.out.println("13:"+sf.getAttribute(13));
			System.out.println("14:"+sf.getAttribute(14));
			System.out.println("15:"+sf.getAttribute(15));
			System.out.println("16:"+sf.getAttribute(16));
			System.out.println("17:"+sf.getAttribute(17));
			System.out.println("18:"+sf.getAttribute(18));
			System.out.println("19:"+sf.getAttribute(19));
			System.out.println("20:"+sf.getAttribute(20));
			System.out.println("21:"+sf.getAttribute(21));
			
			
			
			

			Document doc = new Document(states);
			XMLOutputter outp = new XMLOutputter();
			outp.setFormat(Format.getPrettyFormat());
			FileOutputStream file2;
			
			file2 = new FileOutputStream("d:/file.xml");
			
			outp.output(doc, file2);

			file2.flush();
			file2.close();
			
		


		
		}

	}



