###########################################
# Author: Johannes Signer
# Contact: j.m.signer@gmail.com
# Date: 10.9.2010
# Purpose: Creat csv files for each species
# Comments: 
###########################################
############ Preliminaries ################

# required parameters

# records: a data frame with the records, must have columns labeled
# - specie (name of the species)
# - specie_id (unique id for each species)
# - family (name of the family)
# - family_id (unique id for each family)
# class: taxonomic class to which the species belongs
# min.Points: minimum number of unique occurence points
# log.file: name and path to a log file

# return
# writes for each species (where # of unique occurrences >= min.Points) a directory with a csv and an info file. 

###########################################
# Script: no changes should be made below #
###########################################


write.species.csv <- function(records, class, req.points=10, log.file="log.txt")
{

# create 'primary key' based on locations
records$loc.key <- paste(records$lat,records$lon, sep="")

for (i in unique(records$specie_id))
{
  this.sp <- records[records$specie_id==i,]
  this.sp.u <- this.sp[!duplicated(this.sp$loc.key),]
    

  if(nrow(this.sp.u)>=req.points)
  {
    # create file
   if(!file.exists(paste(i))) dir.create(paste(i))
   if(!file.exists(paste(i,"/training",sep=""))) dir.create(paste(i,"/training",sep=""))
   write(paste("species id : ",i,
              "\nspecie : ",this.sp.u[1,'specie'],
              "\ngenus : ",this.sp.u[1,'genus'],
              "\ngenus_id : ",this.sp.u[1,'genus_id'],
              "\nfamily_id : ",this.sp.u[1,'family_id'], 
              "\nfamily : ", this.sp.u[1,'family'],
              "\nclass : ",class,
              "\nfile.created : ",date(), 
              "\nnumber.of.points : ",nrow(this.sp.u), sep=""), paste(i,"info.txt", sep="/"))
  
    write.table("species,lon,lat", paste(i,"/training/species.csv", sep=""), row.names=F, col.names=F, quote=F, sep=",", append=F)
    write.table(this.sp.u[,c("specie_id","lon","lat")], paste(i,"/training/species.csv", sep=""), row.names=F, col.names=F, quote=F, sep=",", append=T)

    # write to the log file
    write(paste(date(),i,nrow(this.sp.u),"yes", sep=","), log.file, append=T)
  } else write(paste(date(),i,nrow(this.sp.u),"no", sep=","), log.file, append=T)

}
print(paste("completed ", class, sep=""))
}
   
