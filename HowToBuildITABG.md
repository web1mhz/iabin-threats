[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/HowToBuildITABG?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/HowToBuildITABG?wl=es)



## Introduction ##

This is a technical document and is intended to people who has relation with the technical implementation of the project.

The purpose of this document is to illustrate how to build the project from the source code of this repository.

We invite you to read first the [Description](Description.md) of the project.

## Technical Requirements ##

  * Apache Subversion SVN Client. [Get it](http://subversion.apache.org/packages.html).
  * Apache Maven. [Get it](http://maven.apache.org/download.html)
  * Java JRE version 6 or higher. [Get it](http://www.java.com/)

## 1. Checking out the source code ##

Check out the [ita-bg project](http://code.google.com/p/iabin-threats/source/browse/#svn/trunk/ita-bg/src/main/java/org/ciat/ita/bg) of this repository.

> `svn checkout https://iabin-threats.googlecode.com/svn/trunk/ita-bg ita-bg`

Once the project has been checked out, you can update your local copy via the command:

> `svn update`

The instructions for checking out from Google Code's SVN can be found [here](http://code.google.com/p/iabin-threats/source/checkout)

## 2. Organizing code for Eclipse IDE ##

Once the source code has been checked out, if you want to organize it as an Eclipse IDE project, run the command:

> `mvn eclipse:eclipse`

## 3. Compiling project ##

If you want to compile the project to get the binaries, run the command:

> `mvn compile`

It creates the /target/classes directory where you can find the compiled classes

## 4. Packaging project ##

If you want to deploy the .jar file of the project, run the command:

> `mvn package`

Then you can find the .jar in the /target directory, called _ita-bg_ with the current version of the project _e.g. ita-bg-0.1-SNAPSHOT.jar_

The compiling step is not necessary since the packaging one includes the compiling process to create the jar

## 5. Using the project ##

After packaging the project, you can use the services it provides. And after [Configuring](Configuring.md) it you can run it with _java_ commands

You can see the [API](API.md) page to know how to use the classes of the project or run directly any of the executable classes.


### 5.1 Running as stand alone ###

If you have configured the project as stand alone you just need to execute the jar directly:

> `java -Xmx1500m -jar ita-bg-0.1-SNAPSHOT.jar`