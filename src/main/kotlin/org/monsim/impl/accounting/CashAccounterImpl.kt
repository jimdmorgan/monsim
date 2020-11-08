package org.monsim.impl.accounting

import mu.KotlinLogging
import org.monsim.api.accounting.CashAccounter
import org.monsim.bean.Bill
import org.monsim.bean.Constants
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.StringUtilImpl
import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.RuntimeException

class CashAccounterImpl : CashAccounter {


    override fun applyBill(bill: Bill, game: Game) {
        Delegate(game).applyDebt(bill)
    }

    class Delegate(val game: Game) {
        val log = KotlinLogging.logger {}
        val stringUtil = StringUtilImpl()

        fun applyDebt(bill: Bill) {
            val from = bill.debtor
            val to = bill.creditor
            val amount = bill.amount
            log.debug{"debit:${stringUtil.player(from)} credit:${stringUtil.player(to)} amount:$$amount"}
            checkConditions(bill)
            val actualCash = getActualCash(from, amount)
            val actualDebt = Bill(debtor = bill.debtor, creditor = bill.creditor, amount = actualCash)
            subtractActualCash(actualDebt)
            addActualCash(actualDebt)
            val debit = amount - actualCash
            if (debit > 0) {
                establishDebit(Bill(debtor = bill.debtor, creditor = bill.creditor, amount = debit))
            }
        }

        private fun checkConditions(bill: Bill) {
            val amount = bill.amount
            verifyNonNegative(amount)
            when {
                bill.debtor == bill.creditor -> throw RuntimeException("cashTransfer from & to player are the same: ${bill.creditor}")
                else -> return
            }
        }

        private fun establishDebit(bill: Bill) {
            val debtor = bill.debtor
            val creditor = bill.creditor
            val amount = bill.amount
            when {
                debtor == Constants.BANK -> throw RuntimeException("Bank never goes into debt")
                amount <= 0 -> throw RuntimeException("debit amount must be positive: $amount")
            }
            val oldCash = debtor!!.cash
            when{
                oldCash < 0 -> throw RuntimeException("Player#${debtor.id} is already in debt (cash:$$debtor.cash)")
                oldCash > 0 -> throw RuntimeException("Player#${debtor.id} must spend all cash before going into debt (cash:$$oldCash)")
            }
            debtor.creditorId = if (creditor == Constants.BANK) Constants.BANK_ID else creditor!!.id
            debtor.cash = amount * -1
            log.debug("player#${debtor.id} has debt of ${amount} to ${stringUtil.player(creditor)} (cash=$${debtor.cash})")
        }

        /** Gets the amount of actual cash that will be transferred.  (The rest will have to be set up as a debt.) */
        private fun getActualCash(player: Player?, amount: Int): Int {
            verifyNonNegative(amount)
            val availableCash = getAvailableCash(player)
            val actualCash = min(amount, availableCash)
            return actualCash
        }

        private fun getAvailableCash(player: Player?): Int {
            val availableCash = if (player == Constants.BANK) Int.MAX_VALUE else max(0, player!!.cash)
            verifyNonNegative(availableCash)
            return availableCash
        }

        private fun subtractActualCash(bill: Bill) {
            val amount = bill.amount
            val player = bill.debtor
            verifyNonNegative(amount)
            if (player == Constants.BANK) {
                return;
            }
            player!!
            if (amount > player.cash) {
                throw RuntimeException("player#${player.id} doesn't have $${amount} to give (cash:$${player.cash})")
            }
            val oldAmount = player.cash
            player.cash -= amount
            log.debug { "player#${player.id} gave $$amount in cash (to:${stringUtil.player(bill.creditor)})  oldCash:$$oldAmount newCash:$${player.cash}" }
        }

        private fun addActualCash(bill: Bill) {
            val amount = bill.amount
            val player = bill.creditor
            verifyNonNegative(amount)
            if (player == Constants.BANK) {
                return;
            }
            player!!
            val oldCash = player.cash
            payCreditorIfInDebt(bill)
            player.cash = oldCash + amount
            val wasInDebt = oldCash < 0
            val debtNotice = if (wasInDebt) "NOTE:player was in debt so part/all of the cash went straight to creditor" else ""
            log.debug { "player#${player.id} received $$amount in cash (from:${stringUtil.player(bill.debtor)}) oldCash:$$oldCash newCash:$${player.cash} ${debtNotice}" }
        }

        private fun payCreditorIfInDebt(bill: Bill) {
            val amount = bill.amount
            val player = bill.creditor!!
            val debtAmount = player.cash * -1
            if (debtAmount <= 0) {
                return
            }
            if (player.id == player.creditorId){
                throw RuntimeException("player can't be in debt to himself")
            }
            val creditor = playerForId(player.creditorId)
            val payment = min(amount, debtAmount)
            log.debug { "player#${player.id} received $$amount in cash (from ${stringUtil.player(bill.debtor)}), but is in debt $${debtAmount} to ${stringUtil.player(creditor)}, who will be paid $${payment} now" }
            addActualCash(Bill(debtor = player, creditor = creditor, amount = payment))
            if (payment == debtAmount){
                player.creditorId = Constants.BANK_ID
            }
        }

        private fun verifyNonNegative(amount: Int) {
            if (amount < 0) {
                throw RuntimeException("amount $amount is negative")
            }
        }

        private fun playerForId(id: Int?): Player? {
            if (id == Constants.BANK_ID){
                return Constants.BANK
            }
            val matching = game.players.filter { it.id == id }.toSet();
            if (matching.isEmpty()){
                throw RuntimeException("playerId#${id} not found")
            }
            return matching.first()
        }
    }


}