[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/Configuration?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/Configuration?wl=es)



## Introduction ##

This is a technical document and is intended to people who has relation with the technical implementation of the project.

## Technical Requirements ##

  * To this point, the project must be imported, edited and packaged. (see How [How To Build ITA](http://code.google.com/p/iabin-threats/wiki/HowToBuildITA) section)
  * 250GB-500GB of HD space available for database (while importing process), 30GB for shape files and bioclimatic variables files.
  * Latest version of JRE, available for free at [http://www.java.com](http://www.java.com)
  * MySQL database server
    1. GBIF database mounted and configured based on the attached file named my.cnf.  You should change the datadir and tmpdir parameters


## Configuring MySQL Database ##
You can use any other configuration. However, we recomend the following configuration:


```
[mysqld]
#datadir=/var/lib/mysql		 
datadir=/media/Lacie_Disk/mysql			
tmpdir=/media/Lacie_Disk/mysql_temp			
socket=/var/lib/mysql/mysql.sock
user=mysql
# Default to using old password format for compatibility with mysql 3.x
# clients (those using the mysqlclient10 compatibility package). 
old_passwords=1 
#bind-address=0.0.0.0 
port=3306
max_allowed_packet = 1M
table_cache = 512 
sort_buffer_size = 128M 
read_buffer_size = 64M 
read_rnd_buffer_size = 300M 
thread_cache_size = 8 
query_cache_type = 0 
query_cache_size = 0 
thread_concurrency = 16 
myisam_max_sort_file_size = 250G 
myisam_max_extra_sort_file_size = 250G 
# this is used for repair by sorting 
myisam_sort_buffer_size = 3990M 
# maximum value for this is 4g for now! 
key_buffer_size=3990M 
tmp_table_size=512M 
max_heap_table_size=512M 
 
[mysqld_safe] 
log-error=/var/log/mysqld.log 
pid-file=/var/run/mysqld/mysqld.pid
```



## Configuring Files ##

The project needs some extra XML files that should be configured before start to run it (**client\_config.xml** and **server\_config.xml**).

As the _svn_ command doesn't import these files, ITA project can generate them assigning default values that you should change later, depending of your file system and your personal configuration.

run the following commands:

> java -cp ita-0.1-SNAPSHOT.jar org.ciat.ita.client.ClientConfig

> java -cp ita-0.1-SNAPSHOT.jar org.ciat.ita.server.ServerConfig

Both XML should have been created.


---


| server\_config.xml |
|:-------------------|

```
<?xml version="1.0" encoding="UTF-8"?>
<config>
  <rmi port="1099" />
  <database name="portal" ip_addr="localhost" port="3306">
    <user name="server">db_user</user>
    <password user="server">*******</password>
    <tables>
      <table content="unverified_records">temp_land_5A</table>
      <table content="good_records">
        temp_good_records
        <column content="variable">alt</column>
        <column content="variable">bio_1</column>
        <column content="variable">bio_2</column>
        <column content="variable">bio_3</column>
        <column content="variable">bio_4</column>
        <column content="variable">bio_5</column>
        <column content="variable">bio_6</column>
        <column content="variable">bio_7</column>
        <column content="variable">bio_8</column>
        <column content="variable">bio_9</column>
        <column content="variable">bio_10</column>
        <column content="variable">bio_11</column>
        <column content="variable">bio_12</column>
        <column content="variable">bio_13</column>
        <column content="variable">bio_14</column>
        <column content="variable">bio_15</column>
        <column content="variable">bio_16</column>
        <column content="variable">bio_17</column>
        <column content="variable">bio_18</column>
        <column content="variable">bio_19</column>
        <column content="outlier_count">outlier</column>
      </table>
      <table content="final_records">filtered_records</table>
      <table content="unrelible_records">temp_bad_records</table>
    </tables>
  </database>
</config>
```

**rmi port**: The default RMI Port. You can delete this tag in case your application is going executed as "LOCAL" configuration.

**database**:
  * **name**: Database name
  * **ip\_addr**: Database IP address
  * **port**: Database port

**user name**: Database user. (You should change the default value _db\_user_).

**password user**: Database password. (change the asterisks ).

**tables**:
  * **unverified\_records**: In case you have changed the name of the table in SQL scripts, change the default name table (temp\_land\_5A) for the correct.
  * **good\_records**: Same as above, but the default table name in this case is _temp\_good\_records_.
  * **final\_records**:  Same as above, but the default table name in this case is _filtered\_records_
  * **unrelible\_records**:  Same as above, but the default table name in this case is _temp\_bad\_records_

---


| client\_config.xml |
|:-------------------|

```
<?xml version="1.0" encoding="UTF-8" ?>
<config>
  <server ip_addr="localhost" communication_type="LOCAL" />
  <Proxy>
    <HttpProxy server="proxy4.ciat.cgiar.org" port="8080" />
  </Proxy>
  <work records="10000" />
  <shape path="/GADM_v0-6.shp">
    <column name="ISO">ISO</column>
    <column name="country">ISOCOUNTRY</column>
    <column name="state">NAME_1</column>
    <column name="county">NAME_2</column>
  </shape>
  <mask path="/inland_msk.jgm" />
  <maxent>
    <file name="maxent_file" type="jar" path="/maxent.jar" />
    <file name="sample_dir" type="directory" path="/samples/" />
    <file name="backgroung_file" type="csv" path="/background_clm.csv" />
    <file name="lambda_output_dir" type="directory" path="/lambda_output/" />
    <file name="final_output_dir" type="directory" path="/final_output/" />
    <file name="bio_variables_dir" type="directory" path="/small_variables/" />
  </maxent>
  <variables type="bioclimatic" path="/variables/">
    <variable>alt</variable>
    <variable>bio_1</variable>
    <variable>bio_2</variable>
    <variable>bio_3</variable>
    <variable>bio_4</variable>
    <variable>bio_5</variable>
    <variable>bio_6</variable>
    <variable>bio_7</variable>
    <variable>bio_8</variable>
    <variable>bio_9</variable>
    <variable>bio_10</variable>
    <variable>bio_11</variable>
    <variable>bio_12</variable>
    <variable>bio_13</variable>
    <variable>bio_14</variable>
    <variable>bio_15</variable>
    <variable>bio_16</variable>
    <variable>bio_17</variable>
    <variable>bio_18</variable>
    <variable>bio_19</variable>
  </variables>
</config>
```

**ip\_addr**:  IP address of MySQL server

**communication\_type**:
  * _LOCAL_ to run the script as stand alone (Client and Server in the same machine) - _Recommended option_.
  * _RMI_ to run the script as remote configuration (One Server - Many Clients).

**HttpProxy server and port**: In case your Internet connection pass throw a proxy. Otherwise you can delete all the `<Proxy>` tag.

**work records**:  Number of records to be evaluated per batch.

**shape**:
  * **path**: Global and detailed shape file. We recommend to download [this file](http://gisweb.ciat.cgiar.org/ITA/public_downloads/GADM_v0-6.zip)
  * **column**:  Each of the columns that have the shape file.
> More information about Shape file [clic here](http://en.wikipedia.org/wiki/Shapefile).

**mask**: Mask file location containing information about every coordinate in the world, whether land or water. The file can be get it [here](http://gisweb.ciat.cgiar.org/ITA/public_downloads/inland_msk.zip)

**maxent**: Maxent configuration can be omitted. If you prefer, you are allowed to delete it.

**variables path**: Bioclimatic variables location. You can find the 20 variables [here](http://gisweb.ciat.cgiar.org/ITA/public_downloads/BioAIO.zip).


---


Once the file configuration is correclty done, you can start to run each of the scripts presented in

this section.