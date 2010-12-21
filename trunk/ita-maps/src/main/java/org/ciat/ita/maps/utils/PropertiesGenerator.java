package org.ciat.ita.maps.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PropertiesGenerator {
	
	private String fileName;
	
	public PropertiesGenerator(String file){
	this.fileName=file;	
	}
	
	public void write() throws IOException{
			
		
	    FileWriter fstream = new FileWriter(fileName);
	    
        BufferedWriter out = new BufferedWriter(fstream);
    out.write("#archivo de configuracion generado automaticamente - Config file generated by default"
    +"\r\n"
    +"#######################\r\n" 
    +"# Menu language\r\n" 
    +"#######################\r\n" 
    +"language=english"
    +"\r\n"
    +"#######################\r\n" 
    +"# PATH\r\n" 
    +"#######################\r\n" 
    +"path.source=c:/data/\r\n" 
    +"path.target=c:/iabinmaps/\r\n" 
    +"\r\n" 
    +"bioclim.path=bioclim/\r\n" 
    +"threat.path=threat/\r\n" 
    +"species.path=species/\r\n" 
    +"summaries.path=summaries/\r\n" 
    +"summaries.kmloutputpath=protectedArea/\r\n" 
    +"\r\n" 
    +"#######################\r\n" 
    +"# RASTERS\r\n" 
    +"#######################\r\n" 
    +"rasters=p1;p2\r\n" 
    +"p1.group=bioclim\r\n" 
    +"p1.filename=P1.asc\r\n" 
    +"p1.descripcion=Annual mean temperature �C\r\n" 
    +"p2.group=bioclim\r\n" 
    +"p2.filename=P2.asc\r\n" 
    +"p2.descripcion=Mean diurnal temperature range �C\r\n" 
    +"#######################\r\n" 
    +"# SHAPEFILES\r\n" 
    +"#######################\r\n" 
    +"shapes=pa\r\n" 
    +"pa.group=summaries\r\n" 
    +"pa.shapefile=SurAmerica.shp\r\n" 
    +"\r\n" 
    +"#######################\r\n" 
    +"# ZOOM\r\n" 
    +"#######################\r\n" 
    +"zoom.min=0\r\n" 
    +"zoom.max=1\r\n" 
    +"\r\n" 
    +"#######################\r\n" 
    +"# KML\r\n" 
	+"#######################\r\n" 
	+"\r\n" 
	+"style.url=style.url=http://gisweb.ciat.cgiar.org/ita/style/estilo.kml#estilo\r\n" 
	+"style1.url=http://gisweb.ciat.cgiar.org/ita/style/estilo.kml#estilo1\r\n"
	+"\r\n" 
	+"#######################\r\n" 
	+"# PROTECTED AREA\r\n" 
	+"#######################\r\n" 
	+"pa.server.url=http://gisweb.ciat.cgiar.org/ita/protected-areas/pa/\r\n" 
	+"pa.shape.column.indexes=2;4;5;20\r\n" 
	+"pa.shape.column.names=Name;Country;State;UNCAT\r\n" 
	+"pa.kml.main=mainKML.kml\r\n" 
	+"\r\n" 
	+"#######################\r\n" 
	+"# COLORS\r\n" 
	+"#######################\r\n" 
	+"p1.color.type=temperature\r\n" 
	+"p2.color.type=precipitation\r\n" 
	+"\r\n" 
	+"temperature.color.min= 255;255;0\r\n" 
	+"temperature.color.max= 255;0;0\r\n" 
	+"\r\n" 
	+"temperature.scale=quantiles\r\n" 
	+"temperature.scale.quantiles=5\r\n" 
	+"\r\n" 
	+"precipitation.color.min= 255;255;255\r\n" 
	+"precipitation.color.max= 0;0;255\r\n" 
	+"\r\n" 
	+"precipitation.scale=continuous\r\n" );
    out.flush();
	out.close();
	
	}

}
