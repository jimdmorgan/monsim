package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Renter
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.PricerImpl
import org.monsim.impl.accounting.CashAccounterImpl

class RenterImpl : Renter {

    private val gameUtil = GameUtilImpl()
    private val pricer = PricerImpl()
    private val cashTransferer = CashAccounterImpl()
    private val log = KotlinLogging.logger {}


    override fun chargeRentIfNecessary(game: Game) {
        val renter = gameUtil.currentPlayer(game)
        val space = gameUtil.currentSpace(game)
        val reasonRentIsFree = reasonRentIsFree(game)
        if (reasonRentIsFree != null){
            log.debug{"no rent charged to player#${renter.id} for space#${space.id}('${space.name}') because ${reasonRentIsFree}"}
            return
        }
        chargeRent(game)
    }

    private fun chargeRent(game: Game){
        val space = gameUtil.currentSpace(game)
        val property = gameUtil.propertyForSpace(space, game)!!
        val owner = gameUtil.playerForId(property.ownerId, game)!!
        val renter = gameUtil.currentPlayer(game)
        val rent = pricer.rent(property, game)
        log.debug{"player#${renter.id} must pay player#${owner.id} $${rent} in rent after landing on SpaceId#${space.id}('${space.name}') PropertyId#${property.id}"}
        cashTransferer.applyBill(Bill(debtor =  renter, creditor = owner, amount = rent), game)
        property.incomeGenerated += rent
    }

    private fun reasonRentIsFree(game: Game): String? {
        val renter = gameUtil.currentPlayer(game)
        val property = gameUtil.currentProperty(game)
        val reason = when {
            property == null -> "space isn't a property"
            property.ownerId == null -> "property has no owner"
            property.ownerId == renter.id -> "player owns property"
            property.mortgaged -> "property is mortgaged"
            else -> null
        }
        return reason
    }


}