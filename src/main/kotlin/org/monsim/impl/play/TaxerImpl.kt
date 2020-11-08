package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Taxer
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Tax
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl

class TaxerImpl : Taxer {

    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val gameUtil = GameUtilImpl()

    override fun tax(tax: Tax, game: Game) {
        val player = gameUtil.currentPlayer(game)
        val amount = tax.amount
        log.debug{"taxing player#${player.id} amount of ${amount}"}
        cashTransferer.applyBill(Bill(debtor=player, amount = amount), game)
    }


}