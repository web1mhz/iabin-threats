package org.ciat.ita.maps.shape2kml.kml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.io.FileNotFoundException;

import org.opengis.feature.simple.SimpleFeature;

import org.ciat.ita.maps.shape2kml.shape.Shapefile;
import org.ciat.ita.maps.utils.PropertiesManager;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KmlPolygonCreator {

	/*
	 *  ruta de los kmls
	 */
	/**
	 * @uml.property  name="path"
	 */
	private String path;

	/*
	 * se recibe index, nombre de las columnas a mostrar
	 */
	/**
	 * @uml.property  name="atributos"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.String java.lang.String"
	 */
	private HashMap<Integer, String> atributos = new HashMap<Integer, String>();// 

	public KmlPolygonCreator(String path, HashMap<Integer, String> atributos) {
		this.path = path;
		File dir = new File(path);
		dir.mkdirs();
		this.atributos = atributos;
	}

	public void createKML(SimpleFeature sf) throws FileNotFoundException {

		Set<Integer> keySet = atributos.keySet();
		String descripcion = "<table border=\"1\" padding=\"3\" width=\"300\"><tr bgcolor= \"#D2D2D2\">";

		for (Integer i : keySet) {// se crean los titulos de la tabla de la informacion del poligono
			descripcion += "<td>" + atributos.get(i) + "</td>";
		}
		descripcion += "</tr><tr>";
		for (Integer i : keySet) {// se crean los contenidos de la tabla de la informacion del poligono
			descripcion += "<td>" + sf.getAttribute(i) + "</td>";
		}
		descripcion += "</tr></table>";

		final Kml kml = new Kml();

		String filename = path + File.separator + sf.getAttribute(1) + ".kml";

		final Placemark p = kml.createAndSetPlacemark().withName(atributos.get("Name")).withDescription(
				descripcion).withStyleUrl(PropertiesManager.getInstance().getPropertiesAsString("style.url"));
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		Polygon pol = p.createAndSetPolygon();

		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();

		MultiPolygon polygons = (MultiPolygon) sf.getAttribute(0);
		for (int i = 0; i < polygons.getNumGeometries(); i++) {
			Geometry geo = polygons.getGeometryN(i);
			com.vividsolutions.jts.geom.Coordinate[] shapeCoords = geo.getCoordinates();

			for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords)

				kmlCoords.add(new Coordinate(coord.x, coord.y));
		}

		pol.setOuterBoundaryIs(bound);
		System.out.println("...");
		kml.marshal(new File(filename));
		System.out.print("file done ");

	}

	// *********************************************************************
	public Placemark createKMLpointsInfo(SimpleFeature sf, Folder folder) throws FileNotFoundException {

		Set<Integer> keySet = atributos.keySet();
		String descripcion = "<table border=\"1\" padding=\"3\" width=\"300\"><tr bgcolor= \"#D2D2D2\">";

		for (Integer i : keySet) {// se crean los titulos de la tabla de la informacion del poligono
			descripcion += "<td>" + atributos.get(i) + "</td>";
		}
		descripcion += "</tr><tr>";
		for (Integer i : keySet) {// se crean los contenidos de la tabla de la informacion del poligono
			descripcion += "<td>" + sf.getAttribute(i) + "</td>";
		}
		descripcion += "</tr></table>";

		Placemark placemark = KmlFactory.createPlacemark();

		/**
		 * se recorre la lista generando las coordenadas y agregando al folder
		 */

		placemark.withName(atributos.get(1)).withDescription(descripcion);
		Point punto = Shapefile.getPointForMarker(sf);
		placemark.createAndSetPoint().addToCoordinates(punto.getX(), punto.getY());// se crean las coordenadas y se registran al placemark

		return placemark;

	}

}
