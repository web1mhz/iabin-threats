package org.ciat.ita.maps.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class CopyUtility {

	private final static String[] ranges = new String[] {"6000442-6000552" };
	private final static String sourcePath = "\\\\172.22.33.85\\geodata\\Threat-Assement\\species";
	private final static String destinationPath = "\\\\172.22.33.85\\geodata\\ITA\\generated-files\\species";
	//private final static String destinationPath = "D:\\IABIN\\generated_files\\species";
	

	public static void main(String[] args) {
		System.out.println("Loading species directories...");
		Arrays.sort(ranges);		
		File sourceSpecie = null;
		File convexHull, full, fullThreshold;
		for(String range : ranges) {
			String[] minMax = range.split("-");
			int min = Integer.parseInt(minMax[0]);
			int max = Integer.parseInt(minMax[1]);
			for(int currentSpecie = min ; currentSpecie <= max; currentSpecie++) {
				sourceSpecie = new File(sourcePath, ""+currentSpecie);
				if(sourceSpecie.exists()) {
					System.out.println("Specie: "+currentSpecie);
					convexHull = new File(sourceSpecie, "dist_limited_to_convex_hull");
					if(convexHull.exists()) {
						System.out.print("   copying /dist_limited_to_convex_hull");
						copyDirectory(convexHull, new File(destinationPath, currentSpecie+File.separator+"dist_limited_to_convex_hull"));						
						System.out.println("\n     Finished");
					}
					full = new File(sourceSpecie, "full");
					if(full.exists()){
						System.out.print("   copying /full");
						copyDirectory(full, new File(destinationPath, currentSpecie+File.separator+"full"));						
						System.out.println("\n     Finished");
					}
					fullThreshold = new File(sourceSpecie, "full_with_threshold");
					if(fullThreshold.exists()) {
						System.out.print("   copying /full_with_threshold");
						copyDirectory(fullThreshold, new File(destinationPath, currentSpecie+File.separator+"full_with_threshold"));						
						System.out.println("\n     Finished");
					}
				}
			}
			
		}	
	}

	private static void copyDirectory(File src, File dest) {
		if(src.isDirectory()) {
			if(!dest.exists()) {
				dest.mkdirs();
			}
			File[] files = src.listFiles();
			for(File file : files) {
				copyDirectory(file, new File(dest, file.getName()));
			}
		} else {
			try {
				InputStream in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];				
				for(int length = in.read(buffer); length > 0; length=in.read(buffer)) {
					out.write(buffer, 0, length);
				}
				System.out.print(".");
				in.close();
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {		
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("There was an error trying to copy "+src.getAbsolutePath());
				e.printStackTrace();
			}
			
		}
	}
}
