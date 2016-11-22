name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

// https://mvnrepository.com/artifact/org.springframework/spring-web
libraryDependencies += "org.springframework" % "spring-web" % "3.1.1.RELEASE"

// https://mvnrepository.com/artifact/org.springframework/spring-webmvc
libraryDependencies += "org.springframework" % "spring-webmvc" % "4.0.0.RELEASE"




fork in run := true