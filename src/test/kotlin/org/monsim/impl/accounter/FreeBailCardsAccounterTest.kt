package org.monsim.impl.accounter

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.Bill
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.accounting.FreeBailCardAccounterImpl

class FreeBailCardsAccounterTest {
    private val log = KotlinLogging.logger {}
    private val freeBailCardAccounter = FreeBailCardAccounterImpl()

    @Test
    fun transferFreeBailCards() {
        val player1 = Player(id = 1, freeBailCards = 15)
        val player2 = Player(id = 2, freeBailCards = 19)
        freeBailCardAccounter.transferFreeBailCards(Bill(debtor = player1, creditor = player2, amount = 4), Game())
        assertEquals(11, player1.freeBailCards)
        assertEquals(23, player2.freeBailCards)
    }



}