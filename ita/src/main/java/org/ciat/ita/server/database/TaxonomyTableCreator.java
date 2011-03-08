package org.ciat.ita.server.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ciat.ita.server.ServerConfig;

import com.mysql.jdbc.Connection;

public class TaxonomyTableCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("Not enough arguments.");
			System.exit(0);
		}else{
			File taxons=new File(args[0]);
			if(!taxons.exists()){
				System.out.println("File "+args[0]+" doesn't exists");
				System.exit(0);
			}else{
				start(args);
			}
		}

	}

	private static void start(String[] args) {

		try {
			Scanner file=new Scanner(new File(args[0]));
			DataBaseManager.registerDriver();
			java.sql.Connection conx=DataBaseManager.openConnection(ServerConfig.getInstance().database_user, ServerConfig.getInstance().database_password);
			
			String linea=file.nextLine();
			String split[];
			String kingdomID="";
			String classID="";
			String familyID;
			String familyName;
			String genusID;
			String genusName;
			String specieID;
			String specieName;
			String type;
			while(file.hasNextLine()){
				linea=file.nextLine();
				split=linea.split("[,]");
				familyID=split[0].charAt(0)+"55"+split[0].substring(3);
				familyName=split[1];
				genusID=split[2].charAt(0)+"33"+split[2].substring(3);
				genusName=split[3];
				specieID=split[4];
				specieName=split[5];
				type=split[7];
				
				 if(type.equalsIgnoreCase("Insects")){
					 classID="13140937";
					 kingdomID="13140803";
				 }
				 if(type.equalsIgnoreCase("Reptiles")){
					 classID="13140958";
					 kingdomID="13140803";
				 }
				 if(type.equalsIgnoreCase("Birds")){
					 classID="13140955";
					 kingdomID="13140803";
				 }
				 if(type.equalsIgnoreCase("Amphibia")){
					 classID="13140952";
					 kingdomID="13140803";
				 }
				 if(type.equalsIgnoreCase("Mammals")){
					 classID="13140957";
					 kingdomID="13140803";
				 }
				 if(type.equalsIgnoreCase("Plants")){
					 classID="0";
					 kingdomID="13140804";
				 }

				 //insert specie
				 DataBaseManager.makeChange("insert into IABIN_taxon_name " +
						 "(id, canonical,rank ) values (" +
						 specieID+",'"+specieName+"',7000)", conx);
				 
				DataBaseManager.makeChange("insert into IABIN_taxon_concept " +
						"(id, kingdom_concept_id, class_concept_id," +
						"family_concept_id, genus_concept_id, species_concept_id, " +
						"taxon_name_id,rank ) values (" +
						specieID+","+kingdomID+","+classID+","+familyID+","+genusID+
						","+specieID+","+specieID+",7000)", conx);
				
				
				//insert genus
				DataBaseManager.makeChange("insert IGNORE into IABIN_taxon_name " +
						"(id, canonical,rank ) values (" +
						genusID+",'"+genusName+"',6000)", conx);
				
				DataBaseManager.makeChange("insert IGNORE into IABIN_taxon_concept " +
						"(id, kingdom_concept_id, class_concept_id," +
						"family_concept_id, genus_concept_id, " +
						"taxon_name_id, rank ) values (" +
						genusID+","+kingdomID+","+classID+","+familyID+","+genusID+
						","+genusID+",6000)", conx);
				
				//insert family
				DataBaseManager.makeChange("insert IGNORE into IABIN_taxon_concept " +
						"(id, kingdom_concept_id, class_concept_id," +
						"family_concept_id," +
						"taxon_name_id,rank ) values (" +
						familyID+","+kingdomID+","+classID+","+familyID+
						","+familyID+",5000)", conx);
				
				DataBaseManager.makeChange("insert IGNORE into IABIN_taxon_name " +
						"(id, canonical,rank ) values (" +
						familyID+",'"+familyName+"',5000)", conx);
				 
		
				
				
			}
			
			DataBaseManager.closeConnection(conx);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	
		
	}

}
