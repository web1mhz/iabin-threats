###########################################
# most important variable and performance

ev <- read.csv("evaluation_statistics.csv")

for (i in unique(ev$group)) {
  print("-----------------------")
  print(i)
  print(round(with(ev[ev$group==i,], table(max.contribution))/nrow(ev[ev$group==i,])*100,2))

}


 print(round(with(ev[,], table(max.contribution))/nrow(ev[,])*100,2))
