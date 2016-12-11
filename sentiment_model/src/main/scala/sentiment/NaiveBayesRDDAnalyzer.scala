package sentiment

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql._


import sentiment.DataPreprocessor._


/**
  * Created by Jiawei on 11/17/16.
  */


object NaiveBayesRDDAnalyzer {

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Movie-Sentiment")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //    spark.sparkContext.setLogLevel("WARN")

    val stopWordsList = spark.sparkContext.broadcast(loadStopWords(stopwordFilename))

    val dataset = loadAndFormatData(spark.sparkContext, sar14File)

    val cleanedData = cleanAndTransformRDD(dataset, cleanDataset, stopWordsList.value)
    val Array(trainingRDD, testingRDD) = cleanedData.randomSplit(Array(0.9, 0.1), seed = 666)


    //val sameModel = NaiveBayesModel.load(spark.sparkContext, naiveBayesModelSavePath)

//    val range = 0.0 to 10.0 by 0.1
//    val results = range.map {
//      i =>
//        val model = NaiveBayes.train(trainingRDD, lambda = i, modelType = "multinomial")
//        val predictionAndLabel = testingRDD.map {
//          case LabeledPoint(label, vector) =>
//            val prediction = model.predict(vector)
//            (prediction, label)
//        }
//        val accuracy = 100.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / testingRDD.count()
//        (i, accuracy)
//    }

//    results.foreach(result => println(s"lambda: ${result._1}, accuracy: ${result._2}"))
//    val soothing = results.maxBy(_._2)._1 * 0.1
    val soothing = 1
    val model = NaiveBayes.train(trainingRDD, lambda = soothing, modelType = "multinomial")
    val predictionAndLabel = testingRDD.map {
      case LabeledPoint(label, vector) =>
        val prediction = model.predict(vector)
        (prediction, label)
    }
    val accuracy = 100.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / testingRDD.count()
    println(s"***********Accuracy: $accuracy**************")
    model.save(spark.sparkContext, naiveBayesModelSavePath)
  }
}
