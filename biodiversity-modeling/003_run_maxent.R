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

run.maxent <- function(sp_id,max.ram, dir.maxent, no.replicates, replicate.type, dir.proj,log.file="log.run.maxent")
{ 
t.start <- proc.time()[3]
dir.create(paste(dir.out,"/",sp_id,"/results", sep=""))
dir.create(paste(dir.out,"/",sp_id,"/cross", sep=""))
dir.create(paste(dir.out,"/",sp_id,"/proj", sep=""))

# train the model
# flags:
# -a autoron
# -z dont show gui
# -P dont do impoartnace of env variables
system(paste("java -mx",max.ram,"m -jar ", dir.maxent, "/maxent.jar nowarnings outputdirectory=", dir.out,"/",sp_id, "/results samplesfile=",dir.out,"/",sp_id,"/training/species_swd.csv environmentallayers=",dir.out,"/",sp_id,"/training/background_swd.csv -a -z nopictures -P plots=false", sep=""),wait=T)
					
# crossvalidate
system(paste("java -mx",max.ram,"m -jar ", dir.maxent, "/maxent.jar nowarnings outputdirectory=",dir.out,"/", sp_id, "/cross samplesfile=",dir.out,"/",sp_id,"/training/species_swd.csv environmentallayers=",dir.out,"/",sp_id,"/training/background_swd.csv -P replicates=",no.replicates," replicatetype=",replicate.type," -a -z nopictures -P plots=false", sep=""),wait=T)

# project
system(paste("java -mx",max.ram,"m -cp ", dir.maxent, "/maxent.jar density.Project ",dir.out,"/",sp_id,"/results/",sp_id,".lambdas ",dir.proj, " ",dir.out,"/",sp_id,"/proj/",sp_id," nowarnings fadebyclamping -r -a -z", sep=""),wait=T)


## zip files
system(paste("zip -r ",dir.out,"/",sp_id,".zip ", dir.out,"/",sp_id,sep=""))
system(paste("rm -r ",dir.out,"/",sp_id, sep=""))

tt <- proc.time()[3] - t.start
write(paste(sp_id, tt, Sys.time(), sep=";"), log.file, append=T)
}

