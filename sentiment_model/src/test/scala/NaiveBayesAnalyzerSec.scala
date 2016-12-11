///**
//  * Created by Jiawei on 11/22/16.
//  */
//import org.apache.spark.ml.{Pipeline, PipelineModel}
//import org.apache.spark.ml.classification.{NaiveBayes, NaiveBayesModel}
//import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
//import org.apache.spark.ml.feature.StopWordsRemover
//import org.apache.spark.ml.feature.HashingTF
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.{DataFrame, Row, SparkSession}
//import org.scalatest.{FlatSpec, Matchers}
//import sentiment.NaiveBayesDFAnalyzer._
//import sentiment.PreprocessData._
//
//import scala.util.{Success, Try}
//
//class NaiveBayesAnalyzerSec extends FlatSpec with Matchers{
//  val spark = SparkSession
//    .builder()
//    .master("local[*]")
//    .appName("Movie-Sentiment-test")
//    .getOrCreate()
//  spark.sparkContext.setLogLevel("Warn")
//
//  it should "work for loading data set" in {
//    val df: DataFrame = loadDataset(spark, trainingDataFilename)
//    df.columns should matchPattern {
//      case Array("label", "review") =>
//    }
//  }
//
//  it should "work for loading stop words" in {
//    val wordList = loadStopWords(stopwordFilename)
//    assert(wordList.size > 0)
//  }
//
//  it should "work for cleaning data" in {
//    val df: DataFrame = loadDataset(spark, trainingDataFilename)
//    val sw: List[String] = loadStopWords(stopwordFilename)
//    val cdf = cleanDataFrame(df, "review", "words", sw)
////    cdf.col("words").
//    cdf.printSchema()
//    cdf.columns should matchPattern {
//      case Array("label", "words") =>
//    }
//  }
//
//  it should "work for transforming data" in {
//    val df: DataFrame = loadDataset(spark, trainingDataFilename)
//    val sw: List[String] = loadStopWords(stopwordFilename)
//    val cdf = cleanDataFrame(df, "review", "words", sw)
//    val tdf = transformToVector(cdf, "words", "vectors")
//    //    cdf.col("words").
//    tdf.printSchema()
//    println(tdf.first())
//    tdf.columns should matchPattern {
//      case Array("label", "vectors") =>
//    }
//  }
//
////  it should "work for building model" in {
////    val df: DataFrame = loadDataset(spark, trainingDataFilename)
////    val sw: List[String] = loadStopWords(stopwordFilename)
////    val cdf = cleanDataFrame(df, "review", "words", sw)
////    val tdf = transformToVector(cdf, "words", "vectors")
////    //    cdf.col("words").
////    val naiveBayes = new NaiveBayes()
////      .setModelType("multinomial")
////      .setLabelCol("label")
////      .setFeaturesCol("vectors")
////      .setSmoothing(500)
////
////    val naiveBayesModel: Try[NaiveBayesModel] = Try(naiveBayes.fit(tdf))
////    naiveBayesModel should matchPattern {
////      case Success(model) =>
////    }
////    naiveBayesModel.foreach(_.write.overwrite().save(naiveBayesModelSavePath))
////  }
//
////  it should "work for validating model" in {
////    val model = NaiveBayesModel.load(naiveBayesModelSavePath)
////    val df: DataFrame = loadDataset(spark, testingDataFilename)
////    val sw: List[String] = loadStopWords(stopwordFilename)
////    val cdf = cleanDataFrame(df, "review", "words", sw)
////    val tdf = transformToVector(cdf, "words", "vectors")
////    val predict = model.transform(tdf)
////    val evaluator = new MulticlassClassificationEvaluator()
////      .setLabelCol("label")
////      .setPredictionCol("prediction")
////      .setMetricName("accuracy")
////    val acc:Double = evaluator.evaluate(predict) * 100
////    println(acc)
////    acc shouldBe a [Double]
////  }
//
//  it should "work for pipeline" in {
//    val df: DataFrame = loadDataset(spark, trainingDataFilename)
//    val sw: List[String] = loadStopWords(stopwordFilename)
//
//    val removeRedundancyUdf = udf(removeRedundancy(_:String))
//
//    val cleanedDf = df.withColumn("words", removeRedundancyUdf(col("review"))).drop("review")
//    val stopWordsRemover = new StopWordsRemover()
//      .setInputCol("words")
//      .setOutputCol("cleanedWords")
//      .setStopWords(sw.toArray)
//
//    val hashingTF = new HashingTF()
//      .setNumFeatures(1048576)
//      .setInputCol("cleanedWords")
//      .setOutputCol("vectors")
//
//    val naiveBayes = new NaiveBayes()
//      .setModelType("multinomial")
//      .setLabelCol("label")
//      .setFeaturesCol("vectors")
//      .setSmoothing(0.01)
//
//    val pipeline = new Pipeline()
//      .setStages(Array(stopWordsRemover, hashingTF, naiveBayes))
//
//    val model = pipeline.fit(cleanedDf)
//
//    val testDf: DataFrame = loadDataset(spark, testingDataFilename)
//    val cleanedTestDf = testDf.withColumn("words", removeRedundancyUdf(col("review"))).drop("review")
//    cleanedTestDf.printSchema()
//    val count = cleanedTestDf.count()
//    val ccount = cleanedTestDf.rdd.filter{
//      case Row(_, words: Seq[String]) => words == null || words.isEmpty
//    }.count()
//
////    val tdf = stopWordsRemover.transform(cleanedTestDf)
////    val vtdf = hashingTF.transform(tdf).select("label", "vectors")
//
//    val predict = model.transform(cleanedTestDf)
//    val evaluator = new MulticlassClassificationEvaluator()
//      .setLabelCol("label")
//      .setPredictionCol("prediction")
//      .setMetricName("accuracy")
//    val acc = evaluator.evaluate(predict) * 100
//    println(acc)
//    acc shouldBe a [java.lang.Double]
//  }
//}
