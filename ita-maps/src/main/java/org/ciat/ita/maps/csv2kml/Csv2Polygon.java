package org.ciat.ita.maps.csv2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class Csv2Polygon {

	private LinkedList<String[]> listaChull;
	private LinkedList<String[]> listaChullBuff;
	private String estilo;
	private String estilo1;

	public Csv2Polygon(LinkedList<String[]> listaChull,
			LinkedList<String[]> listaChullBuff, String estilo, String estilo1) {

		this.estilo = estilo;
		this.estilo1=estilo1;
		this.listaChull = listaChull;
		this.listaChullBuff = listaChullBuff;

	}

	public void createKML(String path, String archivo, String estilo) throws FileNotFoundException {

		
		String ruta = path + archivo+"-pol.kml";
		//filename = "d:/poligono.kml";

		Kml kml = new Kml();
		Folder folder =kml.createAndSetFolder();
		Placemark placemark=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo);

		Polygon pol = placemark.createAndSetPolygon();
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();

		for (String[] s : listaChull) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.setOuterBoundaryIs(bound);

		//*****************************************************************
	
		Placemark placemark2=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo1);
		Polygon pol2 = placemark2.createAndSetPolygon();
		final Boundary bound2 = new Boundary();
		final LinearRing lin2 = bound2.createAndSetLinearRing();
		List<Coordinate> kmlCoords2 = lin2.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords2.add(coord);
		}
		pol2.setOuterBoundaryIs(bound2);
		//********************************************************************
		
	/*	kmlCoords = lin.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.addToInnerBoundaryIs(bound);*/
	
		kml.setFeature(folder);// se registra el folder al kml

		File dir = new File(path);
		dir.mkdirs();
		
		//kml.marshal();// se imprime kml en consola
		kml.marshal(new File(ruta));// se guarda kml en archivo

	}
	
public void createKMLchull(String path, String archivo, String estilo) throws FileNotFoundException {

		
		String ruta = path + archivo+"-chull.kml";
		//filename = "d:/poligono.kml";

		Kml kml = new Kml();
		Folder folder =kml.createAndSetFolder();
		Placemark placemark=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo);

		Polygon pol = placemark.createAndSetPolygon();
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();

		for (String[] s : listaChull) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.setOuterBoundaryIs(bound);
		

		//*****************************************************************
	
	/*	Placemark placemark2=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo1);
		Polygon pol2 = placemark2.createAndSetPolygon();
		final Boundary bound2 = new Boundary();
		final LinearRing lin2 = bound2.createAndSetLinearRing();
		List<Coordinate> kmlCoords2 = lin2.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords2.add(coord);
		}
		pol2.setOuterBoundaryIs(bound2);
	*/	
		//********************************************************************
		
	/*	kmlCoords = lin.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.addToInnerBoundaryIs(bound);*/
	
		kml.setFeature(folder);// se registra el folder al kml

		File dir = new File(path);
		dir.mkdirs();
		
		//kml.marshal();// se imprime kml en consola
		kml.marshal(new File(ruta));// se guarda kml en archivo

	}
	
	public void createKMLchullbuff(String path, String archivo, String estilo1) throws FileNotFoundException {

		
		String ruta = path + archivo+"-chullbuff.kml";
		//filename = "d:/poligono.kml";

		Kml kml = new Kml();
		Folder folder =kml.createAndSetFolder();
		Placemark placemark=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo1);

		Polygon pol = placemark.createAndSetPolygon();
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();

		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.setOuterBoundaryIs(bound);
		

		//*****************************************************************
	
	/*	Placemark placemark2=folder.createAndAddPlacemark().withName(("Name"))
		.withDescription("descripcion").withStyleUrl(estilo1);
		Polygon pol2 = placemark2.createAndSetPolygon();
		final Boundary bound2 = new Boundary();
		final LinearRing lin2 = bound2.createAndSetLinearRing();
		List<Coordinate> kmlCoords2 = lin2.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords2.add(coord);
		}
		pol2.setOuterBoundaryIs(bound2);
	*/	
		//********************************************************************
		
	/*	kmlCoords = lin.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.addToInnerBoundaryIs(bound);*/
	
		kml.setFeature(folder);// se registra el folder al kml

		File dir = new File(path);
		dir.mkdirs();
		
		//kml.marshal();// se imprime kml en consola
		kml.marshal(new File(ruta));// se guarda kml en archivo

	}
	

}
