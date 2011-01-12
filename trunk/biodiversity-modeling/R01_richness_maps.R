
###########################################
### maps with species richness
library(raster)
library(maps)
library(fields)

path <- ""

plantae <- raster(paste(path,"insects.asc", sep=""))
aves <- raster(paste(path,"insects.asc", sep=""))
reptilia <- raster(paste(path,"insects.asc", sep=""))
insects <- raster(paste(path,"insects.asc", sep=""))
mammalia <- raster(paste(path,"insects.asc", sep=""))
amphibia <- raster(paste(path,"insects.asc", sep=""))

f <- list.files(pattern="cla.*")
for (i in f) assign(strsplit(i,"\\.")[[1]][2],raster(i))


png(file="richness.png", width=18, height=17.75, unit="cm", res=300)

par(mfrow=c(2,3))
# 1
par(mar=c(5,3,5,1))
class <- plantae
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Plants", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)

axis(2, at=seq(-65,10,10),label=F, tck=-0.02) 
axis(2, at=seq(-60,15,10), las=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))



#2
par(mar=c(5,2,5,2))
class <- aves
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Birds", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))

#3
par(mar=c(5,1,5,3))
class <- reptilia
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Reptiles", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)

axis(4, at=seq(-65,10,10),label=F, tck=-0.02) 
axis(4, at=seq(-60,15,10), las=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))


#4
par(mar=c(6,3,4,1))
class <- insecta
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Insects", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)
axis(2, at=seq(-65,10,10),label=F, tck=-0.02) 
axis(2, at=seq(-60,15,10), las=T)

axis(1, at=seq(-85,10,10),label=F, tck=-0.02) 
axis(1, at=seq(-80,15,10), las=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))

#5
par(mar=c(6,2,4,2))
class <- mammalia
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Mammals", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)
axis(1, at=seq(-85,10,10),label=F, tck=-0.02) 
axis(1, at=seq(-80,15,10), las=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))

#6
par(mar=c(6,1,4,3))
class <- amphibia
plot(0, xlim=c(-82,-34), ylim=c(-57,14), xaxt="n", yaxt="n", xlab="", ylab="", main="Richness of Amphibians", asp=1)
rect(par("usr")[1],par("usr")[3],par("usr")[2],par("usr")[4],col="cornflowerblue")
map("world", add=T, fill=T, col="grey", lwd=0.5)
image(class, col=rev(terrain.colors(50)), add=T)
map("world", add=T, lwd=0.5)
abline(h=0, lty=2, lwd=0.5)
box()

rr <- range(getValues(class), na.rm=T)
axis(1, at=seq(-85,10,10),label=F, tck=-0.03) 
axis(1, at=seq(-80,15,10), las=T)

axis(4, at=seq(-65,10,10),label=F, tck=-0.02) 
axis(4, at=seq(-60,15,10), las=T)

image.plot( zlim=c(rr[1],rr[2]), nlevel=50,legend.only=TRUE, horizontal=T,col=rev(terrain.colors(50)))

dev.off()

###########################################
# Total richness
library(spgrass6)
library(raster)

groups <- c("amphibia", "aves", "insecta", "mammalia", "plantae", "reptilia")
for (x in groups) assign(paste("cla.", x, sep=""), raster(readRAST6(paste("cla.",x,sep=""))))

cla.all <- cla.amphibia+cla.aves+cla.insecta+cla.mammalia+cla.plantae+cla.reptilia
cla.all.w1 <- (cla.amphibia^2+cla.aves^2+cla.insecta^2+cla.mammalia^2+cla.plantae^2+cla.reptilia^2)/(cla.amphibia+cla.aves+cla.insecta+cla.mammalia+cla.plantae+cla.reptilia)

cla.all.w2=(cla.amphibia/384+cla.aves/2044+cla.insecta/407+cla.mammalia/493+cla.plantae/15225+cla.reptilia/295)

par(mfrow=c(1,3))
plot(cla.all, main="All (summed)")
plot(cla.all.w1, main="All (squared and divided)")
plot(cla.all.w2, main="All normalised and summed")

## change color scale to quartiles as breaks
coltab <- designer.colors(col=c("grey50", "orange", "yellow","red", "blue"),x=summary(b)[c(1,2,3,5,6)])

b <- getValues(cla.all)
b <- b[!is.na(b)]
b <- (b-min(b))/(max(b)-min(b))
summary(b)

coltab <- designer.colors(col=c("grey50", "orange", "yellow","red", "blue"),x=summary(b)[c(1,2,3,5,6)])

