package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.Jailer
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.type.OutOfJailWay
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.accounting.CashAccounterImpl
import org.monsim.impl.accounting.FreeBailCardAccounterImpl
import java.lang.RuntimeException

class JailerImpl : Jailer {

    val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()
    private val freeBailCardAccounter = FreeBailCardAccounterImpl()
    private val gameUtil = GameUtilImpl()

    override fun jail(game: Game) {
        val player = gameUtil.currentPlayer(game)
        log.debug{"jailing player#${player.id}"}
        when{
            player.jailed -> throw RuntimeException("can't jail. player already jailed")
        }
        alterJail(player, true)
        player.spaceId = game.rule.jailSpaceId
    }

    override fun free(outOfJailWay: OutOfJailWay, game: Game) {
        val player = gameUtil.currentPlayer(game)
        val reasonCantFree = reasonCantFree(outOfJailWay, game)
        when{
            reasonCantFree != null -> throw RuntimeException("can't free player#${player.id}: $reasonCantFree")
        }
        when (outOfJailWay){
            OutOfJailWay.ROLLED_DOUBLES -> log.debug{"player#${player.id} released from jail for rolling doubles"}
            OutOfJailWay.BAIL -> bail(player, game)
            OutOfJailWay.CARD -> useCard(player, game)
        }
        alterJail(player, false)
    }

    override fun mayFree(outOfJailWay: OutOfJailWay, game: Game):Boolean {
        val reasonCantFree = reasonCantFree(outOfJailWay, game)
        val mayFree = reasonCantFree == null
        val player = gameUtil.currentPlayer(game)
        log.debug{"${if (mayFree) "can" else "can't"} free player#${player.id} with ${outOfJailWay}. ${if (mayFree) "" else reasonCantFree}"}
        return mayFree
    }

    private fun reasonCantFree(outOfJailWay: OutOfJailWay, game: Game): String? {
        val player = gameUtil.currentPlayer(game)
        val bail = game.rule.bail
        val freeBailCards = player.freeBailCards
        val cash = player.cash
        val reasonCantFree =
                when {
                    !player.jailed -> "player isn't jailed"
                    outOfJailWay == OutOfJailWay.BAIL && cash < bail -> "can't bail because player's cash $cash} is less than bail ${bail}"
                    outOfJailWay == OutOfJailWay.CARD && freeBailCards < 1 -> "can't use freeBailCard for playerId#${player.id} because freeBailCards=${freeBailCards}"
                    else -> null
                }
        return reasonCantFree
    }

    private fun bail(player: Player, game: Game) {
        val bail = game.rule.bail
        log.debug{"bailing player#${player.id} for a cost of $${bail}"}
        cashTransferer.applyBill(Bill(debtor = player, amount = bail), game)
    }

    private fun useCard(player: Player, game: Game) {
        log.debug{"using freeBailCard for player#${player.id}"}
        freeBailCardAccounter.transferFreeBailCards(Bill(debtor =  player, amount = 1), game)
    }

    private fun alterJail(player: Player, isJailed: Boolean){
        val changed = isJailed != player.jailed
        player.jailed = isJailed
        if (changed){
            player.rollsInJail = 0
        }
    }
}