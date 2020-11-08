package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.PropertyBuyer
import org.monsim.bean.Bill
import org.monsim.bean.Constants
import org.monsim.bean.domain.Game
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.PricerImpl
import org.monsim.impl.accounting.CashAccounterImpl
import org.monsim.impl.accounting.PropertyAccounterImpl
import java.lang.RuntimeException

class PropertyBuyerImpl : PropertyBuyer {

    private val gameUtil = GameUtilImpl()
    private val pricer = PricerImpl()
    private val cashTransferer = CashAccounterImpl()
    private val propertyAccounter = PropertyAccounterImpl()
    private val log = KotlinLogging.logger {}


    override fun buyProperty(game: Game) {
        val reasonWhyCantBuyProperty = reasonWhyCantBuyProperty(game)
        if (reasonWhyCantBuyProperty != null){
            throw RuntimeException("Can't buy property: $reasonWhyCantBuyProperty")
        }
        val player = gameUtil.currentPlayer(game)
        val property = gameUtil.currentProperty(game)!!
        val space = gameUtil.currentSpace(game)
        log.debug{"player#${player.id} is buying property#${property.id} ('${space.name}')"}
        val price = pricer.propertyBuy(property, game)
        cashTransferer.applyBill(Bill(debtor = player, creditor = Constants.BANK, amount = price), game)
        propertyAccounter.transferProperty(property, player, game)
    }

    override fun mayBuyProperty(game: Game): Boolean {
        return reasonWhyCantBuyProperty(game) == null
    }

    override fun propertyForSale(game: Game): Boolean {
        return reasonPropertyNotForSale(game) == null
    }

    fun reasonWhyCantBuyProperty(game: Game): String?{
        val reasonPropertyNotForSale = reasonPropertyNotForSale(game)
        if (reasonPropertyNotForSale != null){
            return reasonPropertyNotForSale
        }
        val player = gameUtil.currentPlayer(game)
        val property = gameUtil.currentProperty(game)!!
        val price = pricer.propertyBuy(property, game)
        if (price > player.cash){
            return "player#${player.id} doesn't have enough cash to buy property#${property.id} cash=${player.cash} cost=${price}"
        }
        return null
    }

    fun reasonPropertyNotForSale(game: Game): String?{
        if (game.rule.isBuyingPropertiesDisabled){
            return "rule doesn't allowed properties to be bought"
        }
        val player = gameUtil.currentPlayer(game)
        val property = gameUtil.currentProperty(game)
        if (property == null){
            return "not a property"
        }
        if (property.ownerId != null){
            return "Property#${property.id} is already owned by player#${property.ownerId}. Player#${player.id} can't buy"
        }
        return null
    }
}