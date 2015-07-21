Documentation on how the Species Distribution Modelling (SDM) was done.

[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/SpeciesDistributionModelling?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/SpeciesDistributionModelling?wl=es)



# Introduction #

Here we provide the technical details for the SDM and the consecutive threat assessment. Broadly the speaking the analysis can be divided into four steps:
  * Preparation of the data
  * SDM and their analysis
  * Calculating threats for each species
  * Calculating the percentage of area protected for each species
Below each of this steps will be described in more detail and reference is given to the source code.

# Details #
## Software ##
All were accomplished using the following software tools:
  * R (in particular packages raster, spgrass6, and snowfall)
  * MaxEnt 3.3.1
  * GRASS 6.4
  * Bash Shell

## Data preparation and organisation ##
### Directory structure ###
Within the root directory _iabin_ there are the following directories located.
  * **data** This directory contains all the data that is used for the models and subsequent analysis. In _data/env_ all environmental data is stored.
  * **parameters** This directory contains the file _parameters.R_. In this file all paths, variables concenring the analysis (i.e. number of points used in the Maxent model, the type of environmental variables used in the Maxent model) are stored.
  * **results** This directory holds a folder for each run of the analysis labelled with the date when the analysis was started. If several runs were performed at the same day, a number is added to the date. Within the folder for each run, an other folder is created for each species, labelled with its unique species id.
  * **src** within the _src_ directory there are two further directories: _src/lib_ containing Maxent and _src/scripts_ containing all scripts that were used and area also available here.

### Workflow ###
  * **Organising species data:** The presence records from GBIF and IABIN SSTN database were split by species. For each species a directory was created containing the presence points for that species and _info.txt_ file. In the _info.txt_ file the initial parameters of the species (i.e. kingdom, family, genus, number of presence points) are saved. During the modelling process results for the species are written in this file.
  * **Getting background points:** MaxEnt requires a random background sample. For each species it was determined in which which biomes (according to the wwf classification) the presence points fell. From these biomes a random sample of 10000 points was drawn for the background points.
  * **Extracting values of the climate variables:** The coordinates of all species and background points were merged. Using maxent the values for all 8 environmental variables were extracted. For each species for every presence and background point values of appropriate environmental variables were assigned.

## Processing ##
  * 01\_main.R: This script contains the whole work flow. Some functions are outsourced in other scripts, in order to reduce the code. The first part, until the evaluation of the Maxent models, is almost exclusively written R. The majority of the subsequent analysis is done with GRASS and scripts are for a bash shell. The most important file is the file _parameters.R_. This file is sourced at the beginning of the process and contains all parameters and functions. The following steps were taken:
  * Create species csv: the function `write.species.csv()` is applied to all species. It extracts the points for all species with 10 or more occurrence records within GBIF and IABIN SSTN, creates a folder for this species and stores the points in the folder training. The script is stored in _001\_write\_species\_csv.R_. E.g. for a species with the id 100001 the following csv is created: _resutls/[date](date.md)/100001/training/species.csv_ and the file _species.csv_ has three columns, which are: _sp,lon,lat_.
  * Create background points: For each species background 10000 random background files are created within the same biomes were the presence points are found. The background file for a species with the id 100001 is written to _results/[date](date.md)/100001/training/background.csv_. Functions to create background files are located in _002\_make\_background.R_.
  * Create SWD: In this part of the script SWD (species with data) created. The function `get.full.swd()` takes from all species all unique presence points and background points and extracts the value of the environmental variables. For each species a species-SWD and background-SWD file is created with the function `get.sp.swd()`. The function is located in _002\_make\_background.R_.
  * Run Maxent: The total number of species is equally distributed over all available servers and cores. For each core a R batch file is created. The function `run.maxent()` creates a Maxent mode, 10 fold cross validation and a projection for South America for each specie. The function `run.maxent()` is located in: 003\_run\_maxent.R
  * Evaluate Maxent and import to GRASS: For this step, R needs to be started from a grass shell. For each species the projected potential distribution is imported to grass and the evaluation statistic is extracted from the Maxent output files and written to the _info.txt_ for each specie.
  * Convex Hull & import to GRASS: Predictions of the potential distribution of each species is cut to a buffered convex hull of all presence points. The buffer distance is 3 degrees.
  * Species Richness per genus: for each genus the total richness of species was calculated.
  * Species Richness per species type: for each species type the species richness was calculated.
  * Threat per specie: For each specie and each threat and level there of, the following was calculated: the percentage of area under threat, the mean occurrence probability under threat and the mean occurrence probability not under threat. Results were written to the _info.txt_ file of each species. All code for calculating the threats is located in _01\_main.R_.
  * Mean occurrence probability inside and outside of protected areas: For each specie the mean occurrence probability inside and outside of protected areas is calculated. Additionally for each species the percentage of area protected is calculated.
  * Summary protected areas: For each protected area the mean species richness and the mean of each threat is calculated.
  * Summary species type: For each species type the mean species richness is calculated and the percent that is protected is calculated.