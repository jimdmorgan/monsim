package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property

interface PropertyBuyer {

    fun buyProperty(game: Game)

    fun propertyForSale(game: Game): Boolean
    fun mayBuyProperty(game: Game): Boolean

}