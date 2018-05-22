name := "loganalytics"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "Spark Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

libraryDependencies += "spark.jobserver" %% "job-server" % "0.8.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"
