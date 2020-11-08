package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Rule
import org.monsim.bean.domain.Turn
import org.monsim.bean.type.OutOfJailWay

class JailerTest {
    private val log = KotlinLogging.logger {}
    private val jailer = JailerImpl()


    @Test
    fun jail() {
        val player = Player(id = 101)
        val game = Game(
                players = listOf(player),
                turn = Turn(playerId = player.id)
        )
        jailer.jail(game)
        assertEquals(Player(id = 101, jailed = true, spaceId = Rule().jailSpaceId), player)
    }

    @Test
    fun bail() {
        val player = Player(id = 101, jailed = true, cash = 500)

        val game = Game(
                players = listOf(player),
                rule = Rule(bail = 60),
                turn = Turn(playerId = player.id)
        )
        jailer.free(OutOfJailWay.BAIL, game)
        assertEquals(Player(id = 101, jailed = false, cash = 440), player)
    }

}