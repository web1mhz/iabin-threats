# Analyse the origin of points
# and how much IABIN increased the number of species that can potentially be modelled


###########################################
### Mean number of points per database

f <- list.files(pattern="^[0-9].*[0-9]$")
fields <- c("species id ", "pts.gbif ", "pts.iabin.sstn ", "number.of.points ")

write(paste(fields, collapse=","), "origin_pts.csv", append=F)
write(paste(fields, collapse=","), "error.origin_pts.csv", append=F)

# extract all values
system.time(lapply(f, rw.threat))

# function that extracts the values
rw.threat <- function(id, file="origin_pts.csv", to.extract=fields) {

  tmp <- read.table(paste(id, "/info.txt", sep=""), sep=":", stringsAsFactors=F)
  extracted <- tmp[tmp[,1] %in% to.extract,2]
  if(length(extracted)==length(to.extract)) {
    write(paste(extracted, collapse=","), file, append=T)
  } else write(paste(tmp[tmp[1,] %in% to.extract,2], collapse=","), paste("error.",file,sep=""), append=T)

}


fo <- read.csv("origin_pts.csv")
fo$group <- as.numeric(substr(fo$species.id,1,1))
fo$pts.total <- with(fo, pts.gbif+pts.iabin.sstn)
fo$p.gbif <- with(fo, pts.gbif/pts.total *100)
fo$p.gbif <- with(fo, pts.gbif/pts.total *100)
fo$p.iabin <- with(fo, pts.iabin.sstn/pts.total *100)

print("....total.....")
with(fo, mean(pts.total))
with(fo, median(pts.total))
with(fo, sd(pts.total))
with(fo, sd(p.gbif))
with(fo, mean(p.gbif))
with(fo, median(p.gbif))

for (i in unique(fo$group)) {
  print("-----------------------")
  print(i)
  print(paste("mean total:",with(fo[fo$group==i,], mean(pts.total))))
  print(paste("median total:",with(fo[fo$group==i,], median(pts.total))))
  print(paste("sd total:",with(fo[fo$group==i,], sd(pts.total))))
  print(paste("mean gbif:",with(fo[fo$group==i,], mean(p.gbif))))
  print(paste("median gbif:",with(fo[fo$group==i,], median(p.gbif))))
  print(paste("sd gbif:",with(fo[fo$group==i,], sd(p.gbif))))
}

# where gibif is less than 10
for (i in unique(fo$group)) {
  print("-----------------------")
  print(i)
  print(paste("number of instances gbif < 10 pts:",nrow(fo[fo$group==i & fo$pts.gbif<10,])/nrow(fo[fo$group==i,])))  
}

print(paste("number of instances gbif < 10 pts:",nrow(fo[fo$pts.gbif<10,])/nrow(fo[,])))  


###########################################
# Table 1, how do the points distribute

tax <- read.csv("tax.csv")

for (i in unique(tax$class))
{
  print(i)
  print(paste("No of families:",length(unique(tax[tax$class==i,'fam_id']))))
  print(paste("No of generas:",length(unique(tax[tax$class==i,'gen_id']))))
  print(paste("No of species:",length(unique(tax[tax$class==i,'sp_id']))))
  print("---------------------------------------------------")
}

# total
print(paste("No of families:",length(unique(tax[,'fam_id']))))
print(paste("No of generas:",length(unique(tax[,'gen_id']))))
print(paste("No of species:",length(unique(tax[,'sp_id']))))

# auc
ev <- read.csv("evaluation_statistics.csv")
ev$group <- as.numeric(substr(ev$id,1,1))

for (i in unique(ev$group)) print(paste(i, "sp. auc. greater", nrow(ev[ev$avg.auc_test>=0.7 & ev$group==i,])))


