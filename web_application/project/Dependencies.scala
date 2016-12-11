import sbt._

object Version {
  val scala            = "2.11.8"
  val spark            = "1.6.0"
  val twitter          = "1.6.0"
  val postgreSQL       = "9.4.1211.jre7"
  val akka             = "2.4.10"
  val playSlick        = "2.0.0"
  val slick            = "3.1.1.2"
}

object Library {
  val sparkSQL          = "org.apache.spark"            %% "spark-sql"                % Version.spark
  val sparkMLLib        = "org.apache.spark"            %% "spark-mllib"              % Version.spark
  val sparkCore         = "org.apache.spark"            %% "spark-core"               % Version.spark
  val postgreSQL        = "org.postgresql"              %  "postgresql"               % Version.postgreSQL
  val sparkStreaming    = "org.apache.spark"            %% "spark-streaming"          % Version.spark
  val twitter           = "org.apache.spark"            %% "spark-streaming-twitter"  % Version.twitter
  val akka              = "com.typesafe.akka"           %% "akka-actor"               % Version.akka
  val akkaSlf4j         = "com.typesafe.akka"           %% "akka-slf4j"               % Version.akka
  val playSlick         = "com.typesafe.play"           %% "play-slick"               % Version.playSlick
  val slick             = "com.typesafe.slick"          %% "slick"                    % Version.playSlick
  val slickHikaricp     = "com.typesafe.slick"          %% "slick-hikaricp"           % Version.slick
}

object Dependencies {
  import Library._

  val dependencies = Seq(
    sparkSQL,
    sparkMLLib,
    sparkCore,
    sparkStreaming,
    twitter,
    playSlick,
//    slick,
//    slickHikaricp,
    postgreSQL,
    akka,
    akkaSlf4j
  )
}


