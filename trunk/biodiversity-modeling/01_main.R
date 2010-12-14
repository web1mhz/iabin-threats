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

# clean workspace
rm(list=ls())

### Load parameters and setup working directory assuming R session was onpend from iabin root directorg
source("./parameters/parameters.R")
# save parameters that were used in this run to the results directory
save(list=ls(), file=paste(dir.out,"/parameters.RData",sep=""))

###########################################
# Read fils and create for each species 
# with 10 or more occurences a csv file 
# (Task 9)
###########################################

sp <- apply(species.files.raw,1,write.species.csv,log.file=log.make.species.csv, req.points=pts.min,dir.out=dir.out)
save(sp, file=species.id.to.process) 

###########################################
# Create SWD files and run maxent
# (Task 10,11,12 and 13) 
###########################################

eco <- raster(ecoregions.path)
eco.values <- getValues(eco)

# extract backgrounds from wwf biomes task 10
### Export variables to workers
sfInit(parallel=sf.parallel, cpus=sf.cpus, type=sf.type)
sfExport("get.background")
sfLibrary(raster)
sfExport("eco")
sfExport("eco.values")
sfExport("me.no.background")
sfExport("dir.out")
sfExport("env.reduced")
sfExport("env.full")

system.time(sfSapply(sp, function(i) get.background(sp_id=i, ecoregions=eco, v.all=eco.values, no.background=me.no.background, make.swd=FALSE)))
 
sfStop()

## remove files that have no background

if(any(grepl("no_bg", list.files(path=dir.out)))) {
  files <- read.table(paste(dir.out,"no_bg_made.csv", sep="/"))
  system(paste("mkdir ", dir.out,"/no_bg", sep=""))
  for (i in files) system(paste("mv -v ",dir.out,"/",i," ",dir.out,"/no_bg",i,sep=""))
  sp <- list.files(path=dir.out, pattern="^[0-9].*")
  
  # save updated sp list
  save(list=ls(), file=paste(dir.out,"/parameters.RData"))
}


## Make swd files 
# extract all unique points
system.time(get.full.swd(type="species", env.full=env.full, dir.out=dir.out))
system.time(get.full.swd(type="background", env.full=env.full, dir.out=dir.out))

# get points for each species sp
system.time(sapply_pb(sp, function(x) get.sp.swd(sp_id=x, type="species", pts=pts.full, swd=read.csv(paste(dir.out, "/species_swd.csv", sep="")))))

## run in parallel, because its to slow
sfInit(parallel=sf.parallel, cpus=sf.cpus, type=sf.type)
sfExport("get.sp.swd")
sfExport("pts.full")
sfExport("dir.out")
system.time(sfSapply(sp, function(x) get.sp.swd(sp_id=x, type="background", pts=pts.full, swd=read.csv(paste(dir.out, "/background_swd.csv", sep="")))))
sfStop()



##### Run Maxent #####
### create the batch files for the servers 
split.list <- rep(1:sum(cores),each=ceiling(length(sp)/sum(cores)), length.out=length(sp))
per.core <- split(sp, split.list)
t.count <- 1 # tmp count
for (server in 1:length(servers))
{
  # save paramters
  if(!file.exists(paste(dir.out,servers[server],sep="/"))) dir.create(paste(dir.out,servers[server],sep="/"))
  for (core in 1:cores[server])
  {
    if(length(per.core)>=t.count)
    {
      # write species list    
      write.table(per.core[[t.count]], paste(dir.out,"/",servers[server],"/species_list_core",core,".txt",sep=""), row.names=F, col.names=F, quote=F)
      # write R batch file for each core
      write(paste("# load params\n",
        "load(\"",dir.out,"/parameters.RData\")\n",
        "# load data\n",
        "files <- read.table(\"",dir.out,"/",servers[server],"/species_list_core",core,".txt\")\n",
        "# prepare the log file\n",
        "log.file <- \"",log.run.maxent.prefix,"_", servers[server], "_core", core,".txt\"\n",
        "write(\"sp_id;proc_time;finished_at\", log.file, append=F)\n",
        "# run maxent\n",
        "sapply(files[,1], function(i) run.maxent(sp_id=i,max.ram=me.max.ram, dir.maxent=dir.maxent, dir.proj=dir.proj, no.replicates=me.no.replicates, replicate.type=me.replicate.type,log.file=log.file))",sep=""),
        paste(dir.out,"/",servers[server],"/runmaxent_core",core,".R",sep=""))
       t.count <- t.count+1
    }
  }
  # for each server make a shell script to start execute the R batches
  # call the shell scripts from the iabin root directory
  write(paste("for f in ",dir.out,"/", servers[server], "/ *.R\n",
    "do\n",
    "screen -S $f -m -d R CMD BATCH $f\n",
    # -S gives a meaningful session names, in this caste genomix*/runmanxent_core*.R
    # -d -m detach console
    "done", sep=""), paste(dir.out,"/", servers[server], "/run_batches.sh", sep=""))
  
}


###########################################
# Extract metrics from the model and 
# import rasters to grass (task 15)
###########################################

# most likely this will be continued after some time and parameters will need to be reloaded
# the name of the out.dir of the run that shall be continued need to be given here!
# This R session needs to be started from a GRASS shell in the right mapset with the right region!
parameters <- "results/20101207.3/parameters.RData"
load(parameters)
# load species ids
load(species.id.to.process)
files <- list.files(path=dir.out,pattern="^[0-9].*.zip")

# This script must be run within a grass shell
# check mapset and region are right
# setting up grass
#system(paste("g.mapset mapset=",mapset," -c", sep=""))
system("g.region sa")
system("g.region res=00:05")

# remove where there were erros predicting, when size is to small
files <- data.frame(files, stringsAsFactors=F)
files <- data.frame(files, size=apply(files,1,function(x) file.info(paste(dir.out,"/",x,sep=""))$size))

# only continue files with more than 1mb (1048576 bytes), write others to the redo file
files.error <- files[files[,2]<1048576,]
files <- files[files[,2]>=1048576,]

# prep out file
eval.stats <- data.frame(id=rep(NA, length(files[,1])), avg.auc_train=NA, avg.auc_test=NA, sd.auc=NA, prevalence=NA, ten.percentile=NA, roc=NA, bio1=NA, bio12=NA, bio15=NA, bio4=NA, bio5=NA, bio6=NA, bio16=NA, bio17=NA, max.contribution=NA)

write.table(files.error, paste(dir.out, "/species_where_maxent_failed.txt",sep=""))

st <- proc.time()
for (i in 1:length(files[,1])){
  system(paste("unzip ",dir.out,"/",files[i,1],sep="")) # extract the archives
  this.id=strsplit(files[i,1],"\\.")[[1]][1] # get the id of the species that is being procesed 
  
  # calculate roc threshold, min distance
  roc <- c()  
  for (j in 0:(me.no.replicates-1)){ ## some revision is needed here!    
    omission.rate <- read.csv(paste(dir.out,"/",this.id, "/cross/", this.id, "_",j,"_omission.csv", sep=""))
    spec <- omission.rate$Fractional.area
    sens <- 1-omission.rate$Training.omission
    log.vals <- omission.rate$Corresponding.logistic.value
        
    abs.dif <- abs(1-(sens+spec))

    if (length(log.vals[which(abs.dif== min(abs.dif))]) > 1) {
      roc <- c(roc, mean(log.vals[which(abs.dif==min(abs.dif))]))
    } else roc <- c(roc, log.vals[which(abs.dif==min(abs.dif))])
  }

  me.res <- read.csv(paste(dir.out,"/",this.id, "/cross/maxentResults.csv", sep=""))
  eval.stats[i,'id'] <- this.id
  eval.stats[i,'avg.auc_train'] <- me.res[(me.no.replicates+1),'Training.AUC']
  eval.stats[i,'avg.auc_test'] <- me.res[(me.no.replicates+1),'Test.AUC']
  
  # if sd < 0, substitute for NA, that is because there fewer than 20 points and CV fails
  me.sd <- me.res[(me.no.replicates+1),'AUC.Standard.Deviation']
  if (me.sd < 0) {eval.stats[i,'sd.auc'] <- NA} else eval.stats[i,'sd.auc'] <- me.sd

  eval.stats[i,'prevalence'] <- me.res[(me.no.replicates+1),'Prevalence..average.of.logistic.output.over.background.sites.']
  eval.stats[i,'ten.percentile'] <- me.res[(me.no.replicates+1),'X10.percentile.training.presence.logistic.threshold']
  eval.stats[i,'roc'] <- mean(roc)

  # bio variables
  eval.stats[i,'bio1'] <- me.res[(me.no.replicates+1),'bio1.contribution']
  eval.stats[i,'bio12'] <- me.res[(me.no.replicates+1),'bio12.contribution']
  eval.stats[i,'bio15'] <- me.res[(me.no.replicates+1),'bio15.contribution']
  eval.stats[i,'bio4'] <- me.res[(me.no.replicates+1),'bio4.contribution']

  # other four variales, only if a species has more than 10 records
  if(any(grepl("bio5", names(me.res)))) eval.stats[i,'bio5'] <- me.res[(me.no.replicates+1),'bio5.contribution']
  if(any(grepl("bio6", names(me.res)))) eval.stats[i,'bio6'] <- me.res[(me.no.replicates+1),'bio6.contribution']
  if(any(grepl("bio16", names(me.res)))) eval.stats[i,'bio16'] <- me.res[(me.no.replicates+1),'bio16.contribution']
  if(any(grepl("bio17", names(me.res)))) eval.stats[i,'bio17'] <- me.res[(me.no.replicates+1),'bio17.contribution']

  # get the bio with the max contribution
  eval.stats[i, 'max.contribution'] <- names(eval.stats)[which(eval.stats[i,]==max(eval.stats[i,grep("bio", names(eval.stats))], na.rm=T))]

  print(paste("finished ", i, "of", length(files[,1]), "species."))
  
  # import projection to grass
  system(paste("r.in.arc in=",dir.out,"/",this.id,"/proj/",this.id,".asc out=me.",this.id," --o  --q",sep=""),wait=T)
  # mport points to grass  
  system(paste("cat ",dir.out,"/",this.id,"/training/species.csv | awk -F',' 'NR > 1 {print $2 \"|\" $3}' | v.in.ascii out=occ_",this.id," --o --q",sep=""),wait=T)
  # delete all files except the info file, that have been extracted from the archive  
  system(paste("ls ",dir.out,"/",this.id," | grep -v info.txt |awk '{print\"",this.id,"/\" $0}' | xargs rm -rf",sep=""),wait=T)
  # move archive into the folder  
  system(paste("mv ",dir.out,"/",files[i,1]," ", dir.out, "/", this.id,sep=""))
  # delete all other folders 
  system(paste("rm -r ", dir.out,"/", this.id, "/cross ",dir.out,"/", this.id, "/proj ",dir.out,"/", this.id, "/results ",dir.out,"/", this.id, "/training",sep=""))
}

et <- proc.time()

# check for errors
missing.entries <- which(is.na(unlist(eval.stats)))
missing.values <- gsub("[0-9]", "", names(missing.entries))
missing.sp <- eval.stats[gsub("[^0-9]","",names(missing.entries)),1]
write.table(c("sp_id,missing"), log.extract.auc,row.names=F, col.names=F, quote=F, append=F)
write.table(cbind(missing.sp, missing.values), log.extract.auc, row.names=F, col.names=F, quote=F, append=T)

# write output
write.table(eval.stats, paste(dir.out, "/evaluation_statistics.csv",sep=""), col.names=T, row.names=F, sep=",", append=F, quote=F)

# write it to the species file
for (i in eval.stats[,1])
{
write(paste("avg.auc_train : ",eval.stats[eval.stats[,1]==i,'avg.auc_train'], 
              "\navg.auc_test : ",eval.stats[eval.stats[,1]==i,'avg.auc_test'],
              "\nsd.auc : ",eval.stats[eval.stats[,1]==i,'sd.auc'],
              "\nprevalence : ",eval.stats[eval.stats[,1]==i,'prevalence'],
              "\nten.percentile : ",eval.stats[eval.stats[,1]==i,'ten.percentile'], 
              "\nroc : ",  eval.stats[eval.stats[,1]==i,'roc'], 
              "\nbio1 : ", eval.stats[eval.stats[,1]==i,'bio1'],
              "\nbio4 : ", eval.stats[eval.stats[,1]==i,'bio4'],
              "\nbio12 : ", eval.stats[eval.stats[,1]==i,'bio12'],
              "\nbio15 : ", eval.stats[eval.stats[,1]==i,'bio15'],
              "\nbio5 : ", eval.stats[eval.stats[,1]==i,'bio5'],
              "\nbio6 : ", eval.stats[eval.stats[,1]==i,'bio6'],
              "\nbio16 : ", eval.stats[eval.stats[,1]==i,'bio16'],
              "\nbio17 : ", eval.stats[eval.stats[,1]==i,'bio17'],
              "\nmost.important.bio : ", eval.stats[eval.stats[,1]==i,'max.contribution'],sep=""), paste(dir.out,i,"info.txt", sep="/"), append=T)
}


###########################################
# Do CHull, export chull as buffer and cut 
# predictions to buffered chull (task 14)
###########################################

# description: cuts the predictions by a buffered convex hull of the occurence points and writes a *.csv with the coordinates of the convexhull and the buffered convex hull.

#### Execute in GRASS shell assuming the location and mapset are correct
#### cd into the working directory of this run eg. cd ./results/20101012.2

# load parameters 
. ../../parameters/parameters.sh

# initizialise
count=1
total=`ls | grep ^[0-9].*.[0-9]$ | wc -l` 

for i in `ls | grep ^[0-9].*.[0-9]$`
do
  # chull
  v.hull in=occ_$i out=occ_$i\_hull --o --q
  # buff chull  
  v.buffer in=occ_$i\_hull out=occ_$i\_hull_buffer distance=$buffer_distance type=area --o --q

  # export chull
  echo "lon,lat" >  $i/chull.csv
   # exports the coords of the polygon (B) and the centroid (C), awk is used to get extract only the points of the polyon. 
  v.out.ascii in=occ_$i\_hull format=standard | awk '/B/, /C/{if(!/B/ && !/C/) print $1","$2}' >> $i/chull.csv

  # export buffered chull
  echo "lon,lat" > $i/chull_buffer.csv
  # exports the coords of the polygon (B) and the centroid (C), awk is used to get extract only the points of the polyon. 
  v.out.ascii in=occ_$i\_hull_buffer format=standard | awk '/B/, /C/{if(!/B/ && !/C/) print $1","$2}' >> $i/chull_buffer.csv

  # cut predicitoins buffer
  v.to.rast in=occ_${i}_hull_buffer out=tmp use=val value=1 --q
  r.mapcalc "MASK=tmp"
  r.mapcalc "me.c.$i=me.$i"
  g.remove rast=tmp,MASK,me.$i --q
    
  # information on progress
  echo processed $count of $total
  count=$(( $count + 1 ))
done

###########################################
# Species Richness per genus (task 16)
###########################################

# make a file where for each species the class, family, genus and species is written

echo "class,fam_id,gen_id,sp_id" > tax.txt

for i in `ls | grep ^[0-9].*.[0-9]$`
do
  class=`awk -F' : ' '/class/ {print $2}' $i/info.txt`
  fam=`awk -F' : ' '/family_id/ {print $2}' $i/info.txt`
  gen=`awk -F' : ' '/genus_id/ {print $2}' $i/info.txt`
  sp=`awk -F' : ' '/species id/ {print $2}' $i/info.txt`
  echo "$class,$fam,$gen,$sp" >> tax.txt

done

# species richness per genus
# genus is the third column in the file tax.txt, hence take unique values

for genus in `awk -F, 'NR>1 {print $3}' tax.txt | sort -u`  # for each unique genus
do
  for species in `awk -F, '/'$genus'/{if(NR>1); print $4}' tax.txt` # for each species that belongs to the genus $genus
  do
    auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt`
    if [  $(echo "$auc_species > $auc_th" | bc) -gt 0 ] # check if the AUC is above the required threshold
    then
      threshold=`awk -F" : " '/^'$occ_th'/{print $2}' $species/info.txt` # get the threshold from the species
      r.mapcalc "gen.sp.$genus.$species=if(isnull(me.c.${species}) ||| me.c.${species} < $threshold,0,1)" # create map for this species
    fi
  done
  g.mlist type=rast pattern="gen.sp.$genus.*" > tmp.sf # potentially problem, and an addtional loop will be needed for the mapcalc
  # adding the raster together
  r.mapcalc "A=0"
  r.mapcalc "B=0"
  out=A
  tmp=B

  while read line
  do
    r.mapcalc "$out=$tmp+$line"
    if [ $out == "A" ]; then
      out=B
      tmp=A
    else
      out=A
      tmp=B
    fi
  done < tmp.sf
  g.copy rast=$tmp,gen.$genus --o
  g.mremove rast="gen.sp.*" -f
done

# clean up
g.mremove rast="gen.sp.*" -f
g.remove rast=A,B
rm tmp.sf



###########################################
# Species Richness per class (task 17)
###########################################

# species richness per class

for class in `awk -F, 'NR>1 {print $1}' tax.txt | sort -u`  # for all classes
do
  for species in `awk -F, '/'$class'/{if(NR>1); print $4}' tax.txt` # get all species that belong to class a class
  do
    auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt` # get the species auc
    if [  $(echo "$auc_species > $auc_th" | bc) -gt 0 ] # treat a species only if its AUC ia above the critical threshold
    then
      sleep 1
      threshold=`awk -F" : " '/^'"$occ_th"'/{print $2}' $species/info.txt`
      r.mapcalc "cla.sp.$class.$species=if(isnull(me.c.${species}) ||| me.c.${species} < $threshold,0,1)" # only consider area above the threshold
    fi
      
  done
  g.mlist type=rast pattern="cla.sp.$class.*" > tmp.sf

  # adding the raster together
  r.mapcalc "A=0"
  r.mapcalc "B=0"
  out=A
  tmp=B
  
  # progress
  # now adding all the rasters together

  while read line
  do
    r.mapcalc "$out=$tmp+$line"
    if [ $out == "A" ]; then
      out=B
      tmp=A
    else
      out=A
      tmp=B
    fi
  done < tmp.sf
  g.copy rast=$tmp,cla.$class
done

# clean up
g.mremove rast="cla.sp.*" -f
g.remove rast=A,B
rm tmp.sf

###########################################
# Threat for each species (task 18)
###########################################

for species in `ls | grep ^[0-9].*.[0-9]$` # do for all species
do
  g.remove rast=MASK
  auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt` # get the average auc for this species

  if [ $(echo "$auc_species > $auc_th" | bc) -gt 0 ] # only proces species where the auc is bigger than the specified auc
  then
    threshold=`awk -F" : " '/roc/{print $2}' $species/info.txt` # get species threshold
    r.mapcalc "tmp.$species=if(me.c.${species} >= $threshold,1,null())" # calculate temp grid only with the area above threshold
    area=`r.sum tmp.$species | awk -F= '{print $2}'` # sum the area over all area
    echo "area.above.th.in.pixel : $area" >> $species/info.txt # write to file
    areakm=`r.report -hne map=tmp.${species} units=k nsteps=1 | awk -F'|' '/TOTAL/{print $3}' | sed 's/,//g'` # sum area in km2
    echo "area.above.th.in.km2 : $areakm" >> $species/info.txt
    
    # area that is alread 'lost', its all no data in the layer ta.aggregated
    r.mapcalc "MASK=if(tmp.$species)&&if(isnull(ta.aggregate))"
    arealost=`r.report -hne map=tmp.${species} units=k | awk -F'|' '/TOTAL/{print $3}' | sed 's/,//g '`
    echo "area.lost.in.km2 : $arealost" >> $species/info.txt
    echo "percent.lost :" `echo "scale=4; $arealost / $areakm * 100" | bc` >> $species/info.txt
    areanotlost=`echo "scale=4; $areakm-$arealost" | bc`
    echo "area.not.lost.in.km2 : $areanotlost" >> $species/info.txt
    # mean occurence probability in lost area
    eval `r.univar -g me.c.$species`
    echo "lost.mean.occ.probability : $mean" >> $species/info.txt
    echo "lost.sd.occ.probability : $stddev" >> $species/info.txt

    # mean occurence probability in not lost area
    g.remove rast=MASK
    r.mapcalc "MASK=if(ta.aggregate)&&if(tmp.$species)"
    eval `r.univar -g me.c.$species`
    echo "not.lost.mean.occ.probability : $mean" >> $species/info.txt
    echo "not.lost.sd.occ.probability : $stddev" >> $species/info.txt
   
    # keep mask for areas that are presence of this species
    # g.remove rast=MASK    
    # r.mapcalc "MASK=tmp.$species" # mask to area above threshold
      for danger in `g.mlist type=rast mapset=PERMANENT pattern="ta.*"` # loop through all threats
      do   
        if [ $(echo "$area > 0" | bc) -gt 0 ]  # only treat species where the area above the threshold is > 0, this check might be obsolete
        then 
          # mean threat
          eval `r.univar -g $danger`
          sleep 0.1
          echo "${danger}.mean : $mean" >> $species/info.txt 
          echo "${danger}.sd : $stddev" >> $species/info.txt 
          echo "${danger}.min : $min" >> $species/info.txt 
          echo "${danger}.max : $max" >> $species/info.txt 
          echo "${danger}.coeff_var : $coeff_var" >> $species/info.txt 
          
          
          # % low, mid, high, no threat
          for level in `g.mlist type=rast mapset=threats pattern="tac.$danger*"` # calculate the % area of each species under each low, mid, high and no thread
          do
            # Percent under threat
            g.remove rast=MASK
            r.mapcalc "MASK=if($level)&&if(tmp.$species)"
            area_level=`r.report -hne map=tmp.$species units=k | awk -F'|' '/TOTAL/{print $3}' | sed 's/,//g'`

            if [ "$danger" = "ta.access_pop" ]; then
              percent_level=`echo "scale=4; $area_level / $areakm *100" | bc`            
            else            
              percent_level=`echo "scale=4; $area_level / $areanotlost *100" | bc`
            fi

            level_name=`echo $level | awk -F. '{print $3"."$4}'` # get the name of the threat and level (1=no, 2=low, 3=mid, 4=high)
            echo "percent.under.threat.$level_name : $percent_level" >> $species/info.txt # write file
            
            # Do mean occurence probability under this threat
            eval `r.univar -g me.c.$species`
            echo "${level_name}.mean.occ.probability : $mean" >> $species/info.txt
            echo "${level_name}.sd.occ.probability : $stddev" >> $species/info.txt

            # Do mean occurence probability not under this threat
            g.remove rast=MASK            
            r.mapcalc "MASK=if(isnull($level))&&if(tmp.$species)"
            eval `r.univar -g me.c.$species`
            echo "not.${level_name}.mean.occ.probability : $mean" >> $species/info.txt
            echo "not.${level_name}.sd.occ.probability : $stddev" >> $species/info.txt

          done
        else
          echo "$danger : NA" >> $species/info.txt    
        fi
      done
  fi
  
  echo "===================================== done with an other one ============================================"
  
  g.remove rast=tmp.$species
  g.remove rast=MASK
done


## Create a threat index with R

# csv
threats <- c()
for (i in list.files(pattern="^[0-9]")) threats <- rbind(threats, t(read.table(paste(i,"/info.txt", sep=""), sep=":"))[2,])
threats <- as.data.frame(threats, stringsAsFactors=F)

names(threats)[1:ncol(threats)] <- t(read.table(paste(i,"/info.txt", sep=""), sep=":", stringsAsFactors=F)[,1])

calc.index <- function(sp_id, dangers=c("rec_conv", "infrastr", "grazing", "fires", "conv_ag", "access_pop")) {

  index <- 0
  tt <- threats[as.numeric(threats[,1])==sp_id,]
  if (tt$percent.lost > 40 &tt$lost.mean.occ.probability > tt$not.lost.mean.occ.probability ) index <- index + 4000
  if (tt$percent.lost > 40 &tt$lost.mean.occ.probability < tt$not.lost.mean.occ.probability ) index <- index + 3000
  if (tt$percent.lost < 40 &tt$lost.mean.occ.probability > tt$not.lost.mean.occ.probability ) index <- index + 2000
  if (tt$percent.lost < 40 &tt$lost.mean.occ.probability < tt$not.lost.mean.occ.probability ) index <- index + 1000 
  for (danger in dangers) for (level in 1:4) index <- index + as.numeric(tt[,paste("percent.under.threat.",danger,".",level," ", sep="")])*level
  return(index)
}


###########################################
# Mean % area protected in and outside 
# protected areas (task 19)
###########################################

# For each species calculate the mean and sd occurence probability within and outside protected areas
# For each species calcualte the % area within and outside protected areas

for species in `ls | grep ^[0-9]`
do
  g.remove rast="MASK"
  r.mapcalc "tmp.$species.1=if(me.c.${species} > 0,1,null())" # calculate temp grid only with the area above threshold
  area_total=`r.sum tmp.${species}.1 | awk -F= '{print $2}'` # sum the area
  r.mapcalc "MASK=if(protected_areas@PERMANENT)"
  eval `r.univar me.c.${species} -g`
  area_protected=`r.sum tmp.${species}.1 | awk -F= '{print $2}'` # sum the area
  percent_protected=`echo "scale=2; $area_protected / $area_total *100" | bc`  
  echo "occ.prob.mean.in.pa : $mean" >> $species/info.txt
  echo "occ.prob.sd.in.pa : $stddev" >> $species/info.txt
  g.remove rast="MASK"  
  r.mask -r
  r.mask -i -o protected_areas@PERMANENT
  eval `r.univar me.c.${species} -g`
  echo "occ.prob.mean.outside.pa : $mean" >> $species/info.txt
  echo "occ.prob.sd.outside.pa : $stddev" >> $species/info.txt
  echo "percent.area.protected : $percent_protected" >> $species/info.txt
done
g.remove rast="MASK"

###########################################
# Calculate for each park, takes about 2hrs (task 20)
###########################################

# for each protected area calculated the 

mkdir parks

for i in `r.category map=protected_areas_cat`
do 
  echo "cat : value" > parks/$i.txt
  # mask only this area
  r.mapcalc "tmp=if(protected_areas_cat==$i,1,null())"
  r.mapcalc "MASK=tmp"  
  for j in `g.mlist type=rast pattern="cla.*[a-z]$"`
  do
    eval `r.univar $j -g`
    echo "$j.max : $max" >> parks/$i.txt
    echo "$j.min : $min" >> parks/$i.txt
    echo "$j.sd : $stddev" >> parks/$i.txt
    echo "$j.mean : $mean" >> parks/$i.txt
  done

  for k in `g.mlist type=rast pattern="ta.*"`
  do
    eval `r.univar $k -g`
    echo "$k.max : $max" >> parks/$i.txt
    echo "$k.min : $min" >> parks/$i.txt
    echo "$k.sd : $stddev" >> parks/$i.txt
    echo "$k.mean : $mean" >> parks/$i.txt
  done
  g.remove rast=MASK
done
g.remove rast=tmp

## Join this values to the map of WDPA
# add columns
v.db.addcol map=pa columns="max_rep double precission"

for i in parks/*
do
  value=`cat $i | awk -F: '/cla.reptilia.max/{print $2}'`
  cat=`echo $i | cut -d/ -f2 | cut -d. -f1`
  v.db.update map=pa column=max_rep value="$value" where="cat=$cat"
  echo "done $i"
done

###########################################
# Species Richness per class (task 21)
###########################################
mkdir summary

for i in `g.mlist type=rast pattern="cla.*[a-z]$"`
do
  > summary/$i.txt 
  r.mapcalc "tmp=if($i>0,1,null())"
  r.mapcalc "MASK=tmp"  
  for j in `g.mlist type=rast pattern="ta.*"`
  do
    eval `r.univar $j -g`
    echo "$j.max : $max" >> summary/$i.txt
    echo "$j.min : $min" >> summary/$i.txt
    echo "$j.sd : $stddev" >> summary/$i.txt
    echo "$j.mean : $mean" >> summary/$i.txt
  done
  r.mask -r 
  area_total=`r.sum $i | awk -F= '{print $2}'`
  r.mask protected_areas
  area_protected=`r.sum $i | awk -F= '{print $2}'` # sum the area that is protected
  percent_protected=`echo "scale=2; $area_protected / $area_total *100" | bc` 
  echo "percent.protected : $percent_protected" >> summary/$i.txt
done

###### Export convex hull grid, env variales and make everyhting ready for the web interface

for i in `ls | grep ^[0-9].*.[0-9]$` # do for all species
do
  #r.out.arc in=me.c.$i out=$i/$i.asc
  #echo $i
  
  echo "lon,lat" >  $i/points.csv
  # exports the coords of the polygon (B) and the centroid (C), awk is used to get extract only the points of the polyon. 
  v.out.ascii in=occ_$i format=point | awk -F'|' '{print $1","$2}' >> $i/points.csv
  echo $i

done

## rename to make the dir ready for the web interface

mkdir ready

for i in `ls | grep ^[0-9].*.[0-9]$` # do for all species
do
  if [ `cat $i/chull.csv | wc -l` -gt 2 ] 
  then
    mkdir ready/$i
    cp $i/points.csv ready/$i/$i.csv
    cp $i/chull.csv ready/$i/${i}-chull.csv
    cp $i/chull_buffer.csv ready/$i/${i}-chull-buff.csv
    cp -v $i/info.txt ready/$i/${i}-info.txt
  fi
done

###### Additional stuff
## chck for env var correlation
r.random sa vector_output=random_env n=5000 --o

for i in $(seq 1 19)
do
v.db.addcol map=random_env columns="bio$i double precision"
v.what.rast vect=random_env rast=bio$i.30sec col=bio$i

done

res <- data.frame(from=rep(NA,19*18/2), to=NA, cor=NA)
count <- 1

for (i in 1:19) for (j in 1:19) if(j>i) {res[count,] <- cbind(i,j,cor(bio[,i],bio[,j])); count <- count + 1}
