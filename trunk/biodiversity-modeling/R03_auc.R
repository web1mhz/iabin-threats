###########################################
# AUC

paste("auc > 0.7:", nrow(ev[ev$avg.auc_test>=0.7,])/nrow(ev))
paste("auc > 0.8:", nrow(ev[ev$avg.auc_test>=0.8,])/nrow(ev))
paste("auc > 0.9:", nrow(ev[ev$avg.auc_test>=0.9,])/nrow(ev))
paste("auc > 0.95:", nrow(ev[ev$avg.auc_test>=0.95,])/nrow(ev))

with(ev, wilcox.test(avg.auc_test, avg.auc_train))

for (i in unique(ev$group)) {
  print("-----------------------")
  print(i)
  print(paste("auc > 0.7:", nrow(ev[ev$group==i & ev$avg.auc_test>=0.7,])/nrow(ev[ev$group==i,])))

}

with(ev, boxplot(avg.auc_test~is.na(bio17)))

nrow(ev[ev$avg.auc_test < 0.7 & !is.na(ev$bio17),])/nrow(ev[!is.na(ev$bio17),])*100
nrow(ev[ev$avg.auc_test < 0.7 & is.na(ev$bio17),])/nrow(ev[is.na(ev$bio17),])*100

wilcox.test(ev[is.na(ev$bio17),'avg.auc_test'], ev[!is.na(ev$bio17),'avg.auc_test'])

mean(ev[is.na(ev$bio17),'avg.auc_test'])
mean(ev[!is.na(ev$bio17),'avg.auc_test'])

###########################################
# plot AUC vs no of poings

par(mar=c(4,4,2,2))
plot(jitter(pts[,4],0.5), ev[,3], xlim=c(10,150), pch=".", las=T, ylab="mean test AUC", xlab="number of points")
abline(h=0.7, lty=2, col="red")
subpl <- subplot(plot(log(pts[,4]), ev[,3],pch=".",ylab="", xlab="Log number of points", las=T,cex.lab=0.8, cex.axis=0.8),120,0.4, c(2,2))
op <- par(no.readonly=T)
par(subpl)
abline(h=0.7, lty=2, col="red")
abline(v=log(40), col="darkgreen", lty=2)
abline(v=log(150), col="grey50")
par(op)
abline(v=40, col="darkgreen", lty=2)
