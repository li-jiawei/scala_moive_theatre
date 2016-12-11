name := "MovieSystem"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.0.2"
val sparkCsvVersion = "1.4.0"
val jacksonVersion = "2.8.1"
val coreNlpVersion = "3.6.0"
val twitterVersion = "1.6.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-twitter" % twitterVersion,
//  "com.databricks" %% "spark-csv" % sparkCsvVersion,
  "edu.stanford.nlp" % "stanford-corenlp" % coreNlpVersion,
  "edu.stanford.nlp" % "stanford-corenlp" % coreNlpVersion classifier "models-english"
//  , "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
  //--------------------------------------------------------------------------------//
)

