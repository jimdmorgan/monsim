package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Repairer
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl
import java.lang.RuntimeException

class RepairerImpl : Repairer {

    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val gameUtil = GameUtilImpl()

    override fun chargeForHouseRepair(repairCostPerHouse: Int, game: Game) {
        val player = gameUtil.currentPlayer(game)
        when {
            repairCostPerHouse < 0 -> throw RuntimeException("RepairCost is negative: $repairCostPerHouse")
        }
        val houses = numberOfHouses(player, game)
        val cost = houses * repairCostPerHouse
        log.debug{"Player#${player.id} is paying $cost to repair $houses house(s) at $repairCostPerHouse cost per house"}
        cashTransferer.applyBill(Bill(debtor = player, amount = cost), game)
    }

    private fun numberOfHouses(player: Player, game: Game): Int {
        val houses = game.properties.filter{it.ownerId == player.id}.filter{it.houses != null}.map{it.houses!!}.sum()
        return houses
    }


}