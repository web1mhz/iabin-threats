[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedureMaxent?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedureMaxent?wl=es)



## Introduction ##

This is a technical document and is intended to people who are responsible for the technical implementation of the project.

The purpose of this document is to illustrate how to configure and run the Maxent script.



## Configuration ##


---


| client\_config.xml |
|:-------------------|

```
<?xml version="1.0" encoding="UTF-8" ?>
<config>
  ....
  ....
  <maxent>
    <file name="maxent_file" type="jar" path="/maxent.jar" />
    <file name="sample_dir" type="directory" path="/samples/" />
    <file name="backgroung_file" type="csv" path="/background_clm.csv" />
    <file name="lambda_output_dir" type="directory" path="/lambda_output/" />
    <file name="final_output_dir" type="directory" path="/final_output/" />
    <file name="bio_variables_dir" type="directory" path="/small_variables/" />
  </maxent>
  ....
  ....
</config>
```

**maxent**:
  * **maxent\_file**: Maxent JAR file location.
  * **sample\_dir**: The sample directory contains the comma separated files resulting from running the [first process](http://code.google.com/p/iabin-threats/wiki/RunningProcedureMaxent?wl=en#1._Exporting_records_into_a_comma_separated_file).
  * **background\_file**: Contains 10000 random points to train a maxent model and can be downloaded from [here](http://gisweb.ciat.cgiar.org/ita/public_downloads/background_clm.csv)
  * **lambda\_output\_dir**: Results from the first run of maxent will be stored in this directory.
  * **final\_output\_dir**: This directory will contains all the Ascii-Grids with projections from the second run of maxent.
  * **bio\_variables\_dir**: Small Bioclimatic variables directory location. Download [here](http://gisweb.ciat.cgiar.org/ita/public_downloads/small.zip). (Do not use the All In One file).



---


## Running Procedure ##

### 1. Exporting records into a comma separated file ###
#### Description ####
Exporting the reliable records into a comma separated file to be run in Maxent

**Inputs:** _filtered\_records_ table filled with the reliable records

**Outputs:** A comma separated file _filtered\_records.csv_ with the reliable records

#### Technical procedure ####
Run the script using java with the following parameters:


**Parameters:**

> `csv` To run the corresponding method to export a csv per specie.

> `-Xmx<BYTES>` of Maximum java heap size

> `-n <NUMBER>` of records that should be analized to show the time statistics

> `-d <OUTPUT>` directory exactly location


**Command line:**

> `java -XmxBYTES -cp ita.jar client.correctormanager.MaxentManager csv -n NUMBER -d OUTPUT`

example:

> `java -Xmx1000m -cp ita.jar client.correctormanager.MaxentManager csv -n 1000 -d /home/output `



### 2. Generate species projections with Maxent ###
#### Description ####
This script generates the lambda files and immediately after generates the projections of the species data using maxent application and the environmental varibles.

**Inputs:** Environmental variables preferably with a low resolution. Maxent runnable jar.

**Outputs:** Projection for species ascii files

#### Technical procedure ####
Run the script in java with the following parameters:

**Parameters:**

> `-Xmx<BYTES>` of Maximum java heap size

> `maxent` to Run the corresponding script to generate species projections

> `-i <INSTANCES>` of Maxent  that will be executed at same time


**Command line:**

> `java -XmxBYTES  -cp ita.jar client.correctormanager.MaxentManager maxent -i INSTANCES `

example:

> `java -Xmx1000m -cp ita.jar client.correctormanager.MaxentManager maxent -i 4`
