[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/WTInstallation?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/WTInstallation?wl=es)



## Introduction ##

This document is intended to people who has relation with the technical implementation of the project.


## Technical Requirements ##

  * 100GB of HD space available
  * Apache Tomcat 6.0, available for free at [http://tomcat.apache.org/](http://tomcat.apache.org/)
  * MySQL database server 5.0


### 1 Taxonomic database ###

The Web Tools use the taxonomic information stored on MySQL Server. An access to this information from the web tool must be granted. If you don't have this information and you want to import it, you may want to create a new database a import the dump which: [ita-wt.sql](ftp://ftp.ciat.cgiar.org/DAPA/ITA/www/public_downloads/ita-wt.sql)

> `mysql -u User -p  database < ita-wt.sql `


> #### 1.1 Creating your own database ####

> Additionally, you may create the database structure with your own information using a CSV file with the follwing header:

> Family ID, Family Name, Genus ID, Genus Name, Specie ID, Specie Name, Specie Type ID,  Specie Type Name

> Where _Specie Type Name_ refers to {Plant, Mammal, Reptile, Amphibian, Bird, Insect} and _Specie Type ID_ for a unique identification per each _Specie Type Name_

> First of all you must create a database and create the tables structure with sql: [ita-wt\_stub.sql](ftp://ftp.ciat.cgiar.org/DAPA/ITA/www/public_downloads/ita-wt_stub.sql)

> Once you have configurated the ita project ( see [Configuration](http://code.google.com/p/iabin-threats/wiki/Configuration)) and the CSV species file, you can run the following command with the file as parameter.

> `java -cp ita.jar org.ciat.ita.server.database.TaxonomyTableCreator species_file.csv`

#### 2 Building Project ####

You may get the complete packaged web in // TODO download war URL

If you want to build you own WAR of the project, you may want to get the sources using

> ` svn checkout https://iabin-threats.googlecode.com/svn/trunk/ita-wt ita-wt `

To update the source you may run:

> ` svn update`

> Once you have the sources, you may package the project using:

> ` mvn package`

This will generate a file with the compiled project ready to install in a web server. You can found the file in the folder ita-wt/target/ with the name ita-wt.war

#### Installing project in a web server ####

#### Installing in Apache Tomcat ####

Once you have installed your Apache Tomcat, and making sure that this web application server is not currently running, you may copy the war file got from the building process into the $TOMCAT/webapps directory of your Apache Tomcat installation.

If the server was up, you may shutdown it by running $TOMCAT/bin/shutdown.sh or $TOMCAT/bin/shutdown.bat according to your OS.

Then, you may want to follow the instructions of the Configure Project section to set the web tool an make it work.

To start Apache tomcat you may run $TOMCAT/bin/startup.bat or $TOMCAT/bin/startup.sh according to your OS.

#### 3 Configure Project ####

Once the installation is required settings for file paths and connection data of the database file ita-wt.properties

```
########################
# DATABASE CONFIGURATION configuration data includes the database, password, ip, port connectivity and name of the database.
########################

user=guest
pass=cajanus
ip=gisbif.ciat.cgiar.org
port=3306
database=gbif_sept2010

#######################
# PATH path to the files of text information of species and ecosystems, and png files path, kml used in the web tool.
#######################

# client (pictures, kml files, etc ...)
public_path = http://gisweb.ciat.cgiar.org/iabin-threats/ITA/generated-files/
```