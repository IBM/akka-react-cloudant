import sbt.Keys._

name := "akka-epl"

version := "1.0"

scalaVersion := "2.12.2"

// set the main class for packaging the main jar
mainClass in assembly := Some("com.epl.akka.SoccerMainController")

assemblyJarName in assembly  := "akka-epl.jar"

val akkaVersion = "2.5.4"
val akkaHttpVersion = "10.0.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

  "com.typesafe.akka" % "akka-slf4j_2.12" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.jsoup" % "jsoup" % "1.10.3",
  "com.ning" % "async-http-client" % "1.9.40",
  "com.typesafe.play" % "play-json_2.12" % "2.6.3",
  "com.cloudant" % "cloudant-client" % "2.9.0",

  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.1",
  "org.seleniumhq.selenium" % "selenium-java" % "3.5.3"

)


//TEST dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
)




