package org.ciat.ita.maps.tilecutter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ciat.ita.maps.tilecutter.raster.Raster;
import org.ciat.ita.maps.tilecutter.tile.ImageManager;
import org.ciat.ita.maps.tilecutter.tile.TileManager;
import org.ciat.ita.maps.tilecutter.tile.colormanager.ColorManager;
import org.ciat.ita.maps.tilecutter.tile.colormanager.ColorManagerFactory;
import org.ciat.ita.maps.utils.PropertiesManager;
import org.geotools.data.collection.ListFeatureCollection;

/**
 * Clase principal (Main)
 * 
 * @author supportadmin
 * 
 */
public class TileCutter {
	static boolean control=false;

	public static void main(String arg[]) {
		
		control=true;
		try {
			execute(arg[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void execute(String arg) throws IOException{
		

		if(control==true) PropertiesManager.register(arg);

		String[] rastersID = PropertiesManager.getInstance().getPropertiesAsStringArray("rasters");
		Raster raster;
		ImageManager iManager;

		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		
		

		for (String rasterID : rastersID) {
			raster = new org.ciat.ita.maps.tilecutter.raster.Raster();
			 
			String descripcion = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".descripcion");
			String group = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".filename");

			iManager = new ImageManager(targetPath + pathGroup + rasterID);
			TileManager tManager = new TileManager(iManager);
			raster.loadRaster(sourcePath + pathGroup + fileName);
			ColorManager cManager = ColorManagerFactory.createColorManager(rasterID, raster);
			iManager.setColorManager(cManager);

			BufferedImage scaleImage = cManager.getScaleImage(descripcion);

			File dir = new File(targetPath+ pathGroup + rasterID + File.separator);
			dir.mkdirs();
			try {
				ImageIO.write(scaleImage, "png", new File(targetPath+ pathGroup + rasterID + File.separator + rasterID+ "scaleTestImage.png"));
			} catch (IOException e) { e.printStackTrace();
			}
			int zoomMin = PropertiesManager.getInstance().getPropertiesAsInt("zoom.min");
			int zoomMax = PropertiesManager.getInstance().getPropertiesAsInt("zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
		}//fin for
		
		

		

	
	}
	
	//converts the raster (.asc) files in the folder specified in properties file, to tile images
	public static void createSpeciesDistributionImages(String arg, String min, String max) throws IOException, NumberFormatException{
		
		if(control==true) PropertiesManager.register(arg);

		Raster raster;
		ImageManager iManager;

		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		String species = PropertiesManager.getInstance().getPropertiesAsString("species.path");
		String speciesDistribution = PropertiesManager.getInstance().getPropertiesAsString("speciesDistribution");
		if (speciesDistribution.equalsIgnoreCase("true")) System.out.println("se recorrerá la carpeta de especies");

		
		File folder = new File(sourcePath + species);
		System.out.println("folder: "+folder);
		File[] listOfFiles;
		if(min != null && max != null) {
			listOfFiles = folder.listFiles(new CustomPathFilter(min, max));
		} else {
			listOfFiles = folder.listFiles();
		}
		
		for (File s : listOfFiles) {
			if (s.isDirectory()) {
		
			String rasterID="speciesDistribution";
			raster = new org.ciat.ita.maps.tilecutter.raster.Raster();
			 
			String group = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group + ".path");
			
			
			//****************************************_full_with_threshold.asc
			
			String fileName = (s.getName()+File.separator + s.getName()+"_full_with_threshold.asc");
			System.out.println("filename: "+fileName);
			
			iManager = new ImageManager(targetPath + pathGroup + s.getName()+"/full_with_threshold/");
			TileManager tManager = new TileManager(iManager);
			raster.loadRaster(sourcePath + pathGroup + fileName);
			ColorManager cManager = ColorManagerFactory.createColorManager(rasterID, raster);
			iManager.setColorManager(cManager);

			int zoomMin = PropertiesManager.getInstance().getPropertiesAsInt("zoom.min");
			int zoomMax = PropertiesManager.getInstance().getPropertiesAsInt("zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
			//***************************************dist. limited to convex hull
			
			 fileName = (s.getName()+File.separator + s.getName()+".asc");
			System.out.println("filename: "+fileName);
			
			iManager = new ImageManager(targetPath + pathGroup + s.getName()+"/dist_limited_to_convex_hull/");
			tManager = new TileManager(iManager);
			raster.loadRaster(sourcePath + pathGroup + fileName);
			cManager = ColorManagerFactory.createColorManager(rasterID, raster);
			iManager.setColorManager(cManager);

			zoomMin = PropertiesManager.getInstance().getPropertiesAsInt("zoom.min");
			zoomMax = PropertiesManager.getInstance().getPropertiesAsInt("zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
		
			//***************************************_full.asc
			
			 fileName = (s.getName()+File.separator + s.getName()+"_full.asc");
			System.out.println("filename: "+fileName);
			
			iManager = new ImageManager(targetPath + pathGroup + s.getName()+"/full/");
			tManager = new TileManager(iManager);
			raster.loadRaster(sourcePath + pathGroup + fileName);
			cManager = ColorManagerFactory.createColorManager(rasterID, raster);
			iManager.setColorManager(cManager);

			zoomMin = PropertiesManager.getInstance().getPropertiesAsInt("zoom.min");
			zoomMax = PropertiesManager.getInstance().getPropertiesAsInt("zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
			
			
		
			}//fin if
		}//fin for
		
		
	}
	
}
