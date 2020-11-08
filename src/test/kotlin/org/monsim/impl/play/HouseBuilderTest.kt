package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType

class HouseBuilderTest {
    private val log = KotlinLogging.logger {}
    private val houseBuilder = HouseBuilderImpl()


    @Test
    fun addHouse(){
        val property = Property(id = 1, ownerId = 101, groupId = 9, houses = 0)
        val player = Player(id = 101, cash = 500)
        val group = Group(id = 9, groupType = GroupType.DEVELOPABLE, housePrice = 100)

        val game = Game(
                players = listOf(player),
                groups = listOf(group),
                properties = listOf(property)
        )
        houseBuilder.changeHouse(property, true, game)
        assertEquals(Property(id = 1, ownerId = 101, groupId = 9, houses = 1), property)
        assertEquals(Player(id = 101, cash = 400), player)
    }

    @Test
    fun subtractHouse(){
        val property = Property(id = 1, ownerId = 101, groupId = 9, houses = 3)
        val player = Player(id = 101, cash = 500)
        val group = Group(id = 9, groupType = GroupType.DEVELOPABLE, housePrice = 100)

        val game = Game(
                players = listOf(player),
                groups = listOf(group),
                properties = listOf(property)
        )
        houseBuilder.changeHouse(property, false, game)
        assertEquals(Property(id = 1, ownerId = 101, groupId = 9, houses = 2), property)
        assertEquals(Player(id = 101, cash = 550), player)
    }

}