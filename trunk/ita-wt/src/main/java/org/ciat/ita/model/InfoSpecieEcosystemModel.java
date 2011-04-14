package org.ciat.ita.model;

import java.util.ArrayList;

public class InfoSpecieEcosystemModel {		
	
	private ArrayList<String> specieEcosystem = new ArrayList<String>();
	
	public void addEcosystem(String name){
		specieEcosystem.add(name);
	}
	
	public ArrayList<String> getEcosystems(){
		return specieEcosystem;
	}
	
}
