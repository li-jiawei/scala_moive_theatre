package sentiment

/**
  * Created by Jiawei on 11/3/16.
  */

import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import play.api.Play
import play.api.libs.oauth.OAuth
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object TwitterStreaming {
  var ssc: StreamingContext = _

  def createStreamingContext(): StreamingContext = {
    val master: String = "local[*]"
    val appName: String = "twitter-stream"
    val interval: Long = 2

    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)
      .set("spark.streaming.unpersist", "true")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    ssc = new StreamingContext(sc, Seconds(interval))
    ssc
  }

  def buildTwitterOAuth(consumerKey: String, consumerSecret: String, accessToken: String, accessTokenSecret: String): Unit = {
    System.setProperty("twitter4j.debug", "true")
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
  }

  def stop(): Unit = {
    ssc.stop()
  }

//  val tweets = TwitterUtils.createStream(ssc, None, filters)
//  val tweetsFilteredByLang = tweets.filter(_.getLang == "en")
//  val tweetText = tweetsFilteredByLang.map(tweet => tweet.getId + "------" + tweet.getText)
//  tweetText.print()
////  val tweet
//
//
//
//  ssc.start()
//  ssc.awaitTermination()
}
