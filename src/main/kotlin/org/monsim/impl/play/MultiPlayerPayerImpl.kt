package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.MultiPlayerPayer
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl
import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.RuntimeException

class MultiPlayerPayerImpl : MultiPlayerPayer {

    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val gameUtil = GameUtilImpl()

    override fun cashToOtherPlayers(amount: Int, game: Game) {
        verifyNonNegative(amount)
        val debtor = gameUtil.currentPlayer(game)
        val cash = debtor.cash
        verifyNonNegative(cash)
        val otherPlayers = otherPlayers(debtor, game)
        log.debug{"Player#${debtor.id} is giving $amount to each of the other ${otherPlayers.size} player(s) for a total of ${amount * otherPlayers.size}"}
        val actualAmount = min(amount, cash / otherPlayers.size)
        if (actualAmount < amount){ //avoid having multiple creditors at once
            log.debug{"player#${debtor.id} was supposed to give the other ${otherPlayers.size} player(s) $${amount} each, but since he only has $${cash} he will only pay them $${actualAmount} each"}
        }
        otherPlayers.forEach{
            cashTransferer.applyBill(Bill(debtor = debtor, creditor = it, amount = actualAmount), game)
        }
    }

    override fun cashFromOtherPlayers(amount: Int, game: Game) {
        verifyNonNegative(amount)
        val creditor = gameUtil.currentPlayer(game)
        val otherPlayers = otherPlayers(creditor, game)
        log.debug{"Player#${creditor.id} is getting $amount from each of the other ${otherPlayers.size} player(s) for a total of ${amount * otherPlayers.size}"}
        otherPlayers.forEach{
            val actualAmount = max(0, min(amount, it.cash)) //since it's not the other player's turn, don't take more cash than they have
            cashTransferer.applyBill(Bill(debtor=it, creditor = creditor, amount = actualAmount), game)
        }
    }

    private fun otherPlayers(player: Player, game: Game): Set<Player>{
        val otherPlayers = gameUtil.playersStillInGame(game).filter{it.id != player.id}.toSet()
        return otherPlayers
    }

    private fun verifyNonNegative(amount: Int) {
        if (amount < 0) {
            throw RuntimeException("amount $amount is negative")
        }
    }

}