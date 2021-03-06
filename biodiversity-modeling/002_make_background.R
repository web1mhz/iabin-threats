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
  points <- read.csv(paste(dir.out,"/",sp_id,"/training/species.csv", sep=""))

  if (nrow(points) > 40) {env.var <- env.full  
  } else env.var <- env.reduced  
  
  v.ok <- unique(xyValues(ecoregions, points[,2:3]))
  #v.all <- getValues(ecoregions)
  v.ok <- v.ok[!is.na(v.ok)]
  v.new <- ifelse(match(v.all, v.ok),1,NA)
  if(any(!is.na(v.new))){
  rr <- setValues(ecoregions, v.new)
  rr.df <- as.data.frame(as(rr,"SpatialPointsDataFrame"))
  background <- rr.df[sample(1:nrow(rr.df), no.background, replace=T),2:3]
  write.table("species,longitude,latitude",paste(dir.out,"/",sp_id,"/training/background.csv", sep=""),col.names=F, row.names=F,append=F, quote=F)
  write.table(c("background", background), paste(dir.out,"/",sp_id,"/training/background.csv",sep=""), col.names=F, row.names=F, sep=",", append=T, quote=F)

  # write env variables
   write(paste("env.var : ",env.var, sep=""), paste(dir.out,sp_id,"info.txt", sep="/"), append=T)
  if (make.swd==TRUE)
  {  
    # make swd.species
    system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ",dir.out,"/",sp_id,"/training/species.csv ",env.var, ">",dir.out,"/",sp_id,"/training/species_swd.csv", sep=""),wait=T)
    # make swd background
    system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ",dir.out,"/",sp_id,"/training/background.csv ",env.var, ">",dir.out,"/",sp_id,"/training/background_swd.csv", sep=""),wait=T)
  }
  } else write(sp_id, paste(dir.out,"/no_bg_made.csv", sep=""), append=T)
  
}

### funciton to get swd files, not that this should only be run on a unix machine since it requires awk, and some shell tools

for i in `ls -1 | grep ^[0-9]`
do
  awk -F, '!/species/ {print $2":"$3","$2","$3}' $i/training/background.csv >> background_sort.u.csv
  echo $i
done

sort -u background_sort.u.csv > background_sort_u.csv # reduced the file from 9.5G to 11mb :)


get.full.swd <- function(type="species", env.full=env.full, dir.out=dir.out)
{
   system(paste("echo key,lon,lat > ",dir.out,"/",type,"_sort_u.csv", sep=""))
   print("created the file")
   system(paste("cat ",dir.out,"/[0-9]*/training/",type,".csv | awk -F, '!/species/ {print $2\":\"$3\",\"$2\",\"$3}'| sort -u >>",dir.out,"/",type,"_sort_u.csv", sep="")) # get unique
   print("now start to extract values with maxent extract")
   system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ",dir.out,"/",type,"_sort_u.csv ",env.full, "> ",dir.out,"/",type,"_swd.csv", sep=""),wait=T) # get swd
}

# get for every species the points required for each species
get.sp.swd <- function(sp_id, type="species",swd=read.csv(paste(dir.out,"/species_swd.csv", sep="") ), pts=pts.full)
{
  # get the number of points for this sp, from the info.txt file
  this.sp.no.points <- system(paste("cat ",dir.out,"/",sp_id,"/info.txt | awk -F: '/number.of.points/{print $2}'",sep=""), intern=T)

  # determine how many variables should be used
  if (this.sp.no.points < pts) {until <- ncol(swd) - 4} else {until <- ncol(swd)}

  # read the file with the x,y coordinates
  points <- read.csv(paste(dir.out,"/",sp_id,"/training/",type,".csv",sep=""))

  # extract the values of the environmental data, by using the uniqe locaiton key x:y
  data <- cbind(points[,1:3],swd[match(paste(points[,2],":",points[,3],sep=""), swd[,1]),4:until])
  
  # remove points with NA's
  data <- data[!is.na(data[,4]),]

  # write the file again
  write.table(data,paste(dir.out,"/", sp_id,"/training/",type,"_swd.csv", sep=""), row.names=F, quote=F, sep=",")

  # print progress
  print(paste("finished with ..... ", sp_id))
}
