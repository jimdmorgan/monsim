package org.monsim.impl.util

import mu.KotlinLogging
import org.monsim.api.util.NetWorthCalculator
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player

class NetWorthCalculatorImpl : NetWorthCalculator {

    private val gameUtil = GameUtilImpl()
    private val log = KotlinLogging.logger {}
    private val pricer = PricerImpl()

    override fun netWorth(player: Player, game: Game): Int {
        val total = player.cash + propertiesValue(player, game) + housesValue(player, game)
        return total
    }

    fun propertiesValue(player: Player, game: Game): Int{
        val properties = gameUtil.playersProperties(player, game).filter{!it.mortgaged}.toSet()
        val total = properties.map{pricer.mortgage(it, game)}.sum()
        return total
    }

    fun housesValue(player: Player, game: Game): Int{
        val properties = gameUtil.playersProperties(player, game).filter{it.houses != null && it.houses!! > 0}.toSet()
        val total = properties.map{pricer.houseSell(it, game) * it.houses!!}.sum()
        return total
    }
}