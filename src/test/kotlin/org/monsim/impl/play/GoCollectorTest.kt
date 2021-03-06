package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Turn

class GoCollectorTest {
    private val log = KotlinLogging.logger {}
    private val collector = GoCollectorImpl()

    @Test
    fun collect(){
        val player = Player(id = 101, cash = 500)
        val game = Game(
                players=listOf(player),
                turn = Turn(playerId = player.id)
        )
        collector.collectForPassingGo(game)
        assertEquals(Player(id = 101, cash = 700), player)
    }


}