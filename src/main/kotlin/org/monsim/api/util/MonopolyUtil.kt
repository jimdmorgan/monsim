package org.monsim.api.util

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group

interface MonopolyUtil {

    fun isGroupMonopoly(group: Group, game: Game):Boolean
    fun isGroupWithHouses(group: Group, game: Game):Boolean


}