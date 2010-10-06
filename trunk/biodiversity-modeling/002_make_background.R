###########################################
# Author: Johannes Signer
# Contact: j.m.signer@gmail.com
# Date: 10.9.2010
# Purpose: make SWD files for MaxEnt
# Comments: 
###########################################
############ Preliminaries ################

# required parameters
# points: csv file with the occurence points
# ecoregions: raster with eco-regions for SA, z-value=Biome
# v.all: all values from ecoregions (getValues(ecoregions)), when supplied, time is saved
# n.background: number of background points

# return
#

###########################################
# Script: no changes should be made below #
###########################################


get.background <- function(sp_id, ecoregions, v.all, no.background=10000, make.swd=FALSE)
{
  points <- read.csv(paste(sp_id,"/training/species.csv", sep=""))

  if (nrow(points) > 40) {env.var <- env.full  
  } else env.var <- env.reduced  
  
  v.ok <- unique(xyValues(ecoregions, points[,2:3]))
  #v.all <- getValues(ecoregions)
  v.ok <- v.ok[!is.na(v.ok)]
  v.new <- ifelse(match(v.all, v.ok),1,NA)
  rr <- setValues(ecoregions, v.new)
  rr.df <- as.data.frame(as(rr,"SpatialPointsDataFrame"))
  background <- rr.df[sample(1:nrow(rr.df), no.background, replace=T),1:2]
  write.table("species,longitude,latitude",paste(sp_id,"/training/background.csv", sep=""),col.names=F, row.names=F,append=F, quote=F)
  write.table(c("background", background), paste(sp_id,"/training/background.csv",sep=""), col.names=F, row.names=F, sep=",", append=T, quote=F)

  # write env variables
   write(paste("env.var : ",env.var, sep=""), paste(sp_id,"info.txt", sep="/"), append=T)
  if (make.swd==TRUE)
  {  
    # make swd.species
    system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ",sp_id,"/training/species.csv ",env.var, ">",sp_id,"/training/species_swd.csv", sep=""),wait=T)
    # make swd background
    system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ",sp_id,"/training/background.csv ",env.var, ">",sp_id,"/training/background_swd.csv", sep=""),wait=T)
  }
}
