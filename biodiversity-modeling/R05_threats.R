# figure 3

library(ggplot2)

th <- read.csv("threats_complete.csv")

name.hash <- data.frame(id=c(1,5,6,7,8,9), names=c("Reptiles", "Amphibians", "Birds", "Insects", "Mammals", "Plants"))
threat.hash <- data.frame(id=c(1,2,3,4), threats=c("Almost no threat", "Some threat", "Mid threat", "High threat"))

th$group.name <- name.hash[match(th$group, name.hash[,1]),2]

### threats
th.sumry <- data.frame(group.name=unique(th$group.name), lQ=NA, median=NA, uQ=NA)
for (i in 1:length(unique(th$group.name))) th.sumry[i,2:ncol(th.sumry)] <- summary(th[th$group.name==unique(th$group.name)[i],'value.highest.risk'])[c(2,3,5)]

ggplot(data=th[order(th$threat.group),]) + geom_bar(aes(x=value.highest.risk,fill=highest.risk), position="fill", binwidth=0.01)+ scale_fill_brewer(palette="Set1")  + facet_wrap(~ group.name)+ geom_density(aes(x=value.highest.risk,y=..scaled..))  + theme_bw() + labs(y="Proprotion",x="Value of highest threat") + geom_vline(data=th.sumry, aes(xintercept=c(lQ, median, uQ)), linetype=2, size=0.4)
 
savePlot("~/12_work/iabin/writing/manuscript.iabin/figures/distribution_threat_per_species_type.png")

###########################################
# figure 4

#--------------------------------------------------------------------#
# cleaning

# create grid where protected = 1 and not protected = 0
#--------------------------------------------------------------------#
# make plot for each species group
#--------------------------------------------------------------------#

# General set up
#--------------------------------------------------------------------#

# load libraries
library(spgrass6)
library(raster)
library(plotrix)

# load datasets from grass
veco <- readVECT6("eco")
vdeco <- veco@data
rsa <- raster(readRAST6("sa"))
reco <- raster(readRAST6("eco_eco"))

rveco <- getValues(reco)
rveco <- ifelse(is.na(rveco),0,rveco)

rvsa <- getValues(rsa)
rvprotected <- getValues(raster(readRAST6("protected_areas")))
rvprotected <- ifelse(is.na(rvprotected),0,1)
rarea <- mask(area(rsa),rsa)
rvarea <- getValues(rarea)

# names of ecoregions
eco.names <- vdeco[!duplicated(vdeco$ECO_ID),]

# group names
sp <- data.frame(grp.id=c(1,5,6,7,8,9),grp.name=c("reptilia","amphibia","aves","insecta", "mammalia", "plantae"),grp.label=c("Reptiles", "Amphibians", "Birds", "Insects", "Mammals", "Plants"))

# prepare eco reagions
eco.df <- data.frame(ECO_ID=unique(rveco), pprotected=0)

# add names for each eco region
eco.df <- eco.df[eco.df[,'ECO_ID']!=0,]
for (i in 1:nrow(eco.df))
	eco.df[i,'name'] <- eco.names[eco.names$ECO_ID==eco.df[i,'ECO_ID'],'ECO_NAME']

for (i in 1:nrow(sp))    
{
   # read raster with the number of species that threatend per pixel
   rth4 <- raster(readRAST6(paste("th4species_grp",sp[i,'grp.id'],"_with_th", sep="")))
   rvth4 <- getValues(rth4)
   rvth4 <- ifelse(is.na(rvth4),0,rvth4)

   # read raster with total number of species of that group per pixel
   rsp <- raster(readRAST6(paste("cla.",sp[i,'grp.name'], sep="")))
   rvsp <- getValues(rsp)
   
   # for each species group calculate the mean number of species and mean number of threatend species for each ecoregion
   count <- 1
   for (j in unique(rveco)) {
      eco.df[eco.df$ECO_ID==j,paste("cla.",sp[i,'grp.id'],".all.sp",sep="")] <- sp.all <- mean(rvsp[rveco==j],na.rm=T)
      eco.df[eco.df$ECO_ID==j,paste("cla",sp[i,'grp.id'],".th4", sep="")] <- sp.th <- mean(rvth4[rveco==j], na.rm=T)
      eco.df[eco.df$ECO_ID==j,paste('pth.',sp[i,'grp.id'],sep="")] <- sp.th / sp.all
      if(i==1) print( eco.df[eco.df$ECO_ID==j,'pprotected'] <- (sum(rvarea[rveco==j & rvprotected==1], na.rm=T) /sum(rvarea[rveco==j],na.rm=T)))
      
      count <- count+1
   }

   # devide ecoregions for each species group into four quantiles
   eco.df[,paste('quant.mean.',i,sep="")] <- 0
   eco.df[,paste('quant.mean.',i,sep="")] <- ifelse(eco.df[,paste('cla',sp[i,1],'.th4', sep="")] < summary(eco.df[,paste('cla',sp[i,1],'.th4',sep="")])[2],1,eco.df[,paste('quant.mean.',i,sep="")] )
   eco.df[,paste('quant.mean.',i,sep="")] <- ifelse(eco.df[,paste('cla',sp[i,1],'.th4', sep="")] >= summary(eco.df[,paste('cla',sp[i,1],'.th4',sep="")])[2]&eco.df[,paste("cla",sp[i,1],".th4", sep="")]<summary(eco.df[,paste('cla',sp[i,1],'.th4', sep="")])[3],2,eco.df[,paste('quant.mean.',i,sep="")] )
   eco.df[,paste('quant.mean.',i,sep="")] <- ifelse(eco.df[,paste('cla',sp[i,1],'.th4', sep="")] >= summary(eco.df[,paste('cla',sp[i,1],'.th4',sep="")])[3]&eco.df[,paste("cla",sp[i,1],".th4", sep="")]<summary(eco.df[,paste('cla',sp[i,1],'.th4', sep="")])[5],3,eco.df[,paste('quant.mean.',i,sep="")] )
   eco.df[,paste('quant.mean.',i,sep="")] <- ifelse(eco.df[,paste('cla',sp[i,1],'.th4', sep="")] >= summary(eco.df[,paste('cla',sp[i,1],'.th4',sep="")])[5],4,eco.df[,paste('quant.mean.',i,sep="")] )


}

cols <- c("darkgreen", "brown", "orange", "red")
with(eco.mean.th4, plot(aprotected,ptotal, pch=quant.mean, col=cols[quant.mean], las=T, ylab="Proportion of total species that within group 4", xlab="Proportion of ecoregion that is under protection"))
with(eco.mean.th4, thigmophobe.labels(aprotected,ptotal,eco.mean.th4$name))
abline(h=mean(eco.mean.th4$ptotal, na.rm=T), col="grey66", lty=2)

par(mfrow=c(2,3))
for (i in 1:nrow(sp)) {
   plot(eco.df$pprotected,eco.df[,paste("pth.",sp[i,1], sep="")],ylim=c(0,.7), pch=eco.df[,paste('quant.mean.',i, sep="")],col=cols[eco.df[,paste('quant.mean.',i, sep="")]],ylab="Portion of species in threat group 4", xlab="Portion of ecoregion under protection")
   title(sp[i, 'grp.label'])
   abline(h=mean(eco.df[,paste("pth.",sp[i,1],sep="")], na.rm=T), col="grey66", lty=2)
   legend("topright",c("1st quantile", "2nd quantile", "3rd quantile", "4th quantile"), pch=1:4, col=cols[1:4])
   #text(0.5,0.25, paste(eco.df[order(eco.df[,paste("pth.",sp[i,1],sep="")],decreasing=T),'name'][1:10], collapse="\n"), pos=4, col="grey50")
   with(head(eco.df[order(eco.df[,paste("pth.",sp[i,1], sep="")],decreasing=T),],11), thigmophobe.labels(pprotected,get(paste("pth.",sp[i,1],sep="")),as.numeric(name)))

}

savePlot("~/12_work/iabin/writing/manuscript.iabin/figures/threats_per_ecoregion.png")


###########################################

tts <- c("ta.aggregate", "ta.conv_ag", "ta.fires", "ta.grazing", "ta.infrastr", "ta.oil_gas", "ta.rec_conv")
tr <- sapply(tts, function(x) raster(readRAST6(x)))
dd <- as.data.frame(sapply(tr, getValues))
dd$highest <- apply(dd,1,max,na.rm=T)
for (i in 1:nrow(dd)) dd[i, 'highest.name'] <- names(dd)[which(dd[i,]==max(dd[i,grep("ta", names(dd))], na.rm=T))][1]





