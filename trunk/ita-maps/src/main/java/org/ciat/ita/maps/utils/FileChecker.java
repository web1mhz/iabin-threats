package org.ciat.ita.maps.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class FileChecker {

	private final static String speciesPath = "\\\\172.22.33.85\\geodata\\ITA\\generated-files\\species\\";
	private final static String logFile = "C:\\Documents and Settings\\hftobon\\Desktop\\log.txt";
	private static PrintWriter logOut;
	
	public static void main(String[] args) throws FileNotFoundException {
		logOut = new PrintWriter(logFile);
		File path = new File(speciesPath);
		if(path.exists() && path.isDirectory()) {
			File[] species = path.listFiles(new FileFilter() {				
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			Arrays.sort(species, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}				
			});
			for(File specie : species) {
				System.out.println("checking specie "+specie.getName());
				File[] files = specie.listFiles();
				boolean full_threshold = false;
				boolean limited_convex = false;
				boolean full = false;
				boolean chull = false;
				boolean chullbuff = false;
				boolean point = false;
				for(File file : files) {				
					if(file.getName().equals("dist_limited_to_convex_hull")) {
						limited_convex = true;
						if(file.list().length < 8) {
							addLogMessage("File "+file.getAbsolutePath()+" does not have image directories.");
						}
					} else if(file.getName().equals("full_with_threshold")) {
						full_threshold = true;
						if(file.list().length < 8) {
							addLogMessage("File "+file.getAbsolutePath()+" does not have image directories.");
						}
					} else if(file.getName().equals("full")) {
						full = true;
						if(file.list().length < 8) {
							addLogMessage("File "+file.getAbsolutePath()+" does not have image directories.");
						}
					} else if(file.getName().equals(specie.getName()+"-chull.kml")) {
						chull = true;
					} else if(file.getName().equals(specie.getName()+"-chullbuff.kml")) {
						chullbuff = true;
					} else if(file.getName().equals(specie.getName()+"-point.kml")) {
						point = true;
					}
				}
				if(!limited_convex) {
					addLogMessage("Specie "+specie.getName()+" does not have /dist_limited_to_convex_hull directory");
				} else if(!full_threshold) {
					addLogMessage("Specie "+specie.getName()+" does not have /full_with_threshold directory");
				} else if(!full) {
					addLogMessage("Specie "+specie.getName()+" does not have /full directory");
				} else if(!chull) {
					addLogMessage("Specie "+specie.getName()+" does not have "+specie.getName()+"-chull.kml directory");
				} else if(!chullbuff) {
					addLogMessage("Specie "+specie.getName()+" does not have "+specie.getName()+"-chullbuff.kml directory");
				} else if(!point) {
					addLogMessage("Specie "+specie.getName()+" does not have "+specie.getName()+"-point.kml directory");
				}
			}
		} else {
			System.out.println("File does not exist or is not a directory.");
		}
		
		logOut.flush();
		logOut.close();

	}
	
	private static void addLogMessage(String message) {
		logOut.println(message);
		System.err.println(message);
	}

}
