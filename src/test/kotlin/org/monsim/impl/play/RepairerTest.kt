package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Turn

class RepairerTest {
    private val log = KotlinLogging.logger {}
    private val repairer = RepairerImpl()

    @Test
    fun chargeForHouseRepair(){
        val player1 = Player(id = 101, cash = 500)
        val game = Game(
                properties = listOf(
                        Property(id = 1, houses = null, ownerId = 101),
                        Property(id = 2, houses = 3, ownerId = 101),
                        Property(id = 3, houses = 5, ownerId = 101),
                        Property(id = 4, houses = 11, ownerId = null),
                        Property(id = 5, houses = null, ownerId = null),
                        Property(id = 6, houses = 13, ownerId = 102),
                ),
                turn = Turn(playerId = player1.id),
                players = listOf(player1)
        )
        repairer.chargeForHouseRepair(23, game)
        assertEquals(Player(id = 101, cash = 316), player1)
    }

}