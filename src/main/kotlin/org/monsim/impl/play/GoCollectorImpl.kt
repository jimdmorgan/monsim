package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.GoCollector
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl

class GoCollectorImpl : GoCollector {

    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val gameUtil = GameUtilImpl()

    override fun collectForPassingGo(game: Game): Unit {
        val player = gameUtil.currentPlayer(game)
        val amount = game.rule.cashForPassingGo
        log.debug{"paying player#${player.id} $${amount} for passing go"}
        cashTransferer.applyBill(Bill(creditor = player, amount = amount), game)
    }



}