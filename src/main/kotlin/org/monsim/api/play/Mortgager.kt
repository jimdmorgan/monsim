package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property

interface Mortgager {

    fun mortgage(property: Property, game: Game)
    fun unmortgage(property: Property, game: Game)

    fun mayMortgage(property: Property, game: Game): Boolean
    fun mayUnmortgage(property: Property, game: Game): Boolean
}