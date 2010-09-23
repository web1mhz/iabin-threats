package csv2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import de.micromata.opengis.kml.v_2_2_0.Boundary;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class Csv2Point {

	private LinkedList<String[]> lista;

	public Csv2Point(LinkedList<String[]> lista) {
		this.lista=lista;
	}

	public void createKML(String path) throws FileNotFoundException {

		String descripcion = "descripcion";

		final Kml kml = new Kml();

		path = "d:";// este viene de properties

		String filename = path + File.separator + "csv2punto.kml";
		filename = "d:/csv2punto.kml";	

		for (String[] s : lista) {
			// try{
			Placemark p = kml
					.createAndSetPlacemark()
					.withName("Name")
					.withDescription(descripcion)
					.withStyleUrl(
							"http://wikipedia.agilityhoster.com/estilo.kml#estilo");
			// p.createAndSetPoint().addToCoordinates(Double.parseDouble(longitude[i]),Double.parseDouble(latitude[i]));
			// }catch(Exception e){ System.out.println(i +" "+longitude[i]);}
			Point pun = p.createAndSetPoint();
			pun.addToCoordinates(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			
			List<de.micromata.opengis.kml.v_2_2_0.Coordinate> kmlCoords = pun.createAndSetCoordinates();
										
				Coordinate coord=new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
					kmlCoords.add(new de.micromata.opengis.kml.v_2_2_0.Coordinate(coord.x, coord.y));
					
			}
			
			
			// System.out.println(i);

		

		System.out.println("estoy enfermo!!");
		kml.marshal();
		kml.marshal(new File(filename));

		/*
		 * final Boundary bound = new Boundary(); final LinearRing lin =
		 * bound.createAndSetLinearRing(); Polygon pol =
		 * p.createAndSetPolygon();
		 * 
		 * List<Coordinate> kmlCoords = lin.createAndSetCoordinates();
		 * 
		 * MultiPolygon polygons = (MultiPolygon) sf.getAttribute(0); for (int i
		 * = 0; i < polygons.getNumGeometries(); i++) { Geometry geo =
		 * polygons.getGeometryN(i); com.vividsolutions.jts.geom.Coordinate[]
		 * shapeCoords = geo .getCoordinates();
		 * 
		 * for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords)
		 * 
		 * kmlCoords.add(new Coordinate(coord.x, coord.y)); }
		 * 
		 * 
		 * pol.setOuterBoundaryIs(bound);
		 * 
		 * kml.marshal(); kml.marshal(new File(filename));
		 */
	}

}
