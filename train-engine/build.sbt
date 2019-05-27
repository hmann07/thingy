name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.11.8"


lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

libraryDependencies += guice

libraryDependencies += ehcache

libraryDependencies += "org.aspectj" % "aspectjweaver" % "1.8.9"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.5"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.5" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.4.11"

libraryDependencies += "io.kamon" %% "kamon-core" % "0.6.3"

libraryDependencies += "io.kamon" %% "kamon-autoweave" % "0.6.3"

libraryDependencies += "io.kamon" %% "kamon-akka" % "0.6.3"

libraryDependencies += "io.kamon" %% "kamon-log-reporter" % "0.6.3"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.16"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % "test"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies += "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.13.0-play26"

libraryDependencies += "org.webjars" %% "webjars-play" % "2.6.3"

libraryDependencies += "org.webjars.npm" % "js-tokens" % "4.0.0"

libraryDependencies += 	"org.webjars.npm" % "react" % "16.3.1"

libraryDependencies += 	"org.webjars.npm" % "react-dom" % "16.3.2"

libraryDependencies += "org.webjars" % "bootstrap" % "4.0.0-2"

libraryDependencies += "org.webjars.npm" % "crossfilter2" % "1.3.14"
