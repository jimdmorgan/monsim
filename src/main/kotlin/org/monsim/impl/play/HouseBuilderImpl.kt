package org.monsim.impl.play

import org.monsim.api.play.HouseBuilder
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import org.monsim.impl.util.PricerImpl

import mu.KotlinLogging
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType
import org.monsim.impl.accounting.CashAccounterImpl
import org.monsim.impl.accounting.HouseAccounterImpl
import java.lang.RuntimeException

class HouseBuilderImpl : HouseBuilder {

    private val houseTransferer = HouseAccounterImpl()
    private val monopolyUtil = MonopolyUtilImpl()
    private val gameUtil = GameUtilImpl()
    private val pricer = PricerImpl()
    private val cashTransferer = CashAccounterImpl()
    private val log = KotlinLogging.logger {}
    private val BANK: Player? = null


    data class Transaction(
            val property: Property,
            val isAdding: Boolean,
            val owner: Player?,
            val buyer: Player?,
            val seller: Player?,
            val price: Int,
            val verb: String
    )

    fun transaction(property: Property, isAdding: Boolean, game: Game): Transaction {
        val owner = gameUtil.playerForId(property.ownerId, game)
        val isDevelopable = gameUtil.groupForProperty(property, game).groupType == GroupType.DEVELOPABLE
        val price = if (!isDevelopable) -1 else if (isAdding) pricer.houseBuy(property, game) else pricer.houseSell(property, game)
        val transaction = Transaction(
                isAdding = isAdding,
                property = property,
                verb = if (isAdding) "add" else "remove",
                price = price,
                owner = owner,
                buyer = if (isAdding) owner else BANK,
                seller = if (isAdding) BANK else owner
        )
        return transaction
    }

    override fun changeHouse(property: Property, isAdding: Boolean, game: Game) {
        val transaction = transaction(property, isAdding, game)
        val verb = transaction.verb
        log.debug { "${verb} house on propertyId#${property.id}" }
        val reasonWhyIllegal = reasonWhyChangeHouseIllegal(transaction, game)
        when{
            reasonWhyIllegal != null -> throw RuntimeException("Can't ${verb} house on property#${property.id}: $reasonWhyIllegal")
        }
        val debt = Bill(debtor = transaction.buyer, creditor = transaction.seller, transaction.price)
        houseTransferer.transferHouse(property, isAdding, game)
        cashTransferer.applyBill(debt, game)
    }

    override fun isChangeHouseLegal(property: Property, isAdding: Boolean, game: Game): Boolean {
        val transaction = transaction(property, isAdding, game)
        return reasonWhyChangeHouseIllegal(transaction, game) == null
    }

    fun reasonWhyChangeHouseIllegal(transaction: Transaction, game: Game): String? {
        val property = transaction.property
        val owner = transaction.owner
        val subError = houseTransferer.transferHouseError(transaction.property, transaction.isAdding, game)
        val error = when {
            subError != null -> subError
            transaction.isAdding && (transaction.price > owner!!.cash)
                -> "Player#${owner!!.id} doesn't have enough cash to add house for property#${property.id}"
            else -> null
        }
        return error
    }
}