package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Pile
import org.monsim.impl.play.CardDrawerImpl

class CardDrawerTest {
    private val log = KotlinLogging.logger {}
    private val cardDrawer = CardDrawerImpl()

    @Test
    fun drawCard() {
        val pile = Pile(id = 1)
        val cards = listOf(
                Card(id = 1, timesUsed = 0),
                Card(id = 2, timesUsed = -1),
                Card(id = 3, timesUsed = 2),
                Card(id = 4, timesUsed = 1),
                Card(id = 5, timesUsed = -2, pileId = 2),
        )
        val game = Game(
                cards = cards,
                piles = listOf(pile)
        )
        assertEquals(Card(id = 2, timesUsed = 0), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 2, timesUsed = 1), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 1, timesUsed = 1), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 1, timesUsed = 2), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 4, timesUsed = 2), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 2, timesUsed = 2), cardDrawer.drawCard(pile, game))
        assertEquals(Card(id = 1, timesUsed = 3), cardDrawer.drawCard(pile, game))
    }





}