[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/Configurationbg?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/Configurationbg?wl=es)



## Introduction ##

This is a technical document and is intended to people who has relation with the technical implementation of the project.

The purpose of this document is to illustrate how to configure the project in this repository.

## Technical Requirements ##

  * To this point, the project must be imported, edited and packaged. (see How [How To Build ITA-BG](http://code.google.com/p/iabin-threats/wiki/HowToBuildITABG) section)
  * Latest version of JRE, available for free at [http://www.java.com](http://www.java.com)
  * MySQL database server



## Configuring Files ##

The project needs an extra XML file that should be configured before start to run it (**server\_config.xml**).

As the _svn_ command doesn't import these files, ITA project can generate them assigning default values that you should change later, depending of your file system and your personal configuration.

run the following commands:

> java -jar ita-bg-0.1-SNAPSHOT.jar org.ciat.ita.bg.server.ServerConfig

XML file should have been created.


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
  * **unverified\_records**: In case you have changed the name of the table in SQL scripts, change the default name table (occurrence\_record) for the correct.
  * **good\_records**: Same as above, but the default table name in this case is _temp\_good\_records_.
  * **final\_records**:  Same as above, but the default table name in this case is _filtered\_records_
  * **unrelible\_records**:  Same as above, but the default table name in this case is _temp\_bad\_records_


Once the file configuration is correctly done, you can start to run each of the scripts presented in

this section.