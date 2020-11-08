package org.monsim.api.util

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property

interface Pricer {

    fun propertyBuy(property: Property, game: Game): Int
    fun houseBuy(property: Property, game: Game): Int
    fun houseSell(property: Property, game: Game): Int
    fun mortgage(property: Property, game: Game): Int
    fun unmortgage(property: Property, game: Game): Int
    fun rent(property: Property, game: Game): Int
}