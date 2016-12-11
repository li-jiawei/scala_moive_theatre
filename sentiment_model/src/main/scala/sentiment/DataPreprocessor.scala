package sentiment

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession, _}
import org.apache.spark.sql.functions._

import scala.io.Source

/**
  * Created by Jiawei on 11/23/16.
  */
object DataPreprocessor {
  val stopwordFilename = "NLTK_English_Stopwords_Corpus.txt"
  val modelAccuracyPath = "/Users/Jiawei/Desktop/final/sentiment140/accuracy2"
  val sar14File = "/Users/Jiawei/Desktop/final/sar14/SAR14.txt"
  val stopWordsFilename = "NLTK_English_Stopwords_Corpus.txt"
  val naiveBayesModelSavePath = "/Users/Jiawei/Desktop/final/sar14/naiveModel"

  def cleanAndTransformDF(df: DataFrame,
                          cleanFunction: (String, List[String]) => Seq[String],
                          sw: List[String])
                         (implicit preCleanColName: String = "review",
                          vectorColName: String = "vector"): DataFrame = {
    val cleanUDF = udf {
      cleanFunction(_: String, sw)
    }
    val cleanedCol = "words"
    val cleanDF = df
      .withColumn(cleanedCol, cleanUDF(col(preCleanColName)))
      .drop(preCleanColName)

    val hashingTF = new org.apache.spark.ml.feature.HashingTF()
      .setInputCol(cleanedCol)
      .setOutputCol(vectorColName)
    hashingTF.transform(cleanDF).drop(cleanedCol)

  }

  def cleanAndTransformRDD(df: DataFrame,
                           cleanFunction: (String, List[String]) => Seq[String],
                           sw: List[String]): RDD[LabeledPoint] = {
    val hashingTF = new org.apache.spark.mllib.feature.HashingTF()
    df.rdd.map {
      case Row(label: Double, review: String) =>
        val words = cleanFunction(review, sw)
        val vector = hashingTF.transform(words)
        LabeledPoint(label, vector)
    }
  }

  def loadDataset(session: SparkSession, filename: String): DataFrame = {
    val customSchema = StructType(Array(
      StructField("label", IntegerType, true),
      StructField("id", StringType, true),
      StructField("date", DateType, true),
      StructField("query", StringType, true),
      StructField("username", StringType, true),
      StructField("review", StringType, true)))
    val training = session.read
      .format("csv")
      .option("header", false)
      .schema(customSchema)
      .load(filename)
      .toDF("label", "id", "date", "query", "username", "review")
    training.drop("id", "date", "query", "username")
  }

  def loadStopWords(filename: String): List[String] = {
    Source.fromInputStream(getClass.getResourceAsStream("/" + filename)).getLines().toList
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

  def scaleScore(label: Double): Double = {
    label match {
      case x if x <= 2 => 0
      case x if x <= 4 => 1
      case x if x <= 6 => 2
      case x if x <= 8 => 3
      case x if x <= 10 => 4
      //      case x: Int if x <= 4 => 0
      //      case x: Int if x <= 6 => 1
      //      case x: Int if x <= 10 => 2
    }
  }

  def loadAndFormatData(sc: SparkContext, filename: String): DataFrame ={
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val rawData = sc.textFile(filename)
    val temp = rawData.map {
      line =>
        val arr = line.split(""" " ,""")
        (arr(1).toDouble, arr(0))
    }.cache()

    temp.map{ case (label, review) =>
      (scaleScore(label), review)
    }.toDF("label", "review")
  }

  def createSparkContext(): SparkContext = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName(this.getClass.getSimpleName)
      .set("spark.serializer", classOf[KryoSerializer].getCanonicalName)
    val sc = SparkContext.getOrCreate(conf)
    sc
  }

}
