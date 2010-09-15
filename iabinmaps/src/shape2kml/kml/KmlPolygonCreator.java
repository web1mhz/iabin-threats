package shape2kml.kml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.io.FileNotFoundException;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KmlPolygonCreator {

	/*
	 *  ruta de los kmls
	 */
	private String path;
	
	/*
	 * se recibe index, nombre de las columnas a mostrar
	 */
	private HashMap<Integer, String> atributos = new HashMap<Integer, String>();// 


	public KmlPolygonCreator(String path, HashMap<Integer, String> atributos) {
		this.path = path;
		File dir=new File(path);
		dir.mkdirs();
		this.atributos  = atributos;

	}

	public void createKML(SimpleFeature sf) throws FileNotFoundException {

		Set<Integer> keySet = atributos.keySet();
		String descripcion = "";

		for (Integer i : keySet) {
			descripcion += atributos.get(i) + " " + sf.getAttribute(i) + "<br>";
		}

		final Kml kml = new Kml();
	
		String filename = path+File.separator+ sf.getAttribute(1)+".kml";

		final Placemark p = kml.createAndSetPlacemark().withName(atributos.get("Name"))
				.withDescription(descripcion).withStyleUrl(
						"http://wikipedia.agilityhoster.com/estilo.kml#estilo");
		final Boundary bound = new Boundary();
		final LinearRing lin = bound.createAndSetLinearRing();
		Polygon pol = p.createAndSetPolygon();

		List<Coordinate> kmlCoords = lin.createAndSetCoordinates();
				
		MultiPolygon polygons = (MultiPolygon) sf.getAttribute(0);
		for (int i = 0; i < polygons.getNumGeometries(); i++) {
			Geometry geo = polygons.getGeometryN(i);
			com.vividsolutions.jts.geom.Coordinate[] shapeCoords = geo
					.getCoordinates();

			for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords)
			
				kmlCoords.add(new Coordinate(coord.x, coord.y));
		}

		
		pol.setOuterBoundaryIs(bound);

		kml.marshal();
		kml.marshal(new File(filename));
	

	}

	
}
