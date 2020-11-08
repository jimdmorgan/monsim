package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property

interface HouseBuilder {


    fun changeHouse(property: Property, isAdding: Boolean, game: Game)
    fun isChangeHouseLegal(property: Property, isAdding: Boolean, game: Game): Boolean

}