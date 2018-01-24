package com.thingy.species


import com.typesafe.config.ConfigFactory
import com.thingy.genome.NetworkGenome.NetworkGenome
import scala.util.Random
import com.thingy.selection.TournamentSelection

case class SpeciesMember (
	genome: NetworkGenome,
	performanceValue: Double)

case class Species(totalDistance: Double = 0.0,
				   memberCount: Int = 0,
				   speciesTotalFitness: Double = 0,
				   averageDistance: Double = 0.0,
				   archetype: NetworkGenome,
				   members: List[SpeciesMember]) {
	
	def add(g: NetworkGenome, performanceValue: Double) = {
		
		val newtotalDist = totalDistance + g.distance(archetype)
		val newMemberCount = memberCount + 1
		val newMembers = SpeciesMember(g, performanceValue) :: members
		val newArchetype = newMembers(Random.nextInt(newMemberCount))
		val updatedTotalFitness = speciesTotalFitness + performanceValue

		this.copy(
			totalDistance = newtotalDist,
			averageDistance = newtotalDist / newMemberCount,
			speciesTotalFitness = updatedTotalFitness,
			archetype = newArchetype.genome,
			members = newMembers, 
			memberCount = newMemberCount)

	}
}

object SpeciesDirectory {
	implicit val config = ConfigFactory.load()
	val speciesExpansionFactor = config.getConfig("thingy").getDouble("species-expansion-factor")
	val speciesStartingThreshold = config.getConfig("thingy").getDouble("species-starting-threshold")
	val populationSize =  config.getConfig("thingy").getDouble("population-size")
}

case class SpeciesDirectory (
	val currentSpeciesId: Int = 0, 
	val species: Map[Int, Species] = Map.empty,
	val totalFitness: Double = 0.0) {

		import SpeciesDirectory._
		
		def reset: SpeciesDirectory = {
			///val updateMembers =  
		
			// GO through each species and clear it's members n rest its counts
		}

		def allocate(f: NetworkGenome, performanceValue: Double): SpeciesDirectory = {
			if (currentSpeciesId == 0) {
				this.copy(totalFitness = totalFitness + performanceValue, 
						  currentSpeciesId = 1, 
						  species = species + (1 -> Species(memberCount = 1, speciesTotalFitness = performanceValue, archetype = f, 
						  								    members = List(SpeciesMember(f, performanceValue)))))
			} else {
				// iterate through the dir listing and add if compatible
				val newSpeciesList = findSpecies(f, performanceValue, species, species)
				this.copy(totalFitness = totalFitness + performanceValue,
						  currentSpeciesId = if(newSpeciesList.size > currentSpeciesId){currentSpeciesId + 1} else {currentSpeciesId},
						  species = newSpeciesList)
			}
		
		}

		def findSpecies(c: NetworkGenome, performanceValue: Double, sList: Map[Int, Species], staticList: Map[Int, Species]): Map[Int, Species] = {

			sList.headOption.map(current => {
				
				val d = c.distance(current._2.archetype)

				if(current._2.memberCount  == 1 && d > speciesStartingThreshold) {
					// this is a compatible species
					staticList + (current._1 -> current._2.add(c, performanceValue))

				} else {

				if(d < (current._2.averageDistance * speciesExpansionFactor)) { 
					// this is a compatible species
					staticList + (current._1 -> current._2.add(c, performanceValue))
				} else {
					// this species is not compatible at all, so go to next
					findSpecies(c, performanceValue, sList.tail, staticList)
				}}
			}).getOrElse(staticList + (currentSpeciesId -> Species(memberCount = 1, speciesTotalFitness = performanceValue, archetype = c, members = List(SpeciesMember(c, performanceValue)))))
		}

		def selectGenerationSurvivors = {
			species.map(s => {

				val speciesCandidates = ((s._2.speciesTotalFitness / totalFitness) * populationSize).toInt
				// elites?
				// 		
				// crossover
				// Here we should pick candidates from the species members x times based on fitness
					1.to(speciesCandidates).map(i => {
							generationFunction(s._2)
				// mutation only
					})
			})
		}

		def generationFunction(s: Species): () => NetworkGenome = {
		val genome1 = TournamentSelection.select(s.members).genome
		val genome2 = TournamentSelection.select(s.members).genome

		val f = () => {
			genome1.crossover(genome2)
		}

		f
	}

}