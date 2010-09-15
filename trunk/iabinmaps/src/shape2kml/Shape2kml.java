package shape2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import shape2kml.kml.KmlGroupCreator;
import shape2kml.kml.KmlPolygonCreator;
import shape2kml.shape.Shapefile;

public class Shape2kml {

	private Shapefile shp;

	public static void main(String[] args) {
		Shape2kml s2k = new Shape2kml();
		s2k.execute();

	}

	private void execute() {
		File file = new File("C:/shapefiles/suramerica/SurAmerica.shp");
		shp = new Shapefile(file);
		SimpleFeature sf = null;
		KmlGroupCreator grupo = new KmlGroupCreator(
				"http://wikipedia.agilityhoster.com/");

		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		int count = 20;

		HashMap<Integer, String> atributos = new HashMap<Integer, String>(); // =
																				// atributos;
		atributos.put(2, "Name");
		atributos.put(4, "Country");
		atributos.put(5, "Departamento");
		atributos.put(20, "Categoria de las naciones unidas");

		KmlPolygonCreator kml = new KmlPolygonCreator("c:/", atributos);

		while (fi.hasNext() && count-- > 0) {
			sf = fi.next();
			try {
				kml.createKML(sf);
				grupo.addElement(sf.getAttribute(1) + ".kml");
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			System.out.print(sf.getAttribute(4) + " ");
			System.out.println(sf.getAttribute(5));

		}

		try {
			grupo.writeKml("mainKml.kml");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
