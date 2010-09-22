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
library(snowfall)

### Set pathes
dir.wd <- "/home/johannes/Dropbox/1_documents/12_work/ciag/trial_p"   # main working directory
dir.sp <- paste(dir.wd, sep="")                                     # directory where species located, should be in ./wd/species and than for each species a folder labeled with species id
dir.env <- paste(dir.wd, "/env", sep="")                            # directory containing the env-rasters in 30 sec.                    
dir.proj <- paste(dir.env, "/proj_5min", sep="")                    # sub directory of dir.env, containing the 5 min rasters of whole sa for projecting
dir.maxent <- paste(dir.wd,"/maxent", sep="")                       # directory containing maxent Software
dir.scripts <- paste("/home/johannes/ciag/svn.checkouts/iabin-threats/biodiversity-modeling", sep="")

ecoregions.path <- paste(dir.env, "/eco_5min.asc", sep="")               # path to the ecoregion raster

### Setup working directory
setwd(dir.wd)

#### Set varirables
# model specific varibales
pts.min <- 10 				                                              # minmum number of training points
pts.full <- 40				                                              # number of points required for a full model (i.e. 8 bioclim variables are included)
model.typ <- "NA"				                                              # model type, 2 (full model), 1 (model with reduced env var), 0 (unsufficient no of points)

# Env. variables
env.reduced <- paste(dir.env,"/bio1.asc ", dir.env,"/bio12.asc ", dir.env,"/bio4.asc ", dir.env,"/bio15.asc ", sep="")
env.full <- paste(env.reduced, paste(dir.env,"/bio5.asc ", dir.env,"/bio6.asc ", dir.env,"/bio16.asc ", dir.env,"/bio17.asc ", sep=""), sep="")

#env.reduced <- paste("bio1","bio12", "bio4","bio15")
#env.full <- paste(env.reduced, "bio5", "bio6", "bio16", "bio17")

# maxent parameters
max.ram <- 1024
replicate.type <- "crossvalidate"
no.replicates <- 10
no.background <- 10000 # number of background points

### sourcing functions
source(paste(dir.scripts, "/001_write_species_csv.R", sep=""))
source(paste(dir.scripts, "/002a_make_background.R", sep=""))
source(paste(dir.scripts, "/003_run_maxent.R", sep=""))
source(paste(dir.scripts, "/000.zipWrite.R", sep=""))
source(paste(dir.scripts, "/000.zipRead.R", sep=""))

### config snowfall
# init snowfall
sfInit(parallel=TRUE, cpus=2, type="SOCK")
# send data to workers
sfExport("write.species.csv")
sfExport("pts.min")
sfExport("no.background")
sfExport("get.background")
sfExport("env.reduced")
sfExport("env.full")
sfExport("dir.maxent")
sfExport("run.maxent")
sfExport("max.ram")
sfExport("no.replicates")
sfExport("replicate.type")
sfExport("dir.proj")
sfExport("zipWrite")
sfLibrary(raster)


###########################################
# Read fils and create for each species with 10 or more occurences a csv file (Task 9)
###########################################

sp <- list()
tmp.count <- 1
for (file in list.files(pattern="sa.*.csv")) # adjust the name if need be
{
 tmp.l <- list()
 tmp.l$class <- strsplit(strsplit(file, "_")[[1]][2],"\\.")[[1]][1] # depending on the name this may need to be adjusted
 tmp.l$records <- read.csv(file)
 sp[[tmp.count]] <- tmp.l
 tmp.count <- tmp.count + 1
}

# create log file 
write("Log file created automatically to track progress of script task9.R \n\ndate,species_id,number_of_unique_points,create_files_for_maxent", "log.txt", append=F)

st <- proc.time()[3]
sfSapply(sp, function(x) write.species.csv(records=x$records, class=x$class, req.points=pts.min, log="log.txt")) # the function write.species.csv is located in 001_write_species_csv.R
et <- proc.time()[3] - st
print(paste("it took",et,"sec"))	

###########################################
# Create SWD files and run maxent(Task 10,11,12 and 13) 
###########################################

files <- list.files(pattern="^[0-9]")
eco <- raster(ecoregions.path)
eco.values <- getValues(eco)

# additional variables to workers
sfExport("eco")
sfExport("eco.values")

st <- proc.time()[3]
sfSapply(files, function(i) get.background(sp_id=i, ecoregions=eco, v.all=eco.values, no.background=no.background)) # make.swd is located in the file 002_make_swd.R
et <- proc.time()[3] - st
print(paste("it took",et,"sec"))	

## run maxent
st <- proc.time()[3]
sfSapply(files, function(i) run.maxent(sp_id=i,max.ram=max.ram, dir.maxent=dir.maxent, no.repclicates=no.repclicates, replicate.type=replicate.type)) # run.maxent is located in the file 003_run_maxent.R 

et <- proc.time()[3] - st
print(paste("it took",et,"sec"))	


###########################################
# Extract values from the model (task 15)
###########################################

files <- list.files(pattern="^[0-9].*")
eval.stats <- data.frame(id=rep(NA, length(files)), avg.auc_train=NA, avg.auc_test=NA, sd.auc=NA, prevalence=NA, ten.percentile=NA, roc=NA)

for (i in 1:length(files)){

  for (j in 0:(no.replicates-1)){
    roc <- c()
    omission.rate <- read.csv(paste(files[i], "/cross/", files[i], "_",j,"_omission.csv", sep=""))
    spec <- omission.rate$Fractional.area
    sens <- 1-omission.rate$Training.omission
    log.vals <- omission.rate$Corresponding.logistic.value
        
    abs.dif <- abs(1-(sens+spec))

    if (length(log.vals[which(abs.dif== min(abs.dif))]) > 1) {
      roc <- c(roc, mean(log.vals[which(abs.dif==min(abs.dif))]))
    } else roc <- c(roc, log.vals[which(abs.dif==min(abs.dif))])
  }

  me.res <- read.csv(paste(files[i], "/cross/maxentResults.csv", sep=""))
  eval.stats[i,'id'] <- files[i]
  eval.stats[i,'avg.auc_train'] <- me.res[(no.replicates+1),'Training.AUC']
  eval.stats[i,'avg.auc_test'] <- me.res[(no.replicates+1),'Test.AUC']
  eval.stats[i,'sd.auc'] <- me.res[(no.replicates+1),'AUC.Standard.Deviation']
  eval.stats[i,'prevalence'] <- me.res[(no.replicates+1),'Prevalence..average.of.logistic.output.over.background.sites.']
  eval.stats[i,'ten.percentile'] <- me.res[(no.replicates+1),'X10.percentile.training.presence.logistic.threshold']
  eval.stats[i,'roc'] <- mean(roc)
  print(paste("finished ", i, "of", length(files), "species."))
}

# check for errors
missing.entries <- which(is.na(unlist(eval.stats)))
missing.values <- gsub("[0-9]", "", names(missing.entries))
missing.sp <- eval.stats[gsub("[^0-9]","",names(missing.entries)),1]
write.table(c("sp_id,missing"), "error_report.txt", row.names=F, col.names=F, quote=F, append=F)
write.table(cbind(missing.sp, missing.values), "error_report.txt",sep=",", row.names=F, col.names=F, quote=F, append=F)

# write output
write.table(eval.stats, "evaluation_statistics.csv", col.names=T, row.names=F, sep=",", append=F, quote=F)

# write it to the species file
for (i in eval.stats[,1])
{
write(paste("avg.auc_train : ",eval.stats[eval.stats[,1]==i,'avg.auc_train'], 
              "\navg.auc_test : ",eval.stats[eval.stats[,1]==i,'avg.auc_test'],
              "\nsd.auc : ",eval.stats[eval.stats[,1]==i,'sd.auc'],
              "\nprevalence : ",eval.stats[eval.stats[,1]==i,'prevalence'],
              "\nten.percentile : ",eval.stats[eval.stats[,1]==i,'ten.percentile'], 
              "\nroc : ",  eval.stats[eval.stats[,1]==i,'roc'], sep=""), paste(i,"info.txt", sep="/"), append=T)
}




