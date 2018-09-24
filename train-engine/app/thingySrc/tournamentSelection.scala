package com.thingy.selection

import com.thingy.genome.NetworkGenome
import com.thingy.species.SpeciesMember
import scala.util.Random

object TournamentSelection {

	def select(genomes:List[SpeciesMember]): SpeciesMember = {
			selectAux(genomes, 3, null)

	}

	def selectAux(genomes:List[SpeciesMember], k: Int, best: SpeciesMember): SpeciesMember = {
			if(k == 0) {
				best
			} else {
				val newCandidate = genomes(Random.nextInt(genomes.length))
				if(k > 0 && (best == null || newCandidate.fitnessValue > best.fitnessValue)) {
					selectAux(genomes, k -1, newCandidate)
				} else {
					selectAux(genomes, k -1, best)
				}
			}
			
	}	
}