package org.ciat.ita.maps.shape2kml.kml;

import java.io.File;
import java.io.FileNotFoundException;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;

public class KmlGroupCreator {

	private final Kml kml;
	private final Folder folder;
	String url;
	
	public KmlGroupCreator(String url){
		kml= new Kml();
		folder=kml.createAndSetFolder();
		this.url=url;
		
	}
	
	public void addElement(String nombre){
		NetworkLink nLink=folder.createAndAddNetworkLink();
		nLink.createAndSetLink().withHref(url+nombre);
	}
	
	public void writeKml(String targetFile,String nombreGrupo) throws FileNotFoundException{
		kml.marshal();
		kml.marshal(new File(targetFile+File.separator+nombreGrupo));
	}
	
	
}
