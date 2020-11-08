package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Resigner
import org.monsim.api.util.GameUtil
import org.monsim.bean.Bill
import org.monsim.bean.Constants
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.type.GroupType
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl
import org.monsim.impl.accounting.FreeBailCardAccounterImpl
import org.monsim.impl.accounting.PropertyAccounterImpl
import org.monsim.impl.util.StringUtilImpl

class ResignerImpl() : Resigner {

    private val log = KotlinLogging.logger {}
    private val gameUtil: GameUtil = GameUtilImpl()
    private val propertyTransferer = PropertyAccounterImpl()
    private val freeBailCardAccounter = FreeBailCardAccounterImpl()
    private val cashTransferer = CashAccounterImpl()
    private val houseBuilder = HouseBuilderImpl()
    private val monopolyUtil = MonopolyUtilImpl()
    private val stringUtil = StringUtilImpl()


    override fun resign(player: Player, game: Game) {
        log.debug{"player#${player.id} is resigning.  All houses will be sold back to bank.  All properties and cash will be given to creditor."}
        turnOverValuablesToCreditor(player, game)
        player.lost = true
        player.cash = 0
        player.creditorId = Constants.BANK_ID
        player.rollsInJail = 0
        player.jailed = false
    }

    private fun turnOverValuablesToCreditor(player: Player, game: Game) {
        val debtor = player
        val creditor = gameUtil.playerForId(debtor.creditorId, game)
        val bill = Bill(debtor = debtor, creditor = creditor, amount = -1)
        sellPlayersHouses(player, game)
        turnOverPropertiesToCreditor(bill, game)
        turnOverCashToCreditor(bill, game)
        turnOverFreeBailCardsToCreditor(bill, game)
    }

    private fun sellPlayersHouses(player: Player, game: Game) {
        log.debug{"Selling all of player#${player.id}'s houses back to bank (if any)"}
        val properties = gameUtil.playersProperties(player, game)
        val groups = properties.map{it.groupId}.map{gameUtil.groupForId(it, game)}.filter{it.groupType == GroupType.DEVELOPABLE}.toSet()
        groups.forEach{group ->
            val propertiesInGroup = properties.filter{it.groupId == group.id}.toSet()
            while (monopolyUtil.isGroupWithHouses(group, game)){
                val mostDevelopedProperty = propertiesInGroup.maxByOrNull {it.houses!!}!!
                houseBuilder.changeHouse(mostDevelopedProperty, false, game)
            }
        }
    }

    private fun turnOverPropertiesToCreditor(bill: Bill, game: Game) {
        val debtor = bill.debtor!!
        val creditor = bill.creditor
        log.debug{"Turning over all of player#${debtor.id}'s properties to ${stringUtil.player(creditor)}"}
        val properties = game.properties.filter { it.ownerId == debtor.id }.toSet()
        properties.forEach { propertyTransferer.transferProperty(it, creditor, game) }
    }

    private fun turnOverCashToCreditor(bill: Bill, game: Game) {
        val debtor = bill.debtor!!
        val creditor = bill.creditor
        val cash = debtor.cash
        if (cash > 0) {
            cashTransferer.applyBill(Bill(debtor = debtor, creditor = creditor, cash), game)
        }
    }

    private fun turnOverFreeBailCardsToCreditor(bill: Bill, game: Game) {
        val debtor = bill.debtor!!
        val creditor = bill.creditor
        val freeBailCards = debtor.freeBailCards
        if (freeBailCards > 0) {
            freeBailCardAccounter.transferFreeBailCards(Bill(debtor = debtor, creditor = creditor, amount = freeBailCards), game)
        }
    }




}