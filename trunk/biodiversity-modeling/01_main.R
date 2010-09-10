###########################################
# Author: Johannes Signer
# Contact: j.m.signer@gmail.com
# Date: 10.9.2010
# Purpose: Organise the whole workflow
# Comments: 
###########################################
############ Preliminaries ################

### Required packages
library(raster)

### Set pathes
dir.wd <- "/home/johannes/Dropbox/1_documents/12_work/ciag/trial"   # main working directory
dir.sp <- paste(dir.wd, sep="")                                     # directory where species located, should be in ./wd/species and than for each species a folder labeled with species id
dir.env <- paste(dir.wd, "/env", sep="")                            # directory containing the env-rasters in 30 sec.                    
dir.proj <- paste(dir.env, "/proj_5min", sep="")                    # sub directory of dir.env, containing the 5 min rasters of whole sa for projecting
dir.maxent <- paste(dir.wd,"/maxent", sep="")                       # directory containing maxent Software
dir.scripts <- paste(dir.wd,"../scripts", sep="")

ecoregions <- paste(dir.env, "/eco_5min.asc", sep="")               # path to the ecoregion raster

### Setup working directory
setwd(wd)

#### Set varirables
# model specific varibales
pts.min <- 10 				                                              # minmum number of training points
pts.full <- 40				                                              # number of points required for a full model (i.e. 8 bioclim variables are included)
model.typ <- NA				                                              # model type, 2 (full model), 1 (model with reduced env var), 0 (unsufficient no of points)

# Env. variables
env.reduced <- paste(dir.env,"/bio1.asc ", dir.env,"/bio12.asc ", dir.env,"/bio4.asc ", dir.env,"/bio15.asc ", sep="")
env.full <- paste(env.reduced, paste(dir.env,"/bio5.asc ", dir.env,"/bio6.asc ", dir.env,"/bio16.asc ", dir.env,"/bio17.asc ", sep=""), sep="")

# maxent parameters
max.ram <- 1024
replicate.type <- "crossvalidate"
no.replicates <- 10
no.background <- 10000 # number of background points

### sourcing functions

source(paste(dir.scripts, "/001_write_species_csv.R", sep=""))
source(paste(dir.scripts, "/002_make_swd.R", sep=""))
source(paste(dir.scripts, "/003_run_maxent.R", sep=""))

###########################################
# Read fils and create for each species with 10 or more occurences a csv file (Task 9)
###########################################

sp <- list()
tmp.count <- 1
for (file in list.files(pattern="sa.*.csv")) # adjust the name if need be
{
 tmp.l <- list()
 tmp.l$class <- strsplit(strsplit(x$file, "_")[[1]][2],"\\.")[[1]][1] # depending on the name this may need to be adjusted
 tmp.l$records <- read.csv(file)
 sp[[tmp.count]] <- tmp.l
 tmp.count <- tmp.count + 1
}

# create log file 
write("Log file created automatically to track progress of script task9.R \n\ndate,species_id,number_of_unique_points,create_files_for_maxent", "log.txt", append=F)


sapply(sp, function(x) write.species.csv(records=x$records, class=x$class, req.points=pts.min, log="log.txt"))

###########################################
# Create SWD files (Task 10,11,12 and 13) 
###########################################

files <- list.files(pattern="^[0-9]")
eco <- raster(ecoregions)
eco.values <- getValues(eco)

for (i in files)
{
 this.sp <- read.csv(paste(i,"/training/species.csv", sep=""))
 if (nrow(this.sp) > 40) {env.var <- env.full  
  } else env.var <- env.reduced
 make.swd(sp_id=i, points=this.sp, ecoregions=eco, v.all=eco.values, no.background=no.background, dir.maxent=dir.maxent, env.var=env.var)
 run.maxent(sp_id=i,max.ram=max.ram, dir.maxent=dir.maxent, no.repclicates=no.repclicates, replicate.type=replicate.type)
}



