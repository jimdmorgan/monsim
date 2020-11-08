package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.api.play.Renter
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Turn
import org.monsim.bean.type.GroupType

class RenterTest {
    private val log = KotlinLogging.logger {}
    private val renter: Renter = RenterImpl()


    @Test
    fun chargeRent() {
        val space = Space(id = 50)
        val player1 = Player(id = 101, cash = 500, spaceId = space.id)
        val player2 = Player(id = 102, cash = 1500)
        val property = Property(id = 1, groupId = 9, spaceId = space.id, ownerId = player2.id, houses = 1)
        val group = Group(id = 9, groupType = GroupType.DEVELOPABLE, rents = listOf(listOf(5, 17, 33, 61)))
        val game = Game(
                players = listOf(player1, player2),
                properties = listOf(property),
                groups = listOf(group),
                turn = Turn(playerId = player1.id),
                spaces = listOf(space)
        )
        renter.chargeRentIfNecessary(game)
        assertEquals(Player(id = 101, cash = 483, spaceId = space.id), player1)
        assertEquals(Player(id = 102, cash = 1517), player2)
    }



}