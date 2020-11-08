package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.api.play.PropertyBuyer
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Turn
import org.monsim.impl.play.PropertyBuyerImpl

class PropertyBuyerTest {
    private val log = KotlinLogging.logger {}
    private val propertyBuyer: PropertyBuyer = PropertyBuyerImpl()


    @Test
    fun buyProperty() {
        val space = Space(id = 50)
        val player = Player(id = 101, cash = 500, spaceId = space.id)
        val property = Property(id = 1, groupId = 9, ownerId = null, spaceId = space.id)
        val group = Group(id = 9, buyPrices = listOf(51))
        val game = Game(
                properties = listOf(property),
                groups = listOf(group),
                turn = Turn(playerId = player.id),
                spaces = listOf(space),
                players = listOf(player)

        )
        propertyBuyer.buyProperty(game)
        assertEquals(Player(id = 101, cash = 449, spaceId = space.id), player)
        assertEquals(Property(id = 1, groupId = 9, ownerId = player.id, spaceId = space.id), property)
    }

}