
second <- gbif <- read.table("sa_reptilia.csv", sep=",", stringsAsFactors=F, head=T)
first <- iabin <- read.table("SSTN_filtered_Reptilia.csv", sep=",", stringsAsFactors=F)

names(first) <- c("sp_id", "specie", "gen_id", "genus", "family_id", "family", "lat", "lon")


first.d <- first[paste(first[,c("lon","lat")], collapse="")%in%paste(second[,c("lon", "lat")], collapse=""),]
second.d <- second[paste(second[,c("lon","lat")], collapse="")%in%paste(first[,c("lon", "lat")], collapse=""),]

if (nrow(first.d) == 0 & nrow(second.d) == 0) print("no duplicates, no further action required") else print("some points occur more than once")

### merge

lookup <- data.frame(no=1:length(unique(c(first$specie, second$specie))), specie=unique(c(first$specie,second$specie)), stringsAsFactors=F)


## correct spelling with the help of google
require(RCurl)
require(stringr)

lookup$corrected <- NA
lookup$new.value <- NA
lookup$fuzzyMatchOk <- NA

for (n in 1:nrow(lookup)){

  url <- paste("http://www.google.com/search?hl=en&q=", paste(strsplit(lookup[n,'specie']," ")[[1]][1:2], collapse="+"), sep="")
  g <- getURL(url)

  if(grepl("Did you mean", g, ignore.case=F)) {
    
    s2.split <- strsplit(strsplit(g, "Did you mean")[[1]][2], "Search search results")[[1]][1]
    sug <- gsub("\\<.{1,2}\\>", "", strsplit(strsplit(s2.split, "spell>")[[1]][2], "</a>")[[1]][1],perl=TRUE)
        
    lookup[n,'corrected'] <- sug   
    lookup[n,'new.value'] <- 1       

  } else if (grepl("Showing results for", g, ignore.case=F)) {

    s2.split <- strsplit(strsplit(g, "Showing results for")[[1]][2], "Search instead for")[[1]][1]
    sug <- gsub("\\<.{1,2}\\>", "", strsplit(strsplit(s2.split, "spell>")[[1]][2], "</nobr>")[[1]][1], perl=TRUE)
    lookup[n,'corrected'] <- sug
    lookup[n,'new.value'] <- 1  

  } else {lookup[n, 'corrected'] <- lookup[n,'specie']; lookup[n,'new.value'] <- 0}  
  
    lookup[n, 'fuzzyMatchOk'] <- ifelse(length(agrep(lookup[n,'specie'], lookup[n,'corrected'],max=1))>0, T, F) 
  
  print(n)
  print(lookup[n, 'corrected'])

}


















