###########################################
# Author: Johannes Signer
# Contact: j.m.signer@gmail.com
# Date: 10.9.2010
# Purpose: run and cross validate maxent
# Comments: 
###########################################
############ Preliminaries ################

# required parameters
# sp_id: species id
# max.ram: max ram available for maxent
# dir.maxent: path to maxent
# no.replicates: number of replicates
# replicate.type: mode of replication

# return
# . to do

###########################################
# Script: no changes should be made below #
###########################################

run.maxent <- function(sp_id,max.ram, dir.maxent, no.repclicates, replicate.type)
{ 
dir.create(paste(sp_id,"/results", sep=""))
dir.create(paste(sp_id,"/cross", sep=""))
dir.create(paste(sp_id,"/proj", sep=""))

# train the model
system(paste("java -mx",max.ram,"m -jar ", dir.maxent, "/maxent.jar nowarnings outputdirectory=", sp_id, "/results samplesfile=",sp_id,"/training/species_swd.csv environmentallayers=",sp_id,"/training/background_swd.csv -a -z", sep=""),wait=T)
					
# crossvalidate
system(paste("java -mx",max.ram,"m -jar ", dir.maxent, "/maxent.jar nowarnings outputdirectory=", sp_id, "/cross samplesfile=",sp_id,"/training/species_swd.csv environmentallayers=",sp_id,"/training/background_swd.csv -P replicates=",no.replicates," replicatetype=",replicate.type," -a -z", sep=""),wait=T)

# project
system(paste("java -mx",max.ram,"m -cp ", dir.maxent, "/maxent.jar density.Project ",sp_id,"/results/",sp_id,".lambdas ",dir.proj, " ",sp_id,"/proj/",sp_id," nowarnings fadebyclamping -r -a -z", sep=""),wait=T)


## zipWrite fails with snowfall, hence use the unix commands
system(paste("tar -zcvf ",sp_id,".tar.gz ", sp_id,sep=""))
system(paste("rm -rv",sp_id))

## write *.gz, function provided by J. Ramirez
#for (raster in list.files(paste(sp_id,"proj/", sep="/"))) zipWrite(raster(paste(sp_id,"proj",raster, sep="/")), paste(sp_id,"proj", sep="/"), paste(strsplit(raster, "\\.")[[1]][1],"gz", sep="."))

# rm original files
#file.remove(paste(sp_id,"/proj/",list.files(paste(sp_id,"/proj/", sep=""), pattern="*.asc"), sep=""))

}

