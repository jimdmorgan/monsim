package org.monsim.impl.util

import org.monsim.api.util.Pricer
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType
import java.lang.RuntimeException

class PricerImpl : Pricer {

    private val gameUtil = GameUtilImpl()
    private val monopolyUtil = MonopolyUtilImpl()

    override fun propertyBuy(property: Property, game: Game): Int {
        val group = gameUtil.groupForProperty(property, game)
        val buyPrices = group.buyPrices
        val schedule = property.schedule
        val index = schedule - 1
        when{
            index < 0 || index >= buyPrices.size -> throw RuntimeException("Bad buyPrice/schedule for Group#${group.id} schedule#${schedule}: ${buyPrices}")
        }
        val buyPrice = buyPrices[index]
        return buyPrice
    }

    override fun houseBuy(property: Property, game: Game): Int {
        val group = gameUtil.groupForProperty(property, game)
        when{
            group.housePrice == null -> throw RuntimeException("Group#${property.groupId} doesn't have a housePrice")
        }
        val houseBuyPrice = group.housePrice!!
        return houseBuyPrice
    }

    override fun houseSell(property: Property, game: Game): Int {
        val buyPrice = houseBuy(property, game)
        val percent = game.rule.houseSellBackPercent
        val sellPrice = (buyPrice * percent) / 100
        return sellPrice
    }

    override fun mortgage(property: Property, game: Game): Int {
        val buyPrice = propertyBuy(property, game)
        val mortgage = (buyPrice * game.rule.mortgagePercent) / 100
        return mortgage
    }

    override fun unmortgage(property: Property, game: Game): Int {
        val buyPrice = propertyBuy(property, game)
        val mortgage = (buyPrice * game.rule.unmortgagePercent) / 100
        return mortgage
    }

    override fun rent(property: Property, game: Game): Int {
        val group = gameUtil.groupForProperty(property, game)
        val rent = when (group.groupType){
            GroupType.DICE -> diceRent(property, game)
            GroupType.FIXED -> fixedRent(property, game)
            GroupType.DEVELOPABLE -> developableRent(property, game)
        }
        return rent
    }

    private fun fixedRent(property: Property, game: Game): Int {
        val rentBase = rentBaseByPropertiesOwned(property, game)
        return rentBase
    }

    private fun diceRent(property: Property, game: Game): Int {
        val rentBase = rentBaseByPropertiesOwned(property, game)
        val dice = game.turn.rollSum
        val rent = dice * rentBase
        return rent
    }


    private fun developableRent(property: Property, game: Game): Int {
        val houses = property.houses!!
        val rentBase = rentBaseByHouses(property, game)
        val group = gameUtil.groupForProperty(property, game)
        val isMonopoly = monopolyUtil.isGroupMonopoly(group, game)
        val rent = if (houses == 0 && isMonopoly) ((rentBase * game.rule.monopolyWithoutHousesRentPercent) / 100) else rentBase
        return rent
    }

    private fun rentBaseByHouses(property: Property, game: Game): Int{
        val rents = rents(property, game)
        val houses = property.houses!!
        val rentBase = rents[houses]
        return rentBase;
    }

    private fun rentBaseByPropertiesOwned(property: Property, game: Game): Int{
        val propertiesOwned = totalPropertiesOwnedInGroup(property, game)
        val rents = rents(property, game)
        when{
            propertiesOwned == 0 -> throw RuntimeException("Player#${property.ownerId} doesn't own any properties in group#${property.groupId}. Can't get rent")
            propertiesOwned > rents.size -> throw RuntimeException("Bad rents for property#${property.id} #Owned:${propertiesOwned} size:${rents.size}: $rents")
        }
        val rentBase = rents[propertiesOwned - 1]
        return rentBase
    }

    private fun totalPropertiesOwnedInGroup(property: Property, game: Game): Int{
        val playerId = property.ownerId
        val groupId = property.groupId
        val owned = game.properties.filter{it.groupId == groupId}.filter{it.ownerId == playerId}.count()
        return owned
    }

    private fun rents(property: Property, game: Game): List<Int>{
        val group = gameUtil.groupForProperty(property, game)
        val schedule = property.schedule
        return group.rents[schedule - 1]
    }
}