###########################################
# Author: Johannes Signer
# Contact: j.m.signer@gmail.com
# Date: 10.9.2010
#p Purpose: Creat csv files for each species
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


write.species.csv <- function(x, req.points=10, log.file="log.txt", dir.out=dir.out)
{
   # all records    
   records <- read.csv(x[1])
   sp.class <- x[2]
   # vector that returns all processed species ids
   sp.processed <- c()
   # create 'primary key' based on locations
   records$loc.key <- paste(records$lat,records$lon, sep="")

   # for the progress bar
   total <- length(unique(records$specie_id))
   current <- 1
   pb <- txtProgressBar(min=0, max=total, style=1)
    
   for (i in unique(records$specie_id))
   {
      this.sp <- records[records$specie_id==i,]
      this.sp.u <- this.sp[!duplicated(this.sp$loc.key),]

      if(nrow(this.sp.u)>=req.points)
      {
          # create file
         dir.create(paste(dir.out,i,"training",sep="/"),recursive=T)

         write(paste("species id : ",i,
              "\nspecie : ",this.sp.u[1,'specie'],
              "\ngenus : ",this.sp.u[1,'genus'],
              "\ngenus_id : ",this.sp.u[1,'genus_id'],
              "\nfamily_id : ",this.sp.u[1,'family_id'], 
              "\nfamily : ", this.sp.u[1,'family'],
              "\nclass : ",sp.class,
              "\nfile.created : ",date(), 
              "\nnumber.of.points : ",nrow(this.sp.u), sep=""), paste(dir.out,i,"info.txt", sep="/"))
  
         write.table("species,lon,lat", paste(dir.out,i,"training/species.csv", sep="/"), row.names=F, col.names=F, quote=F, sep=",", append=F)
         write.table(this.sp.u[,c("specie_id","lon","lat")], paste(dir.out,i,"training/species.csv", sep="/"), row.names=F, col.names=F, quote=F, sep=",", append=T)

      # write to the log file
      write(paste(date(),i,nrow(this.sp.u),"yes", sep=","), log.file, append=T)
      # species to the list of processed species
      sp.processed <- c(sp.processed,i)
   } else write(paste(date(),i,nrow(this.sp.u),"no", sep=","), log.file, append=T)
   setTxtProgressBar(pb, current)
   current  <- current + 1
   # print(paste("processed ",current, " of ",total, sep=""))
}  
print(paste("######## just finished ", sp.class, "######## ", sep=""))
return(sp.processed)
}
   
