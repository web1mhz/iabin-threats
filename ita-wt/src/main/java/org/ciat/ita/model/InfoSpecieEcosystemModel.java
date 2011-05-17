package org.ciat.ita.model;

import java.util.ArrayList;

public class InfoSpecieEcosystemModel {

	

	public InfoSpecieEcosystemModel(ArrayList<String> specieEcosystem) {
		super();
		this.specieEcosystem = new ArrayList<String>();
	}

	private ArrayList<String> specieEcosystem ;

	public void addEcosystem(String name) {
		specieEcosystem.add(name);
	}

	public ArrayList<String> getEcosystems() {
		return specieEcosystem;
	}
}
