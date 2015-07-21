[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedureBG?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/RunningProcedureBG?wl=es)



# Introduction #

This tutorial will describe the different procedure steps in order to have a fully functional installation of the Biogeomancer tool.

### 1 The configuration files ###
#### Description ####
First of all, the values of the  XML configuration file parameters _server\_config.xml_ should be set.

If the configuration file have not been created yet, you can generate it with default values using the script described in the Technical Procedure section. You can then edit it to set the corresponding values.

Most of these parameters are files and directories  paths. Examples of default settings are in the project source code.

**Outputs:** Configuration file: _server\_config.xml_.

#### Technical procedure ####
To generate the default configuration file, run the script with java using the following parameters for the server part:

> `java -cp ita-bg.jar org.ciat.ita.bg.server.ServerConfig`

Then edit the file server\_config.xml


### 2 georeferencing ###
#### Description ####
In order to use the geo-referencing tool, you must provide the number of records that you want to process.

**Inputs:** MySQL table with the occurrence records of the database (which are selected from the table specified in server\_config.xml), and the

**Outputs:** the geo-referenced records are written aside to the original records, in the same table, in fields Blatitude, Blongitude, Uncertainty and is\_fixed.

#### Technical procedure ####
Run into the database the sql query file named “QUERY\_creates\_table\_georeferenced\_records.sql”

Command line example:

> `mysql –u User –p portal < QUERY_creates_table_georeferenced_records.sql `

#### Finalize ####
Once the query has been run, the tables georeferenced\_records and temp\_georeferenced\_records are created.

#### Technical procedure ####
Run the script in java with the following parameters:

**Parameters:**

> `-Xmx<BYTES>` bytes of Maximum java heap size

**Command line:**

> `java -XmxBYTES -cp ita-bg.jar org.ciat.ita.bg.Biogeomancer`


### Standalone ###

In standalone mode you just need to run a single instance.

**Parameters:**
> `-Xmx<BYTES>` of Maximum java heap size

**Command line:**

> `java -XmxBYTES -jar ita-bg.jar`

example

> `java -Xmx1500m -jar ita-bg.jar`

> #### Presentation of Results ####
The results from biogeomancer will be acquired and written to the database,
these data will have also a column named is\_fixed (status) that will contain a number
whose meaning is explained in this table:

status number     explanation
> 0				registers that have not been geoReferenced
  1. registers corrected succesfully
> 2				registers that BG couldnt correct
> 3				registers in which BG returned error
