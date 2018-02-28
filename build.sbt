name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.11.8"

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

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.4" % "test"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.13.0"
