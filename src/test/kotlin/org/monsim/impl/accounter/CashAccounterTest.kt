package org.monsim.impl.accounter

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.accounting.CashAccounterImpl

class CashAccounterTest {
    private val log = KotlinLogging.logger {}
    private val cashTransferer = CashAccounterImpl()

    @Test
    fun cashTransferer_2player() {
        val player1 = Player(id = 1, cash = 15)
        val player2 = Player(id = 2, cash = 45)
        cashTransferer.applyBill(Bill(debtor = player1, creditor = player2, amount = 5), Game())
        assertEquals(10, player1.cash)
        assertEquals(50, player2.cash)
    }

    @Test
    fun cashTransferer_bank_to() {
        val player = Player(id = 1, cash = 15)
        cashTransferer.applyBill(Bill(debtor = player, creditor = null, amount = 5),  Game())
        assertEquals(10, player.cash)
    }

    @Test
    fun cashTransferer_bank_from() {
        val player = Player(id = 1, cash = 15)
        cashTransferer.applyBill(Bill(debtor = null, creditor = player, amount = 100), Game())
        assertEquals(115, player.cash)
    }

    @Test
    fun cashTransferer_debit() {
        val player1 = Player(id = 1, cash = 15)
        val player2 = Player(id = 2, cash = 45)
        cashTransferer.applyBill(Bill(debtor = player1, creditor = player2, amount = 50),  Game())
        assertEquals(Player(id = 1, cash = -35, creditorId = 2), player1)
        assertEquals(Player(id = 2, cash = 60), player2)
    }

    @Test
    fun cashTransferer_payDebtInFull() {
        val player1 = Player(id = 1, cash = -10, creditorId = 2)
        val player2 = Player(id = 2, cash = 50)
        cashTransferer.applyBill(Bill(debtor = null, creditor = player1, amount = 30),  Game(players = listOf(player1, player2)))
        assertEquals(Player(id = 1, cash = 20), player1)
        assertEquals(Player(id = 2, cash = 60), player2)
    }

    @Test
    fun cashTransferer_payDebtInPartial() {
        val player1 = Player(id = 1, cash = -40, creditorId = 2)
        val player2 = Player(id = 2, cash = 50)
        cashTransferer.applyBill(Bill(debtor = null, creditor = player1, amount = 30),  Game(players = listOf(player1, player2)))
        assertEquals(Player(id = 1, cash = -10, creditorId = 2), player1)
        assertEquals(Player(id = 2, cash = 80), player2)
    }


}