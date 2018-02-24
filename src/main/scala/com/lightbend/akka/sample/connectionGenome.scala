package com.thingy.genome

import com.thingy.weight.Weight

object ConnectionGenome {

}

case class ConnectionGenome(
	id: Int, 
	from: Int, 
	to: Int, 
	weight: Weight, 
	enabled: Boolean = true, 
	recurrent: Boolean = false
)
