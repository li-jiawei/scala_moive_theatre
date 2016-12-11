package sentiment

import org.apache.spark.ml.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import sentiment.DataPreprocessor._

/**
  * Created by Jiawei on 11/17/16.
  */


object NaiveBayesDFAnalyzer {
  def main(args: Array[String]) = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Movie-Sentiment")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //    spark.sparkContext.setLogLevel("WARN")

    val stopWordsList = spark.sparkContext.broadcast(loadStopWords(stopwordFilename))
    val dataset = loadAndFormatData(spark.sparkContext, sar14File)

    val cleanedData = cleanAndTransformDF(dataset, cleanDataset, stopWordsList.value)
    val Array(trainingDF, testingDF) = cleanedData.randomSplit(Array(0.9, 0.1), seed = 666)

    val model = new NaiveBayes()
        .setLabelCol("label")
        .setFeaturesCol("vector")
        .setPredictionCol("prediction")
        .setSmoothing(1)
        .fit(trainingDF)

    val predictions = model.transform(testingDF)
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    val accuracy = 100.0 * evaluator.evaluate(predictions)
    println("Test set accuracy = " + accuracy)
    model.write.overwrite().save(naiveBayesModelSavePath)
  }
}
