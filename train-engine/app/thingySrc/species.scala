package com.thingy.species


import com.typesafe.config.ConfigFactory
import com.thingy.genome.NetworkGenome
import scala.util.Random
import com.thingy.selection.TournamentSelection
import play.api.libs.json._
import com.thingy.config.ConfigDataClass.ConfigData


case class SpeciesMember (
	genome: NetworkGenome,
	performanceValue: Double,
	fitnessValue: Double)

object Species {
	implicit val speciesWrites: Writes[Species] = new Writes[Species] {
    def writes(species: Species): JsValue = Json.obj(
    	"speciesTotalFitness" -> species.speciesTotalFitness,
    	"memberCount" -> species.memberCount,
    	"members" -> species.members.map(s=> Json.toJson(s.genome))
    	
        //bar.key -> Json.obj("value" -> bar.value)
    )
}
}

case class Species(id: Int = 0,
					totalDistance: Double = 0.0,
				   memberCount: Int = 0,
				   speciesTotalFitness: Double = 0,
				   averageDistance: Double = 0.0,
				   archetype: NetworkGenome,
				   archetypePF: Double = 0.0,
				   members: List[SpeciesMember]) {
	
	def reset = {
		this.copy(memberCount = 0, speciesTotalFitness = 0, averageDistance = 0, members = List.empty)
	}

	def add(g: NetworkGenome, performanceValue: Double, fitnessValue: Double) = {
		
		val newtotalDist = totalDistance + g.distance(archetype)
		val newMemberCount = memberCount + 1
		val newMembers = SpeciesMember(g, performanceValue, fitnessValue) :: members
		val (newArchetype, newArchetypePF) = if(performanceValue> archetypePF) (g,performanceValue) else (archetype, archetypePF)

		val updatedTotalFitness = speciesTotalFitness + performanceValue

		this.copy(
			totalDistance = newtotalDist,
			averageDistance = newtotalDist / newMemberCount,
			speciesTotalFitness = updatedTotalFitness,
			archetype = newArchetype,
			archetypePF = newArchetypePF,
			members = newMembers, 
			memberCount = newMemberCount)

	}
}

object SpeciesDirectory {
	implicit val config = ConfigFactory.load()
	
	val speciesStartingThreshold = config.getConfig("thingy").getDouble("species-starting-threshold")
	
	implicit val speciesDirWrites: Writes[SpeciesDirectory] = new Writes[SpeciesDirectory] {
    def writes(speciesDir: SpeciesDirectory): JsValue = Json.obj(
    	"species" -> speciesDir.species.map(s=> Json.toJson(s._2)),
    	"totalFitness" -> speciesDir.totalFitness
        //bar.key -> Json.obj("value" -> bar.value)
    )

    def describe = {
    	
    }
}

}

case class SpeciesDirectory (
	val configData: ConfigData = ConfigData(),
	val currentSpeciesId: Int = 0, 
	val species: Map[Int, Species] = Map.empty,
	val totalFitness: Double = 0.0,
	val bestMember: SpeciesMember = null) {

		import SpeciesDirectory._
		
		def reset: SpeciesDirectory = {

			// GO through each species and clear it's members n rest its counts

			val updateSpecies = species.foldLeft(Map[Int, Species]()) {(acc, current) =>
				acc + (current._1 -> current._2.reset)
			}

			this.copy(species = updateSpecies, totalFitness = 0)
			
		}

		def allocate(f: NetworkGenome, performanceValue: Double, fitnessValue: Double): (Int, SpeciesDirectory) = {
			if (currentSpeciesId == 0) {

				// Then no species have been created yet. 
				// Create one at location 1.
				
				(currentSpeciesId, this.copy(totalFitness = totalFitness + performanceValue, 
						  currentSpeciesId = 1, 
						  species = species + (1 -> Species(
						  								id = 1,
						  								memberCount = 1, 
						  								speciesTotalFitness = performanceValue, 
						  								archetype = f, 
						  								archetypePF = performanceValue,
						  								members = List(SpeciesMember(f, performanceValue, fitnessValue)))),
						  bestMember = SpeciesMember(f, performanceValue, fitnessValue)))
			} else {

				// iterate through the dir listing and add if compatible
				

				val (allocatedSpecies, newSpeciesList) = findSpecies(f, performanceValue, fitnessValue, species, species)
				val best = if(bestMember == null || performanceValue > bestMember.performanceValue) SpeciesMember(f, performanceValue, fitnessValue) else bestMember
				// Increase the species Id. and create new list.

				(allocatedSpecies, this.copy(totalFitness = totalFitness + performanceValue,
						  currentSpeciesId = if(newSpeciesList.size > currentSpeciesId){currentSpeciesId + 1} else {currentSpeciesId},
						  species = newSpeciesList,
						  bestMember = best))
			}
		
		}

		def findSpecies(c: NetworkGenome, performanceValue: Double, fitnessValue: Double, sList: Map[Int, Species], staticList: Map[Int, Species]): (Int, Map[Int, Species]) = {

			// probably worth checking if still compatible with exisiting species.
			if(c.species != 0 && c.distance(sList(c.species).archetype) < speciesStartingThreshold) {
				// then 
			    val species = sList(c.species)
			    val updatedspec = species.add(c, performanceValue, fitnessValue)
			    
			    (c.species, sList + (c.species -> updatedspec))
			    //else {}
			 } else {
			
				sList.headOption.map(current => {
				
					val d = c.distance(current._2.archetype)

					// if(d > (current._2.averageDistance * speciesExpansionFactor)) { 
					if(d < speciesStartingThreshold) {

						// this is a compatible species

						//println("Species match rule 2")

						(current._1, staticList + (current._1 -> current._2.add(c, performanceValue, fitnessValue)))
					} else {

						//println("check next species")
						// this species is not compatible at all, so go to next
						findSpecies(c, performanceValue, fitnessValue, sList.tail, staticList)
					}
				}).getOrElse({
					//println("No Matched Species Create new")
					(currentSpeciesId + 1, staticList + (currentSpeciesId + 1 -> Species(id = currentSpeciesId + 1,  memberCount = 1, speciesTotalFitness = performanceValue, archetype = c, archetypePF=  performanceValue,  members = List(SpeciesMember(c, performanceValue, fitnessValue))))
				)})
			}
		}

		def selectGenerationSurvivors = {
			val populationSize =  configData.populationSize


			species.map(s => {

				val speciesCandidates = ((s._2.speciesTotalFitness / totalFitness) * populationSize).toInt
				// elites?
				// 		
				// crossover
				// Here we should pick candidates from the species members x times based on fitness
					1.to(speciesCandidates).map(i => {
							
							if(Random.nextDouble < config.getConfig("thingy").getDouble("crossover-rate")){
							// crossover
							generationFunction(s._2)
							} else {
							// mutation only
							() => TournamentSelection.select(s._2.members).genome
							}
					})
			})
		}

		def generationFunction(s: Species): () => NetworkGenome = {
		val genome1 = TournamentSelection.select(s.members)
		val genome2 = TournamentSelection.select(s.members)

		val orderedGenome = (if(genome1.performanceValue > genome2.performanceValue) genome1 else genome2, if(genome1.performanceValue > genome2.performanceValue) genome2 else genome1)
		
		val f = () => {
			orderedGenome._1.genome.crossover(orderedGenome._2.genome)
		}

		f
	}

}