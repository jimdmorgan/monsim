package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Mortgager
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Property
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import org.monsim.impl.util.PricerImpl
import org.monsim.impl.accounting.CashAccounterImpl

class MortgagerImpl : Mortgager {

    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val monopolyUtil = MonopolyUtilImpl()
    private val pricer = PricerImpl()
    private val gameUtil = GameUtilImpl()


    override fun mortgage(property: Property, game: Game) {

        val amountToOwner = pricer.mortgage(property, game)
        val owner = gameUtil.playerForId(property.ownerId, game)!!
        val space = gameUtil.spaceWithId(property.spaceId, game)
        log.debug { "player#${property.ownerId} is mortgaging property#${property.id}('${space.name}') so the bank will pay him $${amountToOwner}" }
        alterMortgage(property, true, game)
        cashTransferer.applyBill(Bill(creditor = owner, amount = amountToOwner), game)
    }

    override fun mayMortgage(property: Property, game: Game): Boolean {
        return reasonCantMortgage(property, game) == null
    }

    fun reasonCantMortgage(property: Property, game: Game): String? {
        val reason = when {
            game.rule.isMortgagingDisabled -> "game rule doesn't allow mortgaging"
            property.mortgaged -> "Property#${property.id} already mortgaged"
            property.ownerId == null -> "Property#${property.id} has no owner"
            property.houses != null && property.houses!! > 0 -> "Property#${property.id} has houses"
            else -> null
        }
        return reason
    }


    override fun unmortgage(property: Property, game: Game) {

        val amountFromOwner = pricer.unmortgage(property, game)
        val owner = gameUtil.playerForId(property.ownerId, game)!!
        val space = gameUtil.spaceWithId(property.spaceId, game)
        log.debug { "player#${property.ownerId} is unmortgaging property#${property.id}('${space.name}') so he will pay the bank $${amountFromOwner}" }
        alterMortgage(property, false, game)
        cashTransferer.applyBill(Bill(debtor = owner, amount = amountFromOwner), game)
    }


    override fun mayUnmortgage(property: Property, game: Game): Boolean {
        return reasonCantUnmortgage(property, game) == null
    }

    fun reasonCantUnmortgage(property: Property, game: Game): String? {
        if (game.rule.isMortgagingDisabled) {
            return "game rule doesn't allow mortgaging (or unmortgaging)"
        }
        val cost = pricer.unmortgage(property, game)
        if (property.ownerId == null) {
            return "Property#${property.id} has no owner"
        }
        val player = gameUtil.playerForId(property.ownerId, game)!!
        val reason = when {
            !property.mortgaged -> "Property#${property.id} isn't mortgaged"
            player.cash < cost -> "Property#${property.id}'s owner Player#${player.id}'s cash (${player.cash} doesn't have enough to unmortgage (${cost})"
            else -> null
        }
        return reason
    }

    private fun alterMortgage(property: Property, isMortgaged: Boolean, game: Game) {
        property.mortgaged = isMortgaged
    }
}