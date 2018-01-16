package com.thingy.selection

import com.thingy.genome.NetworkGenome
import com.thingy.species.SpeciesMember
import scala.util.Random
object RouletteWheel {

  def select(genomes: List[SpeciesMember], totalFitnessValue: Double): SpeciesMember = {

    val t = totalFitnessValue * Random.nextDouble
    selectAux(genomes, t, 0)

  }

  def selectAux(genomes: List[SpeciesMember], targetFitnessValue: Double, cumulativeError: Double): SpeciesMember = {
    try {
      val t = genomes.head.performanceValue
    } catch {
      case e: Exception => println(cumulativeError + ", " + targetFitnessValue)
    }

    if (targetFitnessValue <= cumulativeError + genomes.head.performanceValue) {
      genomes.head
    } else {
      selectAux(genomes.tail, targetFitnessValue, cumulativeError + genomes.head.performanceValue)
    }
  }
}