package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Rule
import org.monsim.bean.domain.Turn
import org.monsim.bean.type.GroupType

class PricerTest {
    private val log = KotlinLogging.logger {}
    private val pricer = PricerImpl()


    @Test
    fun rent() {
        val property = Property(id = 101, groupId = 9, ownerId = 1, houses = 2)
        val game = Game(
                groups = listOf(Group(id = 9, groupType = GroupType.DEVELOPABLE, rents=listOf(listOf(50, 200, 600, 1400, 1700, 2000)))),
                properties = listOf(property),
                rule = Rule()
        )
        assertEquals(600, pricer.rent(property, game))
    }

    @Test
    fun rentNoHouses() {
        val property1 = Property(id = 101, groupId = 9, ownerId = 1, houses = 0)
        val property2 = Property(id = 102, groupId = 9, ownerId = null, houses = 0)
        val game = Game(
                groups = listOf(Group(id = 9, groupType = GroupType.DEVELOPABLE, rents=listOf(listOf(52, 200, 600, 1400, 1700, 2000)))),
                properties = listOf(property1, property2),
                rule = Rule()
        )
        assertEquals(52, pricer.rent(property1, game))
        property2.ownerId = 1
        assertEquals(104, pricer.rent(property1, game))
    }


    @Test
    fun rentFixed() {
        val property1 = Property(id = 101, groupId = 9, ownerId = 1, houses = 2)
        val property2 = Property(id = 102, groupId = 9, ownerId = null, houses = 2)
        val game = Game(
                groups = listOf(Group(id = 9, groupType = GroupType.FIXED, rents=listOf(listOf(24, 52, 108, 221)))),
                properties = listOf(property1, property2),
                rule = Rule()
        )
        assertEquals(24, pricer.rent(property1, game))
        property2.ownerId = 1
        assertEquals(52, pricer.rent(property1, game))
    }

    @Test
    fun rentDice() {
        val property1 = Property(id = 101, groupId = 9, ownerId = 1, houses = 2)
        val property2 = Property(id = 102, groupId = 9, ownerId = null, houses = 2)
        val game = Game(
                groups = listOf(Group(id = 9, groupType = GroupType.DICE, rents=listOf(listOf(4, 10)))),
                properties = listOf(property1, property2),
                turn = Turn(rollSum = 7)
        )
        assertEquals(28, pricer.rent(property1, game))
        property2.ownerId = 1
        game.turn.rollSum = 8
        assertEquals(80, pricer.rent(property1, game))
    }


}