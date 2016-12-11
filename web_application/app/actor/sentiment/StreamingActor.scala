package actor.sentiment

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import message.{Sentiment, SparkStreaming}
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import sentiment.DataPreprocessor._
import sentiment.TwitterStreaming._


class StreamingActor extends Actor{
      //    Attempt to initialize spark context in preStart, but something goes wrong, so put then in receive method
//    var ssc: StreamingContext = _
//    var stopWordList: List[String] = _
//    var model: NaiveBayesModel = _

  override def preStart(): Unit = {
    super.preStart()
//    ssc = StreamingContext.getActiveOrCreate(createStreamingContext)
//    stopWordList = loadStopWords(stopwordFilename)
//    model = NaiveBayesModel.load(ssc.sparkContext, naiveBayesModelSavePath)

//    stopWordList = loadStopWords(stopwordFilename)
  }

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(loggingEnabled = true) {
      case e:Exception => Stop
    }

  override def receive = {

    case SparkStreaming(sign, conf) => {
      if (sign) {
        for {
          ck <- conf.getString("twitter.consumerKey")
          cs <- conf.getString("twitter.consumerSecret")
          at <- conf.getString("twitter.accessToken")
          ats <- conf.getString("twitter.accessTokenSecret")
        } {
          buildTwitterOAuth(ck, cs, at, ats)
        }
        val ssc = StreamingContext.getActiveOrCreate(createStreamingContext)
        val stopWordList = loadStopWords(stopwordFilename)
        val model = NaiveBayesModel.load(ssc.sparkContext, naiveBayesModelSavePath)
        println(model.toString)
        val sentimentActor = context.actorSelection("akka://application/user/adminActor/sentimentActor")

        // We use the key word "trump" as our search condition for testing purpose
        // becasuse everyone talks about trump, so we can fetch a huge amount of data in real time for testing our appliction.
        val tweets = TwitterUtils.createStream(ssc, None, Array("trump"))
          .filter(_.getLang == "en")
          //        .map(tweet => tweet.getId + "------" + tweet.getText)
          .map(tweet => cleanAndTransformToVector(tweet.getText, cleanDataset, stopWordList))
          .cache()

        val predictAndCount = tweets
          .map { vectors =>
            (model.predict(vectors), 1)
          }.reduceByKey( (a, b) => a + b )

        predictAndCount.foreachRDD {
          rdd =>
            rdd.collect().foreach {
              case (score, count) =>
                sentimentActor ! Sentiment(score, count)
            }
        }
        ssc.start()
      } else {
        stop()
      }

    }
  }
}
case object StreamingActor {
  def props = Props(classOf[StreamingActor])
}