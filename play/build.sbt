name := "play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers ++= Seq(Resolver.bintrayRepo("kwark", "maven"))


libraryDependencies ++= Dependencies.dependencies

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.4"
)