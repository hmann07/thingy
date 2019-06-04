package com.thingy.config

import com.typesafe.config.ConfigFactory
import reactivemongo.bson.{
  BSONWriter, BSONDocument, BSONDouble, BSONDocumentWriter, BSONDocumentReader, Macros, document
}

 /*
 #seed-network = "/thingy/app/thingySrc/resources/seed.json"
 // seed-network = "C:\\Users\\mau\\Documents\\thingy\\train-engine\\app\\thingySrc\\resources\\seed.json"
 // #seed-subnet = "/thingy/app/thingySrc/resources/subnet-seed.json"
  seed-subnet = "C:\\Users\\mau\\Documents\\thingy\\train-engine\\app\\thingySrc\\resources\\seed.json"
  weight-range = 8
  population-size = 150
  generations = 200
  species-starting-threshold = 2
  species-distance-weight-factor = 0.3
  crossover-rate = 0.75
  mutation-rate = 0.95
  weight-mutation-likelihood = 0.85 
  jiggle-over-reset=.90
*/

object ConfigDataClass {
private val config = ConfigFactory.load()


case class RuntimeConfig(
  mongoStorage: Boolean = true
)


case class ConfigData(
  environmentId: String = "",
  populationSize: Int = config.getConfig("thingy").getInt("population-size"), 
  maxGenerations: Int = config.getConfig("thingy").getInt("generations"),
  connectionWeightRange: Int = config.getConfig("thingy").getInt("weight-range"),
  speciesMembershipThreshold: Double = config.getConfig("thingy").getDouble("species-starting-threshold"),
  crossoverRate: Double = config.getConfig("thingy").getDouble("crossover-rate"),
  globalMutationRate: Double = config.getConfig("thingy").getDouble("mutation-rate"),
  weightMutationRate: Double = config.getConfig("thingy").getDouble("weight-mutation-likelihood"),
  weightJiggleOverReset: Double = config.getConfig("thingy").getDouble("jiggle-over-reset"),
  addNetworkNode: Double = 0.025,
  addNetworkConnection: Double = 0.025,
  mutateWeights: Double = 0.95,
  compatWeightCoeff: Double = 0.4,
  compatDisjointCoeff: Double = 1
  )

  implicit val configWriter: BSONDocumentWriter[ConfigData] = Macros.writer[ConfigData]

  

}

