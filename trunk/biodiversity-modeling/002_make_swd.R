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
# dir.maxent: directory where maxent is found 


# return
#

###########################################
# Script: no changes should be made below #
###########################################

make.swd <- function(sp_id,points, ecoregions, v.all="NA", no.background=10000, dir.maxent, env.var)
{
  if (v.all=="NA") v.all <- getValues(ecoregions) # save time, when doing it only once, so better to pass as argument

  v.ok <- unique(xyValues(ecoregions, points[,2:3]))
  #v.all <- getValues(ecoregions)
  v.ok <- v.ok[!is.na(v.ok)]
  v.new <- ifelse(match(v.all, v.ok),1,NA)
  rr <- setValues(ecoregions, v.new)
  rr.df <- as.data.frame(as(rr,"SpatialPointsDataFrame"))
  background <- rr.df[sample(1:nrow(rr.df), no.background, replace=T),1:2]
  write.table("species,longitude,latitude",paste(sp_id,"/training/background.csv", sep=""),col.names=F, row.names=F,append=F, quote=F)
  write.table(c("background", background), paste(sp_id,"/training/background.csv",sep=""), col.names=F, row.names=F, sep=",", append=T, quote=F)

  # make swd.species
  system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ./",sp_id,"/training/species.csv ",env.var, ">",sp_id,"/training/species_swd.csv", sep=""))
  # make swd background
  system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ./",sp_id,"/training/background.csv ",env.var, ">",sp_id,"/training/background_swd.csv", sep=""))
}
