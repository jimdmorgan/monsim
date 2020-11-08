package org.monsim.api.accounting

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property

interface HouseAccounter {

    fun transferHouse(property: Property, isAdded: Boolean, game: Game)

    fun transferHouseError(property: Property, isAdded: Boolean, game: Game):String?

}