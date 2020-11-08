package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.api.play.MultiPlayerPayer
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Turn

class MultiPlayerPayerTest {
    private val log = KotlinLogging.logger {}
    private val multiPlayerPayer: MultiPlayerPayer = MultiPlayerPayerImpl()

    @Test
    fun cashFromOtherPlayers(){
        val player1 = Player(id = 101, cash = 500)
        val player2 = Player(id = 102, cash = 600)
        val player3 = Player(id = 103, cash = 700)
        val player4 = Player(id = 104, cash = 800, lost = true)
        val game = Game(
                players = listOf(player1, player2, player3, player4),
                turn = Turn(playerId = player1.id)
        )
        multiPlayerPayer.cashFromOtherPlayers(40, game)
        assertEquals(Player(id = 101, cash = 580), player1)
        assertEquals(Player(id = 102, cash = 560), player2)
    }

    @Test
    fun cashToOtherPlayers(){
        val player1 = Player(id = 101, cash = 500)
        val player2 = Player(id = 102, cash = 600)
        val player3 = Player(id = 103, cash = 700)
        val player4 = Player(id = 104, cash = 800, lost = true)
        val game = Game(
                players = listOf(player1, player2, player3, player4),
                turn = Turn(playerId = player1.id)
        )
        multiPlayerPayer.cashToOtherPlayers(40, game)
        assertEquals(Player(id = 101, cash = 420), player1)
        assertEquals(Player(id = 102, cash = 640), player2)
    }


}