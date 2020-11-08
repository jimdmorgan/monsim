package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Tax
import org.monsim.bean.domain.Turn

class TaxerTest {
    private val log = KotlinLogging.logger {}
    private val taxer = TaxerImpl()


    @Test
    fun tax(){
        val player = Player(id = 101, cash = 500)
        val game = Game(
                players=listOf(player),
                turn = Turn(playerId = player.id)
        )
        taxer.tax(Tax(amount = 60), game)
        assertEquals(Player(id = 101, cash = 440), player)
    }


}