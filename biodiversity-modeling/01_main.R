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

### Load parameters and setup working directory assuming R session was onpend from iabin root directorg
load("./parameters/parameters.RData")
setwd(dir.wd)

source(script.write.species)
source(script.make.background)
source(script.run.maxent)

###########################################
# Read fils and create for each species 
# with 10 or more occurences a csv file 
# (Task 9)
###########################################

sp <- apply(species.files.raw,1,write.species.csv,log.file=log.make.species.csv, req.points=pts.min,dir.out=dir.out)
save(sp, file=species.id.to.pro

###########################################
# Create SWD files and run maxent
# (Task 10,11,12 and 13) 
###########################################

files <- list.files(pattern="^[0-9]")
eco <- raster(ecoregions.path)
eco.values <- getValues(eco)

# additional variables to workers
sfExport("eco")
sfExport("eco.values")

st <- proc.time()[3]
# extract backgrounds from wwf biomes task 10
sfSapply(files, function(i) get.background(sp_id=i, ecoregions=eco, v.all=eco.values, no.background=no.background, make.swd=FALSE)) # make.swd is located in the file 002_make_swd.R
et <- proc.time()[3] - st
print(paste("it took",et,"sec"))	

## Make swd files --- optimization and clean up is required task 11
# species files
# mergee all location
system("echo key,lon,lat > ssp_sort_u.csv")
system("cat [0-9]*/training/species.csv | awk -F, '!/species/ {print $2\":\"$3\",\"$2\",\"$3}'| sort -u >> ssp_sort_u.csv") # get unique
system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval ssp_sort_u.csv ",env.full, "> species_swd.csv", sep=""),wait=T) # get swd

# get for every species the points required
swd.info <- read.csv("species_swd.csv")
for (i in list.files(pattern="^[0-9]"))
{
  this.sp.no.points <- system(paste("cat ",i,"/info.txt | awk -F: '/number.of.points/{print $2}'",sep=""), intern=T)
  if (this.sp.no.points < pts.full) {until <- ncol(swd.info) - 4} else {until <- ncol(swd.info)}
  file <- read.csv(paste(i,"/training/species.csv",sep=""))
  write.table(cbind(file[,1:3],swd.info[match(paste(file[,2],":",file[,3],sep=""), swd.info[,1]),4:until]),paste(i,"/training/species_swd.csv", sep=""), row.names=F, quote=F, sep=",")
  print(i)
}


# make for background
system("echo key,lon,lat > bg_sort_u.csv")
system("cat [0-9]*/training/background.csv | awk -F, '!/species/ {print $2\":\"$3\",\"$2\",\"$3}'| sort -u >> bg_sort_u.csv") # get unique
system(paste("java -cp ", dir.maxent, "/maxent.jar density.Getval bg_sort_u.csv ",env.full, "> bg_swd.csv", sep=""),wait=T) # get swd

# get for every species the points required
swd.info <- read.csv("bg_swd.csv")
for (i in list.files(pattern="^[0-9]"))
{
  this.sp.no.points <- system(paste("cat ",i,"/info.txt | awk -F: '/number.of.points/{print $2}'",sep=""), intern=T)
  if (this.sp.no.points < pts.full) {until <- ncol(swd.info) - 4} else {until <- ncol(swd.info)}
  file <- read.csv(paste(i,"/training/background.csv",sep=""))
  file.to.write <- cbind(file[,1:3],swd.info[match(paste(file[,2],":",file[,3],sep=""), swd.info[,1]),4:until])
  write.table(file.to.write[!is.na(file.to.write[,4]),],paste(i,"/training/background_swd.csv", sep=""), row.names=F, quote=F, sep=",")
  print(i)
}


### create the batch files for the servers (genomix1, genomix2)
files <- list.files(pattern="^[0-9]")
split.list <- rep(1:sum(cores),each=ceiling(length(files)/sum(cores)), length.out=length(files))
per.core <- split(files, split.list)
save(max.ram, dir.maxent, no.replicates, replicate.type, dir.proj, file=".maxent_run.Rdata")
t.count <- 1 # tmp count
for (server in 1:length(servers))
{
  # save paramters
  
  if(!file.exists(servers[server])) dir.create(servers[server])
  for (core in 1:cores[server])
  {
    if(length(per.core)>=t.count)
    {
      # write species list    
      write.table(per.core[[t.count]], paste(severs[server],"/species_list_core",core,".txt",sep=""), row.names=F, col.names=F, quote=F)
      # write R batch file for each core
      write(paste("# load params\n",
        "load(.maxent_run.Rdata)\n",
        "# load the maxent function\n",
        "source(003_run_manxent.R\n",
        "# load data\n",
        "files <- read.table(",servers[server],"/species_list_core",core,".txt)\n",
        "# run maxent\n",
        "sapply(files, function(i) run.maxent(sp_id=i,max.ram=max.ram, dir.maxent=dir.maxent, dir.proj=dir.proj, no.repclicates=no.repclicates, replicate.type=replicate.type)",sep=""),
        paste(servers[server],"/runmaxent_core",core,".R",sep=""))
       t.count <- t.count+1
    }
  }
}

me=`hostname`

for f in $me/*.R
do
  screen -S $f -d -m R CMD BATCH $f
done

## run maxent task 
st <- proc.time()[3]
sfSapply(files, function(i) run.maxent(sp_id=i,max.ram=max.ram, dir.maxent=dir.maxent, no.repclicates=no.repclicates, replicate.type=replicate.type)) # run.maxent is located in the file 003_run_maxent.R 

et <- proc.time()[3] - st
print(paste("it took",et,"sec"))	



###########################################
# Extract metrics from the model and 
# import rasters to grass (task 15)
###########################################

files <- list.files(pattern="^[0-9].*.tar.gz")
eval.stats <- data.frame(id=rep(NA, length(files)), avg.auc_train=NA, avg.auc_test=NA, sd.auc=NA, prevalence=NA, ten.percentile=NA, roc=NA)

# move species that havent been done to a folder redo 
system("mkdir redo")
system("mv ls -1 | grep ^[0-9]*[0-9]$ | xargs mv redo")

# This script must be run within a grass shell
# check mapset and region are right
# setting up grass
system("g.mapset mapset=trial.v2 -c")
system("g.region sa")
system("g.region res=00:05")

for (i in 1:length(files)){
  system(paste("tar -zxf",files[i]))
  this.id=strsplit(files[i],"\\.")[[1]][1]
  for (j in 0:(no.replicates-1)){
    roc <- c()
    omission.rate <- read.csv(paste(this.id, "/cross/", this.id, "_",j,"_omission.csv", sep=""))
    spec <- omission.rate$Fractional.area
    sens <- 1-omission.rate$Training.omission
    log.vals <- omission.rate$Corresponding.logistic.value
        
    abs.dif <- abs(1-(sens+spec))

    if (length(log.vals[which(abs.dif== min(abs.dif))]) > 1) {
      roc <- c(roc, mean(log.vals[which(abs.dif==min(abs.dif))]))
    } else roc <- c(roc, log.vals[which(abs.dif==min(abs.dif))])
  }

  me.res <- read.csv(paste(this.id, "/cross/maxentResults.csv", sep=""))
  eval.stats[i,'id'] <- this.id
  eval.stats[i,'avg.auc_train'] <- me.res[(no.replicates+1),'Training.AUC']
  eval.stats[i,'avg.auc_test'] <- me.res[(no.replicates+1),'Test.AUC']
  eval.stats[i,'sd.auc'] <- me.res[(no.replicates+1),'AUC.Standard.Deviation']
  eval.stats[i,'prevalence'] <- me.res[(no.replicates+1),'Prevalence..average.of.logistic.output.over.background.sites.']
  eval.stats[i,'ten.percentile'] <- me.res[(no.replicates+1),'X10.percentile.training.presence.logistic.threshold']
  eval.stats[i,'roc'] <- mean(roc)
  print(paste("finished ", i, "of", length(files), "species."))
  
  system(paste("r.in.arc in=",this.id,"/proj/",this.id,".asc out=me.",this.id," --o  --q",sep=""),wait=T)
  system(paste("cat ",this.id,"/training/species.csv | awk -F',' 'NR > 1 {print $2 \"|\" $3}' | v.in.ascii out=occ_",this.id," --o --q",sep=""),wait=T)
  system(paste("ls ",this.id," | grep -v info.txt |awk '{print\"",this.id,"/\" $0}' | xargs rm -rf",sep=""),wait=T)
  system(paste("mv",files[i],this.id))
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


###########################################
# Do CHull, export chull as buffer and cut 
# predictions to buffered chull (task 14)
###########################################

# description: cuts the predictions by a buffered convex hull of the occurence points and writes a *.csv with the coordinates of the convexhull and the buffered convex hull.

buffer_distance=2 # in degree

# initizialise
count=1
total=`ls | grep ^[0-9].* | wc -l` 

for i in `ls | grep ^[0-9].*`
do
  # chull
  v.hull in=occ_$i out=occ_$i\_hull --o --q
  # buff chull  
  v.buffer in=occ_$i\_hull out=occ_$i\_hull_buffer distance=$buffer_distance type=area --o --q

  # export chull
  v.to.points -v in=occ_$i\_hull out=tmp --o --q
  echo "lon,lat" >  $i/chull.csv
  v.out.ascii in=tmp format=point | awk -F'|' '{print $1","$2}' >> $i/chull.csv

  # export buffered chull
  v.to.points -v in=occ_$i\_hull_buffer out=tmp --o --q
  echo "lon,lat" > $i/chull_buffer.csv
  v.out.ascii in=tmp format=point | awk -F'|' '{print $1","$2}' >> $i/chull_buffer.csv

  # cut predicitoins buffer
  v.to.rast in=occ_${i}_hull_buffer out=tmp use=val value=1 --q
  r.mapcalc "MASK=tmp"
  r.mapcalc "me.c.$i=me.$i"
  g.remove rast=tmp,MASK,me.$i --q
    
  
  echo processed $count of $total
  count=$(( $count + 1 ))
done

###########################################
# Species Richness per genus (task 16)
###########################################

echo "class,fam_id,gen_id,sp_id" > tax.txt

for i in `ls | grep ^[0-9].*`
do
  class=`awk -F' : ' '/class/ {print $2}' $i/info.txt`
  fam=`awk -F' : ' '/family_id/ {print $2}' $i/info.txt`
  gen=`awk -F' : ' '/genus_id/ {print $2}' $i/info.txt`
  sp=`awk -F' : ' '/species id/ {print $2}' $i/info.txt`
  echo "$class,$fam,$gen,$sp" >> tax.txt

done

# species richness per genus
# genus is the third column in the file tax.txt, hence take unique values

auc_th=0.7
occ_th=roc

for genus in `awk -F, 'NR>1 {print $3}' tax.txt | sort -u`  # for each unique genus
do
  for species in `awk -F, '/'$genus'/{if(NR>1); print $4}' tax.txt` # for each species that belongs to the genus $genus
  do
    auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt`
    if [  $(echo "$auc_species > $auc_th" | bc) -gt 0 ] # check if the AUC is above the required threshold
    then
      threshold=`awk -F" : " '/roc/{print $2}' $species/info.txt` # get the threshold from the species
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

g.mremove rast="gen.sp.*" -f

###########################################
# Species Richness per class (task 17)
###########################################

# species richness per class
auc_th=0.7
occ_th=roc

for class in `awk -F, 'NR>1 {print $1}' tax.txt | sort -u`  # for all classes
do
  for species in `awk -F, '/'$class'/{if(NR>1); print $4}' tax.txt` # get all species that belong to class a class
  do
    auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt` # get the species auc
    if [  $(echo "$auc_species > $auc_th" | bc) -gt 0 ] # treat a species only if its AUC ia above the critical threshold
    then
      threshold=`awk -F" : " '/roc/{print $2}' $species/info.txt`
      r.mapcalc "cla.sp.$class.$species=if(isnull(me.c.${species}) ||| me.c.${species} < $threshold,0,1)" # only consider area above the threshold
    fi
  done
  g.mlist type=rast pattern="cla.sp.$class.*" > tmp.sf

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
  g.copy rast=$tmp,cla.$class
done


###########################################
# Threat for each species (task 18)
###########################################

auc_th=0.7
occ_th=roc


# average threat

for species in `ls | grep ^[0-9]`
do
auc_species=`awk -F" : " '/avg.auc_test/{print $2}' $species/info.txt`

if [ $(echo "$auc_species > $auc_th" | bc) -gt 0 ]
then
threshold=`awk -F" : " '/roc/{print $2}' $species/info.txt` # get species threshold
r.mapcalc "tmp.$species=if(me.c.${species} >= $threshold,1,null())" # calculate temp grid only with the area above threshold
r.mapcalc "MASK=tmp.$species" # mask to area above threshold
area=`r.sum tmp.$species | awk -F= '{print $2}'` # sum the area
echo "area.above.th.in.pixel : $area" >> $species/info.txt # write to file
  for danger in `g.mlist type=rast mapset=threats pattern="ta.*"` # loop through all threats
    do   
    if [ $(echo "$area > 0" | bc) -gt 0 ]  # only treat species where the area above the threshold is > 0
    then 
      # mean threat
      area_danger=`r.sum $danger@threats | awk -F= '{print $2}'` # sum up the danger for this species
      mean=`echo "scale=2; $area_danger / $area" | bc`      # calculate the man danger
      echo "$danger : $mean" >> $species/info.txt    # write mean danger to info file
      
      # % low, mid, high, no threat
      for level in `g.mlist type=rast mapset=threats pattern="tac.$danger*"` # calculate the % area of each species under each low, mid, high and no thread
      do
        area_level=`r.sum $level@threats | awk -F= '{print $2}'` # calculate area of threat at no, low, mid, high
        percent_level=`echo "scale=2; $area_level / $area * 100" | bc` # calculate the percentage
        level_name=`echo $level | awk -F. '{print $3"."$4}'` # get the name of the threat and level (1=no, 2=low, 3=mid, 4=high)
        echo "percent.under.threat.$level_name : $percent_level" >> $species/info.txt # write file
      done
     else
      echo "$danger : missing.data.test" >> $species/info.txt    
    fi
  done
fi
g.remove rast=tmp.$species
g.remove rast=MASK
done




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
  r.mask -r
  r.mask -i protected_areas@PERMANENT
  eval `r.univar me.c.${species} -g`
  echo "occ.prob.mean.outside.pa : $mean" >> $species/info.txt
  echo "occ.prob.sd.outside.pa : $stddev" >> $species/info.txt
  echo "percent.area.protected : $percent_protected" >> $species/info.txt
done
g.remove rast="MASK"

###########################################
# Species Richness per class (task 20)
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

###########################################
# Species Richness per class (task 21)
###########################################
mkdir classes

for i in `g.mlist type=rast pattern="cla.*[a-z]$"`
do
  > classes/$i.txt 
  r.mapcalc "tmp=if($i>0,1,null())"
  r.mapcalc "MASK=tmp"  
  for j in `g.mlist type=rast pattern="ta.*"`
  do
    eval `r.univar $j -g`
    echo "$j.max : $max" >> classes/$i.txt
    echo "$j.min : $min" >> classes/$i.txt
    echo "$j.sd : $stddev" >> classes/$i.txt
    echo "$j.mean : $mean" >> classes/$i.txt
  done
  r.mask -r 
  area_total=`r.sum $i | awk -F= '{print $2}'`
  r.mask protected_areas
  area_protected=`r.sum $i | awk -F= '{print $2}'` # sum the area that is protected
  percent_protected=`echo "scale=2; $area_protected / $area_total *100" | bc` 
  echo "percent.protected : $percent_protected" >> classes/$i.txt
done


