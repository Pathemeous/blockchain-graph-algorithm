# Reading in data
data3 = read.table("output.data", TRUE)
data2 = t(data3)
data = data.frame(
  type = data2["type",]
  , size = data2["size",]
  , mean = as.numeric(data2["time",])
  , std  = as.numeric(data2["std",])
)

# min and max y-position for stderror bars
data$emin <- (data$mean - data$std)
data$emax <- (data$mean + data$std)

# (Un)comment this line if you only want calculation time or everything
 data = data[data$type=="calc", ]

# Let the Plotting commence!
library("ggplot2")
qplot(size, mean, data=data, geom=c("line","point"), colour=type, group=type) +
  geom_errorbar(aes(x=size, ymin=emin, ymax=emax)) + 
  geom_line(aes(size, mean))
