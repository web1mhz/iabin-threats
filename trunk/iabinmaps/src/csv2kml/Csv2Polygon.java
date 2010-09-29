package csv2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class Csv2Polygon {

	private LinkedList<String[]> listaChull;
	private LinkedList<String[]> listaChullBuff;
	private String estilo;

	public Csv2Polygon(LinkedList<String[]> listaChull,
			LinkedList<String[]> listaChullBuff, String estilo) {

		this.estilo = estilo;
		this.listaChull = listaChull;
		this.listaChullBuff = listaChullBuff;

	}

	public void createKML(String path) throws FileNotFoundException {

		String filename = "d:/poligono.kml";
		Placemark placemark = KmlFactory.createPlacemark().withName(("Name"))
				.withDescription("descripcion").withStyleUrl(estilo);

		Kml kml = new Kml();

		Polygon pol = placemark.createAndSetPolygon();
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();

		for (String[] s : listaChull) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.setOuterBoundaryIs(bound);

		kmlCoords = lin.createAndSetCoordinates();
		for (String[] s : listaChullBuff) {
			Coordinate coord = new Coordinate(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
			kmlCoords.add(coord);
		}
		pol.addToInnerBoundaryIs(bound);

		kml.setFeature(placemark);// se registra el folder al kml

		kml.marshal();// se imprime kml en consola
		kml.marshal(new File(filename));// se guarda kml en archivo

	}

}
