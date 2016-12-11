package sentiment

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import org.apache.spark.sql._
import scala.collection.convert.wrapAll._
import scala.collection.mutable.ListBuffer
import scala.math.round
import sentiment.DataPreprocessor._

/**
  * Created by Jiawei on 11/23/16.
  */
object CoreNlpAnalyzer {

  def main(args: Array[String]) {
    val session = SparkSession
      .builder()
      .master("local[*]")
      .appName("CoreNLP")
      .getOrCreate()

    val dataset = loadAndFormatData(session.sparkContext, sar14File)
    val Array(_, testingDF) = dataset.randomSplit(Array(0.9, 0.1), seed = 666)
    testingDF.persist()
    val stopWordsList = loadStopWords(stopwordFilename)
    val cleanedDF = testingDF.rdd.map{
      case Row(label: Double, review: String) =>
        (label, cleanDataset(review, stopWordsList).mkString(" "))
    }.persist()
    val predictAndLabel = cleanedDF.map{
      case (label: Double, review: String) =>
        (label, extractSentiment(review))
    }
    predictAndLabel.persist()

    val accurary = 100.0 * predictAndLabel.filter(tuple => tuple._1 == tuple._2).count() / predictAndLabel.count()
    println(f"----------The accuracy with CoreNLP is $accurary%.2f%%-----------")
  }


  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)


  def extractSentiment(text: String): Int = {
    val (_, sentiment) = extractSentiments(text)
      .maxBy { case (sentence, _) => sentence.length }
    sentiment
  }

  def extractSentiments(text: String): List[(String, Int)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences.map{
        sentence =>
          val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
          (sentence.toString, RNNCoreAnnotations.getPredictedClass(tree))
      }.toList
  }

  def computeWeightedSentiment(tweet: String): Int = {

    val annotation = pipeline.process(tweet)
    val sentiments: ListBuffer[Double] = ListBuffer()
    val sizes: ListBuffer[Int] = ListBuffer()

    for (sentence <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation])) {
      val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
      val sentiment = RNNCoreAnnotations.getPredictedClass(tree)

      sentiments += sentiment.toDouble
      sizes += sentence.toString.length
    }

    val weightedSentiment = if (sentiments.isEmpty) {
      -1
    } else {
      val weightedSentiments = (sentiments, sizes).zipped.map((sentiment, size) => sentiment * size)
      weightedSentiments.sum / sizes.sum
    }
    round(weightedSentiment).toInt
  }

}
