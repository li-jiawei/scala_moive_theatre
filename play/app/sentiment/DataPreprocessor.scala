package sentiment

import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
//import org.apache.spark.sql.{Row, SparkSession, _}
import org.apache.spark.sql.functions._
import javax.inject.Inject

import akka.actor.{Actor, ActorRef, Props}
import play.Configuration
import play.api.Play
import play.api.Play.current
import play.api.mvc._
import play.api.i18n.Lang

import scala.io.Source

/**
  * Created by Jiawei on 11/23/16.
  */

//class DataPreprocessor {
//  @Inject val configuration: Configuration
//
//}

object DataPreprocessor {

  val stopwordFilename = "NLTK_English_Stopwords_Corpus.txt"
  val naiveBayesModelSavePath = "naiveModel"

//  def cleanAndTransformDF(df: DataFrame,
//                          cleanFunction: (String, List[String]) => Seq[String],
//                          sw: List[String])
//                         (implicit preCleanColName: String = "review",
//                          vectorColName: String = "vector"): DataFrame = {
//    val cleanUDF = udf {
//      cleanFunction(_: String, sw)
//    }
//    val cleanedCol = "words"
//    val cleanDF = df
//      .withColumn(cleanedCol, cleanUDF(col(preCleanColName)))
//      .drop(preCleanColName)
//
//    val hashingTF = new org.apache.spark.ml.feature.HashingTF()
//      .setInputCol(cleanedCol)
//      .setOutputCol(vectorColName)
//    hashingTF.transform(cleanDF).drop(cleanedCol)
//
//  }

//  def cleanAndTransformRDD(df: DataFrame,
//                           cleanFunction: (String, List[String]) => Seq[String],
//                           sw: List[String]): RDD[LabeledPoint] = {
//    val hashingTF = new org.apache.spark.mllib.feature.HashingTF()
//    df.rdd.map {
//      case Row(label: Int, review: String) =>
//        val words = cleanFunction(review, sw)
//        val vector = hashingTF.transform(words)
//        LabeledPoint(label, vector)
//    }
//  }

//  def loadDataset(session: SparkSession, filename: String): DataFrame = {
//    val customSchema = StructType(Array(
//      StructField("label", IntegerType, true),
//      StructField("id", StringType, true),
//      StructField("date", DateType, true),
//      StructField("query", StringType, true),
//      StructField("username", StringType, true),
//      StructField("review", StringType, true)))
//    val training = session.read
//      .format("csv")
//      .option("header", false)
//      .schema(customSchema)
//      .load(filename)
//      .toDF("label", "id", "date", "query", "username", "review")
//    training.drop("id", "date", "query", "username")
//  }

  def loadStopWords(filename: String): List[String] = {
    Source.fromFile(filename).getLines.toList
//    Source.fromInputStream(getClass.getResourceAsStream("/" + filename)).getLines().toList
  }

  def cleanDataset(text: String, stopWordsList: List[String]): Seq[String] = {
    text.toLowerCase()
      .replaceAll("\n", "") // match new line
      .replaceAll("rt\\s+", "") // match RT chars
      .replaceAll("\\s+@\\w+", "") // match @people
      .replaceAll("@\\w+", "")
      .replaceAll("\\s+#\\w+", "") // match #hashtag
      .replaceAll("#\\w+", "")
      .replaceAll("(?:https?|http?)://[\\w/%.-]+", "") // match url
      .replaceAll("(?:https?|http?)://[\\w/%.-]+\\s+", "")
      .replaceAll("(?:https?|http?)//[\\w/%.-]+\\s+", "")
      .replaceAll("(?:https?|http?)//[\\w/%.-]+", "")
      .split("\\W+")
      .filter(_.matches("^[a-zA-Z]+$"))
      .filter(!stopWordsList.contains(_))
  }

  def cleanAndTransformToVector(text: String, cleanFunction: (String, List[String]) => Seq[String],sw: List[String]): Vector = {
    val hashingTF = new org.apache.spark.mllib.feature.HashingTF()
    val words = cleanFunction(text, sw)
    val vector = hashingTF.transform(words)
    vector
  }
}
