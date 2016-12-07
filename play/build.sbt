name := "play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

//libraryDependencies ++= Seq("postgresql" % "postgresql" % "9.1-901.jdbc4")
// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212.jre7"



unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

// https://mvnrepository.com/artifact/com.typesafe.play/play-slick_2.11
libraryDependencies += "com.typesafe.play" % "play-slick_2.11" % "2.0.0"
