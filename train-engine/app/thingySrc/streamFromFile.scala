package com.thingy.experiencestream

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import java.nio.file.Paths
import scala.concurrent._

class StreamFromFile(implicit as: ActorSystem) {

//implicit val system = ActorSystem("QuickStart")
implicit val materializer = ActorMaterializer()

val file = Paths.get("C:\\Windows\\Temp\\normalisedIrisData.csv")

println(file)
def startStream: Future[IOResult] = FileIO.fromPath(file)
  .map(println)
  .to(Sink.foreach(println))
  .run()

}