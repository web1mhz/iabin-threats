#### Set of funcitons to make life easier

# inc(x,increment=1) increments x by a value increment, default is 1
inc <- function(x,increment=1) eval.parent(substitute(x <- x + increment))

# introduce some unix commands

# get wd
pwd <- function() getwd()
 
# set wd
cd <- function(x) setwd(x)

# remove all objects
rma <- function() rm(list=ls())

# sapply with progress, originating from plyr??

sapply_pb <- function(X, FUN, ...)
{
   env <- environment()
   pb_Total <- length(X)
   counter <- 0
   pb <- txtProgressBar(min = 0, max = pb_Total, style = 1)

   wrapper <- function(...){
      curVal <- get("counter", envir = env)
      assign("counter", curVal +1 ,envir=env)
      setTxtProgressBar(get("pb", envir=env), curVal +1)
      FUN(...)
    }
    sapply(X, wrapper, ...)
    close(pb)
}
