package org.monsim.impl.accounting

import mu.KotlinLogging
import org.monsim.api.accounting.FreeBailCardAccounter
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.StringUtilImpl
import java.lang.Math.abs
import java.lang.RuntimeException

class FreeBailCardAccounterImpl : FreeBailCardAccounter {

    private val gameUtil = GameUtilImpl()
    private val stringUtil = StringUtilImpl()
    private val log = KotlinLogging.logger {}

    override fun transferFreeBailCards(bill: Bill, game: Game) {
        val from = bill.debtor
        val to = bill.creditor
        val amount = bill.amount
        log.debug{"tranferFreeBailCards from:${stringUtil.player(from)} to:${stringUtil.player(to)} amount:$amount"}
        when {
            amount < 0 -> throw RuntimeException("amount is negative: ${amount}")
            from == to -> throw RuntimeException("from player is same as to player: $from")
            from != null && from.freeBailCards < amount -> throw RuntimeException("from player doesn't have enough freeBailCards needed:$amount has:${from.freeBailCards}")
        }
        alterFreeBailCards(from, amount * -1)
        alterFreeBailCards(to, amount)
    }

    private fun alterFreeBailCards(player: Player?, amount: Int){
        if (player == null){
            return
        }
        val verb = if (amount >= 0) "gained" else "lost"
        val oldAmount = player.freeBailCards
        val newAmount = oldAmount + amount
        player.freeBailCards = newAmount
        log.debug("${stringUtil.player(player)} ${verb} ${abs(amount)} freeBailCard(s) old:$oldAmount new:${newAmount}")
    }


}