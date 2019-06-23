
package com.thingy.population

import com.thingy.genome.NetworkGenome

trait PopulationCandidate {
	val genomeFunction: () => NetworkGenome
}

case class ElitePopulationCandidate(f: () => NetworkGenome) extends PopulationCandidate {
	val genomeFunction = f
}

case class NormalPopulationCandidate(f: () => NetworkGenome) extends PopulationCandidate {
	val genomeFunction = f
}