package actor.sentiment

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import message.{Sentiment, SparkStreaming}
import org.apache.spark.mllib.classification.NaiveBayesModel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import sentiment.DataPreprocessor._
import sentiment.TwitterStreaming._


/**
  * Created by Jiawei on 12/1/16.
  */
class StreamingActor extends Actor{

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

//    case StartSparkStreaming(conf) => {
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
//        val originalSender = sender()
        val sentimentActor = context.actorSelection("akka://application/user/adminActor/sentimentActor")

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
//                originalSender ! Sentiment(score)
                sentimentActor ! Sentiment(score, count)
            }
        }

        ssc.start()
      } else {
        stop()
      }


//      val fw = new FileWriter(new File("streamingdata.txt"))
//
//      tweets.foreachRDD{
//        rdd => rdd.foreach{
//          case (_, score) =>
//            fw.write(score + "\n")
//            fw.flush
//        }
//      }
//      fw.flush()

//        .map { tweet => cleanDataset(tweet.getText, stopWordList) }
//        .filter(words => words.length > 1)
//        .map { words =>
//          val hashingTF = new org.apache.spark.mllib.feature.HashingTF()
//          val vectors = hashingTF.transform(words)
//          model.predict(vectors)
//        }
//      tweets.print()
//      tweets.foreachRDD{
//        rdd =>
//          sentimentActor = context.actorOf(Props[StreamingActor], name = "streamingActor")
//      }
      //        .map(tweet => model.predict(tweet))
      //      prediction.print()
    }
  }

  override def postStop() {
//    ssc.stop()
  }

}
case object StreamingActor {
  def props = Props(classOf[StreamingActor])

}