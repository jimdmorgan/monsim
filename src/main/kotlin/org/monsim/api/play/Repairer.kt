package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Tax

interface Repairer {

    fun chargeForHouseRepair(repairCostPerHouse: Int, game: Game)

}