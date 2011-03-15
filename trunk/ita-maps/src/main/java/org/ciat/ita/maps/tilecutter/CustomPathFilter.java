package org.ciat.ita.maps.tilecutter;

import java.io.File;
import java.io.FileFilter;

public class CustomPathFilter implements FileFilter {
	private int min, max;
	
	public CustomPathFilter(String min, String max) {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
	}
	
	@Override
	public boolean accept(File pathname) {
		if(Integer.parseInt(pathname.getName()) >= min && Integer.parseInt(pathname.getName()) <= max) {
			return true;
		}
		return false;
	}
}
