[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedure?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedure?wl=es)



# Introduction #

This tutorial will describe the different procedure steps in order to have a fully functional installation of the data filtering tool.

### 1 The configuration files ###
#### Description ####
First of all, the values of the  XML configuration files parameters _server\_config.xml_ and _client\_config.xml_ should be set.

If the configuration files have not been created yet, you can generate them with default values using the script described in the Technical Procedure section. You can then edit them to set the corresponding values.

Most of these parameters are files and directories  paths. Examples of default settings are in the project source code.

Please configure both files even if you just want to run it in a stand-alone mode.

**Outputs:** Configuration files: _server\_config.xml_ and _client\_config.xml_.

#### Technical procedure ####
To generate the default configuration files, run the script with java using the following parameters for the server part:

> `java -cp ita.jar org.ciat.ita.server.ServerConfig`

and for the clients part:

> `java -cp ita.jar org.ciat.ita.client.ClientConfig`

Then edit the files server\_config.xml and client\_config.xml


### 2 Basic filtering ###
#### Description ####
We first proceed to a basic filtering which excludes the records which have no latitude or longitude values, the ones which geospatial issue is different to zero, the ones which not stored as specimen, and optionally those which specie has less than ten records.

**Inputs:** MySQL table with the occurrence records of the database

**Outputs:** Several temporal tables (temp\_land\_1A, temp\_land\_2A, temp\_land\_3A, temp\_land\_4A and temp\_land\_5A ). The temp\_land\_4A table contains all the records that passed the filters, and temp\_land\_5A table is empty and prepared to be filled during the next step.

#### Technical procedure ####
Run into the database the sql query file named “QUERY\_basic\_filter.sql”

Command line example:

> `mysql –u User –p portal < QUERY_basic_filter.sql `

#### Finalize ####
Once the query has been run, the tables temp\_land\_1A, temp\_land\_2A and temp\_land\_3A are not needed anymore, so these can be drooped. The tables temp\_land\_4A and temp\_land\_5A will be used in the next step.

### 3 Getting the JGM file (optional) ###
#### Description ####
If you don't have the JGM file for the filtering non terrestrial species step, You may  download it from: //TODO download URL

Or you can create it using a mask ASCII file and the WorldMaskManager script.

#### Technical procedure ####
Run the script in java with the following parameters:

**Parameters:**
> `jgm` To run the conversion algorithm

> `-Xmx<BYTES>` bytes of Maximum java heap size

> `<MASK>` absolute path of the ascii mask file

> `-k <KILOMETERS>` from the coast considered as a "near land" distance

**Command line:**

> `java -XmxBYTES -cp ita.jar org.ciat.ita.client.manage.WorldMaskManager jgm -m MASK -k KILOMETERS`

Example, Creation of a JGM file in linux file system using a radius of 5 kilometers :

> `java -Xmx1000m -cp ita.jar org.ciat.ita.client.manage.WorldMaskManager jgm -m /home/inland_mask/inland_msk.asc -k 5 `


### 4 Filtering non terrestrial species ###
#### Description ####
The records from non terrestrial species are not possible to evaluate. Consequently, the non terrestrial species should be discriminated and the terrestrial ones will be stored in the _temp\_land\_5A_ table.

A reliable method to do this, would be to knowing the _nub\_concept\_id_ of all non terrestrial species and filter the records from _temp\_land\_4A_ and insert just  terrestrial ones in the _temp\_land\_5A_ table. But all these nub\_concept\_id are hard to know. So in this step, the non terrestrial species will be determinated as follow:

It determinates for each record  if it is terrestrial or not. by using a land mask. Once all records has been evaluated,  it determinate the percent of non terrestrial records for that specie, if this percent is equal or upper than 90%, then the specie is consider as non terrestrial. Just the terrestrial species records are inserted in the temp\_land\_5A table.


**Inputs:** _temp\_land\_4A_ table records,land mask file “inland\_msk.jgm”

**Outputs:** _temp\_land\_5A_ table filled with the records of terrestrial species.

#### Technical procedure ####
Run the script in java with the following parameters:

> `-Xmx<BYTES>` of Maximum java heap size

> `terreval` string to filter non terrestrial species

> `-n <NUMBER>` of records that should be analyzed to show the time statistics

Command line:

> `java -XmxBYTES -cp ita.jar org.ciat.ita.client. manage.WorldMaskManager terreval -n NUMBER`

example:

> `java -Xmx1000m -cp ita.jar org.ciat.ita.client.manage.WorldMaskManager terreval -n 100000`

**Finalize:** Once the script has been run, the table temp\_land\_4A is not needed anymore. It can therefore be dropped.


### 5 Getting EMM file ###

If you don't have the EMM All in One file (BioAIO.emm) required for the Geospatial Evaluation step, you may download from [here](http://gisweb.ciat.cgiar.org/ITA/public_downloads/BioAIO.zip)

Or you can create it using the original ASCII files of the bioclimatic variables specified in the XML Client configuration and the EnvironmentMaskManger script.

#### Technical procedure ####
**Parameters:**

> `-d <VARS>` absolute path of the environmental variables folder

**Command line:**

> `java -XmxBYTES -cp ita.jar org.ciar.ita.client. manage.EnvironmentMaskManager -d VARS_FOLDER`

Example for Linux:

> `java -Xmx1000m -cp ita.jar org.ciat.ita.client. manage.EnvironmentMaskManager -d /home/bio_asc`

### 6 Geospatial evaluation ###
#### Description ####
Evaluates the records using geospatial resources.

**Inputs:** _temp\_land\_5A_ table filled with the records of terrestrial species. Shape file _GADM\_v0-6.shp_, land mask file _inland\_msk.jgm_ (jgm format is necesary) and environmental variables file _BioAIO.emm_ and the format file _BioAIO.format_

**Outputs:** Two tables temp\_good\_records and temp\_bad\_records filled with the data according to the evaluation results. If the record passed all the filters, its data with the corresponding environmental data should be in the temp\_good\_records table. On the contrary, if the records were recognized as unreliable its data should be in the temp\_bad\_records table with the corresponding type of error found.

Error possible values:
**M:** Out of Mask
**NC:** Null Country
**WC:** Wrong Country
**O:** In Ocean
**NL:** Near Land

#### Technical procedure ####
First, you should run  the sql file named “QUERY\_create\_GBtables.sql” into the database

example:

> `mysql portal < QUERY_create_GBtables.sql `

Once the tables are created, You have the choice to run the geospatial script in standalone mode or client-server mode.


### Standalone ###

In standalone mode you just need to run a single instance.

**Parameters:**
> `-Xmx<BYTES>` of Maximum java heap size

**Command line:**

> `java -XmxBYTES -jar ita.jar`

example

> `java -Xmx1500m -jar ita.jar`

### Client-Server ###

First of all you have to run one instance as a server, the you can run several client instances as you wish.

**Parameters:**
> `-Xmx<BYTES>` of Maximum java heap size

**Server command line:**

> `java -XmxBYTES -cp ita.jar org.ciat.ita.server.Server `

example

> `java -Xmx20000m -cp ita.jar org.ciat.ita.server.Server `

**Client command line:**

> `java -XmxBYTES -cp ita.jar org.ciat.ita.client.Client `

example

> `java -Xmx1500m -cp ita.jar org.ciat.ita.client.Client `

**Finalize:** Once the script has been run, the table temp\_land\_5A is not needed anymore, so it can be dropped.



### 7 Environmental evaluation ###
#### Description ####
Within each specie calculate the maximum value and the minimum value of each environmental variable to determinate when the record is an outlier for that variable.


**Inputs:** _temp\_good\_records_ table filled with the geospatially reliable records data and the corresponding environmental data

**Outputs:** _temp\_good\_records_ table with the count of the outlier for all variables

#### Technical procedure ####
Run the script in java with the following parameters:

> `-Xmx<BYTES>` of Maximum java heap size

Run the script in java :

Command line:

> `java -XmxBYTES -cp ita.jar org.ciat.ita.server.OutliersManager `

example:

> `java -Xmx2000m -cp ita.jar org.ciat.ita.server.OutliersManager `




### 8 (Optional) Getting geospatial and environmental statistics ###

#### Description ####
This is an optional step. Its aim is to extract some statistics of the filtering steps. how many records are reliable and how many are not, specifying the error type.


**Inputs:** _temp\_good\_records_ table with the count of the outlier for all variables

**Outputs:** The XML file _statistics.xml_ is generated. It contains all the statistics of the set of records evaluated

#### Technical procedure ####
Run the script with the following parameters:

**Command line:**

> `java  -cp ita.jar org.ciat.ita.server.database.DataBaseStatistics`


### 10 Final filter for reliable data ###
#### Description ####

Discriminate the records that are  considerated as outlier in more than 16  environmental variable distributions and then those for which the specie has less than ten records. Only the reliable records are then stored into the final table called filtered\_records

**Inputs:** 	temp\_good\_records table with the count of the outlier for all variables

**Outputs:** 	filtered\_records table filled with the totally reliable records

#### Technical procedure ####
Run into the database the sql file named "QUERY\_final\_filter.sql"

example:

> `mysql -u User -p portal < QUERY_final_filter.sql`


**Finalize:** The tables temp\_good\_records and temp\_bad\_records are not needed anymore. The temporal tables temp\_good\_1A, temp\_good\_2A, temp\_good\_3A are useless, they were just for the query, so they can be dropped.
